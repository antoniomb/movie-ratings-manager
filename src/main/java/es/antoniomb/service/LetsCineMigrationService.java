package es.antoniomb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.antoniomb.dto.MigrationInput;
import es.antoniomb.dto.MovieInfo;
import es.antoniomb.dto.UserInfo;
import es.antoniomb.utils.LetsCineUtils;
import es.antoniomb.utils.MigrationUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by amiranda on 18/9/16.
 */
@Service
public class LetsCineMigrationService implements IMigrationService {

    private static Logger LOGGER = Logger.getLogger(LetsCineMigrationService.class.getName());

    @Override
    public List<MovieInfo> getRatings(MigrationInput input) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public Integer setRatings(MigrationInput input, List<MovieInfo> moviesInfo) {

        UserInfo userInfo = login(input.getToUsername(), input.getToPassword());

        return fillMoviesInfo(userInfo, moviesInfo);
    }

    public UserInfo login(String username, String password) {
        Map<String,String> cookies = new HashMap<>();
        try {
            MigrationUtils.disableSSLCertCheck();

            if (password == null) {
                //Dummy cookie
                cookies.put("Elgg",username);
            }
            else {
                LOGGER.info("User name: " + username);

                Document loginPage = Jsoup.connect(LetsCineUtils.URLS.LOGIN.getUrl()).get();
                Element form = loginPage.getElementsByClass("elgg-form-login").get(0);
                String elggTs = form.getElementsByAttributeValue("name", "__elgg_ts").val();
                String elggToken = form.getElementsByAttributeValue("name", "__elgg_token").val();

                //Request for login
                Connection.Response login = Jsoup.connect(LetsCineUtils.URLS.LOGIN_POST.getUrl())
                        .data("returntoreferer", "true")
                        .data("username", username)
                        .data("password", password)
                        .data("__elgg_ts", elggTs)
                        .data("__elgg_token", elggToken)
                        .header("Connection", "keep-alive")
                        .header("Origin", "https://www.letscine.com")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Host", "www.letscine.com")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .referrer("https://www.letscine.com/login")
                        .method(Connection.Method.POST)
                        .execute();

                System.out.println(login.parse().body());

                if (login.cookie(LetsCineUtils.SESSION_COOKIE) == null) {
                    throw new RuntimeException("Login error");
                }

                cookies = login.cookies();
            }
        }
        catch(IOException e){
            LOGGER.log(Level.WARNING, "Error logging user " + username, e);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setCookies(cookies);

        return userInfo;
    }

    public Integer fillMoviesInfo(UserInfo userInfo, List<MovieInfo> moviesInfo) {

        List<String> unmatchedMovies = new ArrayList<>();

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
                    if (jsonNode.path("release_date").asText().contains(movieInfo.getYear())) {
                        if (id == null) {
                            id = jsonNode.path("id").asText();
                            type = jsonNode.path("media_type").asText();
                        }
                        else {
                            id = null;
                            LOGGER.warning("Multiple movies matched in same year for title: "+movieInfo.getTitle());
                        }
                        if (jsonNode.path("original_title").asText().equals(movieInfo.getTitle())) {
                            break;
                        }
                    }
                }

                if (id == null) {
                    String movieInfoStr = movieInfo.getTitle() + " - year: " + movieInfo.getYear() +
                            " - viewDate: " + movieInfo.getDate();
                    unmatchedMovies.add(movieInfoStr);
                    LOGGER.warning("Movie not found: " + movieInfoStr);
                }
                else {

                    Connection.Response loadResponse = Jsoup.connect(LetsCineUtils.URLS.LOAD_MOVIE.getUrl() + id)
                            .cookies(userInfo.getCookies())
                            .method(Connection.Method.POST)
                            .execute();

                    Document moviePage = Jsoup.connect(loadResponse.url().toString()).cookies(userInfo.getCookies()).get();

                    Element form = moviePage.getElementsByClass("elgg-form-comment-save").get(0);
                    String elggTs = form.getElementsByAttributeValue("name","__elgg_ts").val();
                    String elggToken = form.getElementsByAttributeValue("name", "__elgg_token").val();
                    String movieUrl = loadResponse.url().getPath();
                    String movieId = movieUrl.split("/")[2];
                    String watched_id = moviePage.getElementsByAttribute("watched-id").get(0).attributes().get("watched-id");

                    LOGGER.info("Load movie title: "+movieInfo.getTitle()+" with id: "+movieId+ " and watchedId: "+watched_id);

                    if (watched_id == null || watched_id.isEmpty()) {
                        Connection.Response addResponse = Jsoup.connect(LetsCineUtils.URLS.ADD_MOVIE.getUrl())
                                .data("watched_date", movieInfo.getDate())
                                .data("id", id)
                                .data("movie_id", movieId)
                                .data("type", type)
                                .data("__elgg_ts", elggTs)
                                .data("__elgg_token", elggToken)
                                .header("X-Requested-With", "XMLHttpRequest")
                                .method(Connection.Method.POST)
                                .cookies(userInfo.getCookies())
                                .ignoreContentType(true)
                                .execute();

                        LOGGER.info("AddMovie - title: "+movieInfo.getTitle()+": "+addResponse.body());
                    }
                    else {
                        Connection.Response changeDateResponse = Jsoup.connect(LetsCineUtils.URLS.CHANGE_DATE_MOVIE.getUrl())
                                .data("watched_date", movieInfo.getDate())
                                .data("watched_id", watched_id)
                                .data("__elgg_ts", elggTs)
                                .data("__elgg_token", elggToken)
                                .header("X-Requested-With", "XMLHttpRequest")
                                .method(Connection.Method.POST)
                                .cookies(userInfo.getCookies())
                                .ignoreContentType(true)
                                .execute();

                        LOGGER.info("UpdateDate - title: "+movieInfo.getTitle()+": "+changeDateResponse.body());
                    }

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

                    LOGGER.info("RateMovie - title: "+movieInfo.getTitle()+": "+rateResponse.body());

                    addMovie++;
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error settings ratings for movie "+movieInfo.getTitle(), e);
            }
        }

        LOGGER.info("Migration succesfull! "+(moviesInfo.size()-unmatchedMovies.size()) + " movies migrated!");

        if (!unmatchedMovies.isEmpty()) {
            LOGGER.warning("There are "+unmatchedMovies.size()+ " that not matches from source to target");
            for (String unmatchedMovie : unmatchedMovies) {
                LOGGER.warning("Title: "+unmatchedMovie);
            }
        }

        return addMovie;
    }
}
