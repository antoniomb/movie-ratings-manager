package es.antoniomb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.antoniomb.dto.MovieInfo;
import es.antoniomb.dto.UserInfo;
import es.antoniomb.utils.FAUtils;
import es.antoniomb.utils.LetsCineUtils;
import es.antoniomb.utils.MigrationUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

        UserInfo userInfo = login(username, password);

        return fillMoviesInfo(userInfo, moviesInfo);
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

        if (login == null || login.cookie(LetsCineUtils.SESSION_COOKIE) == null) {
            throw new RuntimeException("Login error");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setCookies(login.cookies());

        return userInfo;
    }

    public Integer fillMoviesInfo(UserInfo userInfo, List<MovieInfo> moviesInfo) {
        int addMovie = 0;
        for (MovieInfo movieInfo : moviesInfo) {
            try {
                String url = LetsCineUtils.URLS.SEARCH.getUrl() + movieInfo.getTitle().replaceAll(" ", "+");
                String json = Jsoup.connect(url).ignoreContentType(true).execute().body();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode searchResult = mapper.readTree(json);

                String id = null;
                String type = null;
                for (JsonNode jsonNode : searchResult) {
                    if (jsonNode.path("original_title").asText().equals(movieInfo.getTitle())) {
                        if (jsonNode.path("release_date").asText().contains(movieInfo.getYear())) {
                            id = jsonNode.path("id").asText();
                            type = jsonNode.path("media_type").asText();
                            break;
                        }
                    }
                }

                if (id == null) {
                    LOGGER.warning("Movie not found for title: "+movieInfo.getTitle());
                }
                else {

                    Connection.Response loadResponse = Jsoup.connect(LetsCineUtils.URLS.LOAD_MOVIE.getUrl() + id)
                            .cookies(userInfo.getCookies())
                            .method(Connection.Method.POST)
                            .execute();

                    Element form = loadResponse.parse().body().getElementsByClass("elgg-form-comment-save").get(0);
                    String elggTs = form.getElementsByAttributeValue("name","__elgg_ts").val();;
                    String elggToken = form.getElementsByAttributeValue("name", "__elgg_token").val();
                    String movieUrl = loadResponse.url().getPath();
                    String movieId = movieUrl.split("/")[2];

                    LOGGER.info("Load movie "+movieInfo.getTitle()+" with id "+movieId);

                    Connection.Response addResponse = Jsoup.connect(LetsCineUtils.URLS.ADD_MOVIE.getUrl())
                            .data("watched_date", movieInfo.getDate())
                            .data("id", id)
                            .data("movie_id", movieId)
                            .data("type", type)
                            .data("__elgg_token", elggToken)
                            .data("__elgg_ts", elggTs)
                            .header("X-Requested-With", "XMLHttpRequest")
                            .method(Connection.Method.POST)
                            .cookies(userInfo.getCookies())
                            .ignoreContentType(true)
                            .execute();

                    LOGGER.info("AddMovie: "+addResponse.body());

                    Connection.Response rateResponse = Jsoup.connect(LetsCineUtils.URLS.RATE_MOVIE.getUrl())
                            .data("movie_id", movieId)
                            .data("rating", movieInfo.getRate())
                            .data("__elgg_token", elggToken)
                            .data("__elgg_ts", elggTs)
                            .header("X-Requested-With", "XMLHttpRequest")
                            .method(Connection.Method.POST)
                            .cookies(userInfo.getCookies())
                            .ignoreContentType(true)
                            .execute();

                    LOGGER.info("RateMovie: "+rateResponse.body());

                    addMovie++;
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error settings ratings for movie "+movieInfo.getTitle(), e);
            }
        }


        return addMovie;
    }
}
