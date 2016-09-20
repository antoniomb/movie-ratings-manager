package es.antoniomb.service;

import es.antoniomb.dto.MovieInfo;
import es.antoniomb.dto.UserInfo;
import es.antoniomb.utils.FAUtils;
import es.antoniomb.utils.LetsCineUtils;
import es.antoniomb.utils.MigrationUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by amiranda on 18/9/16.
 */
@Service
public class LetsCineMigrationService implements IMigrationService {

    private static Logger LOGGER = Logger.getLogger(LetsCineMigrationService.class.getName());

    @Override
    public List<MovieInfo> getRatings(String username, String password) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public Integer setRatings(String username, String password, List<MovieInfo> moviesInfo) {
//        throw new RuntimeException("Not implemented!");
        return 0;
    }

    public UserInfo login(String username, String password) {
        Connection.Response login = null;
        try {
            MigrationUtils.disableSSLCertCheck();

            LOGGER.info("User name: "+username);

            //Request for login
            login = Jsoup.connect(LetsCineUtils.URLS.LOGIN_POST.getUrl())
                    .data("returntoreferer", "true")
                    .data("username", username)
                    .data("password", password)
                    .method(Connection.Method.POST)
                    .execute();
        }
        catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error logging user "+username, e);
        }

        if (login == null || login.cookie("Elgg") == null) {
            throw new RuntimeException("Login error");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setCookies(login.cookies());

        return userInfo;
    }
}
