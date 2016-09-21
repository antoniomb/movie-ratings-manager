package es.antoniomb.login;

import es.antoniomb.dto.MovieInfo;
import es.antoniomb.dto.UserInfo;
import es.antoniomb.service.LetsCineMigrationService;
import es.antoniomb.utils.LetsCineUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amiranda on 21/9/16.
 */
public class LetsCineTest {

    private LetsCineMigrationService letsCineMigrationService = new LetsCineMigrationService();

    @Test
    public void login(){
        UserInfo userInfo = letsCineMigrationService.login("antoniomiranda", "Abc123456");

        Assert.assertTrue(userInfo.getCookies().containsKey(LetsCineUtils.SESSION_COOKIE));
    }

    @Test
    public void fillMoviesInfo() {
        UserInfo userInfo = letsCineMigrationService.login("antoniomiranda", "Abc123456");

        List<MovieInfo> movies = new ArrayList<>();
        MovieInfo movie = new MovieInfo("The Godfather", "1972");
        movie.setDate("2014-12-12");
        movie.setRate("10");
        movies.add(movie);

        Integer rated = letsCineMigrationService.fillMoviesInfo(userInfo, movies);

        Assert.assertEquals(rated.longValue(), 1L);
    }
}
