package es.antoniomb.service;

import es.antoniomb.dto.MigrationInput;
import es.antoniomb.dto.MovieInfo;
import es.antoniomb.dto.UserInfo;
import es.antoniomb.exception.MigrationException;
import es.antoniomb.utils.FAUtils;
import es.antoniomb.utils.MigrationUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amiranda on 18/09/16.
 */
@Service
public class FAMigrationService implements IMigrationService {

    private static Logger LOGGER = Logger.getLogger(FAMigrationService.class.getName());

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public List<MovieInfo> getRatings(MigrationInput input) {

        UserInfo userInfo = login(input.getFromUsername(), input.getFromPassword());

        fillUserInfo(userInfo);

        return fillMoviesInfo(userInfo, input.getFromDate(), input.getToDate());
    }

    @Override
    public Integer setRatings(MigrationInput input, List<MovieInfo> movieInfo) {
        throw new RuntimeException("Not implemented!");
    }

    public UserInfo login(String username, String password) {
        Connection.Response login = null;
        try {
            MigrationUtils.disableSSLCertCheck();

            LOGGER.info("User name: " + username);

            //Request for login
            login = Jsoup.connect(FAUtils.URLS.LOGIN_POST.getUrl())
                    .data("postback", "1")
                    .data("rp", "")
                    .data("username", username)
                    .data("password", password)
                    .method(Connection.Method.POST)
                    .validateTLSCertificates(false)
                    .execute();
        }
        catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error logging user "+username, e);
        }

        if (login == null || login.cookie(FAUtils.SESSION_COOKIE) == null) {
            throw new MigrationException("Login error");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setCookies(login.cookies());

        return userInfo;
    }

    public void fillUserInfo(UserInfo userInfo) {
        try {
            //Request for obtaining userId
            Document votesPage = Jsoup.connect(FAUtils.URLS.VOTES.getUrl()).cookies(userInfo.getCookies()).get();
            Element userContainer = votesPage.body().getElementById("user-login-container");
            if (userContainer != null) {
                Pattern userIdPattern = Pattern.compile("user_id=(\\d+)");
                Matcher userIdMatcher = userIdPattern.matcher(userContainer.getElementsByClass("user-menu-wr").get(0).toString());
                if (userIdMatcher.find()) {
                    userInfo.setUserId(userIdMatcher.group(1));
                }
            }
            if (userInfo.getUserId() == null) {
                throw new MigrationException("Error obtaining userID");
            }
            LOGGER.info("User id: "+userInfo.getUserId());

            //Request for rating pages and votes number
            Document ratingsPage = Jsoup.connect(FAUtils.URLS.RATINGS.getUrl()+userInfo.getUserId()).cookies(userInfo.getCookies()).get();


            String pages = "1";
            Elements pager = ratingsPage.body().getElementsByClass("pager").get(0).getElementsByTag("a");
            if (pager.size() > 0) {
                pages = pager.get(pager.size() - 2).childNode(0).outerHtml();
            }
            userInfo.setPages(Integer.valueOf(pages));
            LOGGER.info("User ratings pages: " + userInfo.getPages());

            String votes =  ratingsPage.body().getElementsByClass("active-tab").get(0).childNode(3).childNode(1).outerHtml();
            if (votes != null) {
                userInfo.setVotes(Integer.valueOf(votes.substring(1).replaceAll(",","")));
            }
            else {
                throw new MigrationException("Error obtaining user votes");
            }
            LOGGER.info("User total votes: " + userInfo.getVotes());

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error obtaining ratings", e);
        }
    }

    public List<MovieInfo> fillMoviesInfo(UserInfo userInfo, String fromDate, String toDate) {
        List<MovieInfo> movies = new ArrayList<>();
        List<Future<List<MovieInfo>>> futures = new ArrayList<>();
        for (int i=1; i <= userInfo.getPages(); i++) {
            int page = i;
            futures.add(executorService.submit(() ->
                    fillMoviesInfoPage(userInfo, fromDate, toDate, page)));
        }

        //Wait for futures
        int futuresDone = 0;
        while (true) {
            for (Future<List<MovieInfo>> future : futures) {
                try {
                    movies.addAll(future.get());
                    futuresDone++;
                } catch (InterruptedException|ExecutionException e) {
                    LOGGER.log(Level.WARNING, "Error obtaining ratings", e);
                }
            }

            if (futuresDone == userInfo.getPages()) {
                break;
            }

            //Wait for next iteration
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

        return movies;
    }

    private List<MovieInfo> fillMoviesInfoPage(UserInfo userInfo, String fromDate, String toDate, int i) {
        List<MovieInfo> movies = new ArrayList<>();
        String url = FAUtils.URLS.RATINGS.getUrl() + userInfo.getUserId() + FAUtils.URLS.PAGE_PREFIX.getUrl() + i;
        Document ratingsPage = null;
        try {
            ratingsPage = Jsoup.connect(url).cookies(userInfo.getCookies()).get();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error loading cookies", e);
        }

        Elements movieDateElements = ratingsPage.body().getElementsByClass("user-ratings-wrapper");
        for (Element movieDateElement : movieDateElements) {
            Elements dateElement = movieDateElement.getElementsByClass("user-ratings-header");
            String date = dateElement.get(0).childNode(0).outerHtml().substring(10);

            try {
                String[] split = date.replaceAll(",", "").split(" ");
                date = split[2]+"-"+ //year
                       String.format("%02d", FAUtils.MONTH_FORMAT.parse(split[0]).getMonth()+1) + "-" + //month
                       String.format("%02d", Integer.valueOf(split[1])); //day
            } catch (ParseException e) {
                LOGGER.log(Level.WARNING, "Error parsing date "+date, e);
            }

            //If date ranges defined, escape if its out of range
            if ((fromDate != null && date.compareTo(fromDate) < 0) ||
                (toDate != null && date.compareTo(toDate) > 0) ) {
                continue;
            }

            Elements movieElements = movieDateElement.getElementsByClass("user-ratings-movie");
            for (Element movieElement : movieElements) {
                Elements idElement = movieElement.getElementsByClass("movie-card");
                String id = idElement.get(0).attr("data-movie-id");

                Elements titleElement = movieElement.getElementsByClass("mc-title");
                String title = titleElement.get(0).getElementsByTag("a").get(0).childNode(0).outerHtml();
                boolean isShortMovie = title.contains("(S)");
                boolean isTVSerie = title.contains("(TV Series)");
                boolean isDocumentary = false;
                title = title.replaceAll("\\(S\\)","");
                title = title.replaceAll("\\(TV Series\\)","");
                title = title.replaceAll("\\(TV\\)","").trim();
                String year = titleElement.get(0).getAllElements().get(0).childNode(1).outerHtml().trim().substring(1, 5);
                String country = titleElement.get(0).getElementsByTag("img").get(0).attr("title");

                Elements directorElement = movieElement.getElementsByClass("mc-director");
                String director = directorElement.get(0).getElementsByTag("a").get(0).childNode(0).outerHtml();

                List<String> actors = new ArrayList<>();
                Elements castElement = movieElement.getElementsByClass("mc-cast").get(0).getElementsByClass("nb");
                if (castElement.size() > 0) {
                    for (Element element : castElement) {
                        String actor = element.getElementsByTag("a").get(0).childNode(0).outerHtml();
                        if (actor.equals("Documentary")) {
                            isDocumentary = true;
                        }
                        actors.add(actor);
                    }
                }

                Elements ratingElement = movieElement.getElementsByClass("user-ratings-movie-rating");
                String rating = ratingElement.get(0).getElementsByClass("ur-mr-rat").get(0).childNode(0).outerHtml().substring(1);

                MovieInfo movieInfo = new MovieInfo(title, year);
                movieInfo.setId(id);
                movieInfo.setRate(rating);
                movieInfo.setDate(date);
                movieInfo.setCountry(country);
                movieInfo.setDirector(director);
                movieInfo.setActors(actors);
                movieInfo.setShortMovie(isShortMovie);
                movieInfo.setDocumentary(isDocumentary);
                movieInfo.setTVSerie(isTVSerie);
                movies.add(movieInfo);

                LOGGER.info(movieInfo.toString());
            }
        }
        return movies;
    }

}
