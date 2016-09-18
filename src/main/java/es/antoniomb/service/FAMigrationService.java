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

    public String login(String username, String password) {
        String userID = null;

        try {
            MigrationUtils.disableSSLCertCheck();

            Connection.Response login = Jsoup.connect(FAUtils.URLS.LOGIN_POST.getUrl())
                    .data("postback", "1")
                    .data("rp", "")
                    .data("username", username)
                    .data("password", password)
                    .method(Connection.Method.POST)
                    .execute();

            Map<String, String> cookies = login.cookies();

            Document votes = Jsoup.connect(FAUtils.URLS.VOTES.getUrl()).cookies(cookies).get();

            Pattern userIdPattern = Pattern.compile("user_id=(\\d+)");

            Matcher matcher = userIdPattern.matcher(votes.body().getElementById("user-nick").toString());
            if (matcher.find()) {
                userID = matcher.group(1);
            }
            else {
                LOGGER.log(Level.WARNING, "Error obtaining userId for username "+username);
            }

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error logging user "+username, e);
        }

        if (userID == null) {
            throw new RuntimeException("Login error");
        }

        return userID;
    }

}
