package es.antoniomb.service;

import es.antoniomb.dto.MovieInfo;
import es.antoniomb.dto.UserInfo;
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

    @Override
    public List<MovieInfo> getRatings(String username, String password) {

        UserInfo userInfo = login(username, password);

        fillUserInfo(userInfo);

        return fillMoviesInfo(userInfo);
    }

    @Override
    public Integer setRatings(String username, String password, List<MovieInfo> movieInfo) {
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
                    .execute();
        }
        catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error logging user "+username, e);
        }

        if (login == null || login.cookie(FAUtils.SESSION_COOKIE) == null) {
            throw new RuntimeException("Login error");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setCookies(login.cookies());

        return userInfo;
    }

    public void fillUserInfo(UserInfo userInfo) {
        try {
            //Request for obtaining userId
            Document votesPage = Jsoup.connect(FAUtils.URLS.VOTES.getUrl()).cookies(userInfo.getCookies()).get();
            if (votesPage.body().getElementById("user-nick") != null) {
                Pattern userIdPattern = Pattern.compile("user_id=(\\d+)");
                Matcher userIdMatcher = userIdPattern.matcher(votesPage.body().getElementById("user-nick").toString());
                if (userIdMatcher.find()) {
                    userInfo.setUserId(userIdMatcher.group(1));
                }
            }
            if (userInfo.getUserId() == null) {
                throw new RuntimeException("Error obtaining userID");
            }
            LOGGER.info("User id: "+userInfo.getUserId());

            //Request for rating pages and votes number
            Document ratingsPage = Jsoup.connect(FAUtils.URLS.RATINGS.getUrl()+userInfo.getUserId()).cookies(userInfo.getCookies()).get();

            String pages = ratingsPage.body().getElementsByClass("user-ratings-info-top").get(0).child(0).childNode(3).childNode(0).outerHtml();
            if (pages != null) {
                userInfo.setPages(Integer.valueOf(pages));
            }
            else {
                throw new RuntimeException("Error obtaining rating pages");
            }
            LOGGER.info("User ratings pages: " + userInfo.getPages());

            String votes = ratingsPage.body().getElementsByClass("active-tab").get(0).childNode(1).childNodes().get(0).outerHtml();
            if (votes != null) {
                userInfo.setVotes(Integer.valueOf(votes.substring(1).replaceAll(",","")));
            }
            else {
                throw new RuntimeException("Error obtaining user votes");
            }
            LOGGER.info("User total votes: " + userInfo.getVotes());

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error obtaining ratings", e);
        }
    }

    public List<MovieInfo> fillMoviesInfo(UserInfo userInfo) {
        List<MovieInfo> movies = new ArrayList<>();
        try {
            for (int i=1; i <= userInfo.getPages(); i++) {
                String url = FAUtils.URLS.RATINGS.getUrl() + userInfo.getUserId() + FAUtils.URLS.PAGE_PREFIX.getUrl() + i;
                Document ratingsPage = Jsoup.connect(url).cookies(userInfo.getCookies()).get();

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


                    Elements movieElements = movieDateElement.getElementsByClass("user-ratings-movie");
                    for (Element movieElement : movieElements) {
                        Elements idElement = movieElement.getElementsByClass("movie-card");
                        String id = idElement.get(0).attr("data-movie-id");

                        Elements titleElement = movieElement.getElementsByClass("mc-title");
                        String title = titleElement.get(0).getElementsByTag("a").get(0).childNode(0).outerHtml();
                        String year = titleElement.get(0).getAllElements().get(0).childNode(1).outerHtml().trim().substring(1, 5);

                        Elements ratingElement = movieElement.getElementsByClass("user-ratings-movie-rating");
                        String rating = ratingElement.get(0).getElementsByClass("ur-mr-rat").get(0).childNode(0).outerHtml().substring(1);

                        MovieInfo movieInfo = new MovieInfo(title, year);
                        movieInfo.setId(id);
                        movieInfo.setRate(rating);
                        movieInfo.setDate(date);
                        movies.add(movieInfo);

                        LOGGER.info(movieInfo.toString());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error obtaining ratings", e);
        }

        return movies;
    }

}
