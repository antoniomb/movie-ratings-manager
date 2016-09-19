package es.antoniomb.service;

import es.antoniomb.utils.FAUtils;
import es.antoniomb.utils.MigrationUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
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

        Map<String, String> cookies = login(username, password);

        getRatings(cookies);


    }

    public Map<String, String> login(String username, String password) {
        Connection.Response login = null;
        try {
            MigrationUtils.disableSSLCertCheck();

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

        return login.cookies();
    }

    public void getRatings(Map<String, String> cookies) {
        try {
            String userID = null;
            Document votesPage = Jsoup.connect(FAUtils.URLS.VOTES.getUrl()).cookies(cookies).get();
            Pattern userIdPattern = Pattern.compile("user_id=(\\d+)");
            Matcher userIdMatcher = userIdPattern.matcher(votesPage.body().getElementById("user-nick").toString());
            if (userIdMatcher.find()) {
                userID = userIdMatcher.group(1);
            }
            else {
                throw new RuntimeException("Error obtaining userID");
            }
            LOGGER.info("User id: "+userID);

            String pages = null;
            Document ratingsPage = Jsoup.connect(FAUtils.URLS.RATINGS.getUrl()+userID).cookies(cookies).get();
            Pattern pagePattern = Pattern.compile("Page \n  <b>1</b> of \n  <b>(\\d+)</b>");
            Matcher pageMatcher = pagePattern.matcher(ratingsPage.body().getElementsByClass("user-ratings-info-top").toString());
            if (pageMatcher.find()) {
                pages = pageMatcher.group(1);
            }
            else {
                throw new RuntimeException("Error obtaining rating pages");
            }
            LOGGER.info("User ratings pages: "+pages);

            String votes = null;
            Pattern votesPattern = Pattern.compile("<div class=\"number\">\n  (\\d+)\n </div>");
            Matcher votesMatcher = votesPattern.matcher(ratingsPage.body().getElementsByClass("active-tab").toString());
            if (votesMatcher.find()) {
                votes = votesMatcher.group(1);
            }
            else {
                throw new RuntimeException("Error obtaining user votes");
            }
            LOGGER.info("User total votes: "+votes);

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error obtaining ratings", e);
        }

    }

}
