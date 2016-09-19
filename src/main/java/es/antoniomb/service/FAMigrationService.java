package es.antoniomb.service;

import es.antoniomb.dto.MovieInfoDTO;
import es.antoniomb.dto.UserInfoDTO;
import es.antoniomb.utils.FAUtils;
import es.antoniomb.utils.MigrationUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
public class FAMigrationService {

    private static Logger LOGGER = Logger.getLogger(FAMigrationService.class.getName());

    public void migrate(String username, String password) {

        UserInfoDTO userInfo = login(username, password);

        fillUserInfo(userInfo);

        fillMoviesInfo(userInfo);
    }

    public UserInfoDTO login(String username, String password) {
        Connection.Response login = null;
        try {
            MigrationUtils.disableSSLCertCheck();

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

        if (login == null) {
            throw new RuntimeException("Login error");
        }

        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setCookies(login.cookies());

        return userInfo;
    }

    public void fillUserInfo(UserInfoDTO userInfo) {
        try {
            //Request for votes
            Document votesPage = Jsoup.connect(FAUtils.URLS.VOTES.getUrl()).cookies(userInfo.getCookies()).get();

            Pattern userIdPattern = Pattern.compile("user_id=(\\d+)");
            Matcher userIdMatcher = userIdPattern.matcher(votesPage.body().getElementById("user-nick").toString());
            if (userIdMatcher.find()) {
                userInfo.setUserId(userIdMatcher.group(1));
            }
            else {
                throw new RuntimeException("Error obtaining userID");
            }
            LOGGER.info("User id: "+userInfo.getUserId());

            //Request for ratings
            Document ratingsPage = Jsoup.connect(FAUtils.URLS.RATINGS.getUrl()+userInfo.getUserId()).cookies(userInfo.getCookies()).get();

            Pattern pagePattern = Pattern.compile("Page \n  <b>1</b> of \n  <b>(\\d+)</b>");
            Matcher pageMatcher = pagePattern.matcher(ratingsPage.body().getElementsByClass("user-ratings-info-top").toString());
            if (pageMatcher.find()) {
                userInfo.setPages(Integer.valueOf(pageMatcher.group(1)));
            }
            else {
                throw new RuntimeException("Error obtaining rating pages");
            }
            LOGGER.info("User ratings pages: " + userInfo.getPages());

            Pattern votesPattern = Pattern.compile("<div class=\"number\">\n  (\\d+)\n </div>");
            Matcher votesMatcher = votesPattern.matcher(ratingsPage.body().getElementsByClass("active-tab").toString());
            if (votesMatcher.find()) {
                userInfo.setVotes(Integer.valueOf(votesMatcher.group(1)));
            }
            else {
                throw new RuntimeException("Error obtaining user votes");
            }
            LOGGER.info("User total votes: " + userInfo.getVotes());

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error obtaining ratings", e);
        }
    }

    public List<MovieInfoDTO> fillMoviesInfo(UserInfoDTO userInfo) {
        List<MovieInfoDTO> movies = new ArrayList<>();
        try {
            for (int i=1; i <= userInfo.getPages(); i++) {
                String url = FAUtils.URLS.RATINGS.getUrl() + userInfo.getUserId() + FAUtils.URLS.PAGE_PREFIX.getUrl() + i;
                Document ratingsPage = Jsoup.connect(url).cookies(userInfo.getCookies()).get();

                Elements movieDateElements = ratingsPage.body().getElementsByClass("user-ratings-wrapper");
                for (Element movieDateElement : movieDateElements) {
                    Elements dateElement = movieDateElement.getElementsByClass("user-ratings-header");
                    String date = dateElement.get(0).childNode(0).outerHtml().substring(10);

                    Elements movieElements = movieDateElement.getElementsByClass("user-ratings-movie");
                    for (Element movieElement : movieElements) {
                        Elements idElement = movieElement.getElementsByClass("movie-card");
                        String id = idElement.get(0).attr("data-movie-id");

                        Elements titleElement = movieElement.getElementsByClass("mc-title");
                        String title = titleElement.get(0).getElementsByTag("a").get(0).childNode(0).outerHtml();
                        String year = titleElement.get(0).getAllElements().get(0).childNode(1).outerHtml().trim().substring(1, 5);

                        Elements ratingElement = movieElement.getElementsByClass("user-ratings-movie-rating");
                        String rating = ratingElement.get(0).getElementsByClass("ur-mr-rat").get(0).childNode(0).outerHtml().substring(1);

                        MovieInfoDTO movieInfo = new MovieInfoDTO();
                        movieInfo.setId(id);
                        movieInfo.setTitle(title);
                        movieInfo.setYear(year);
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
