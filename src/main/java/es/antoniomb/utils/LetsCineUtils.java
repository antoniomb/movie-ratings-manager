package es.antoniomb.utils;

/**
 * Created by amiranda on 21/9/16.
 */
public class LetsCineUtils {

    public static String SESSION_COOKIE = "Elgg";

    public enum URLS {
        HOME("http://www.letscine.com"),
        LOGIN_POST("https://www.letscine.com/action/login"),
        SEARCH("https://www.letscine.com/search.php?lang=es&type=multi&term="),
        LOAD_MOVIE("https://www.letscine.com/movies/load/"),
        ADD_MOVIE("https://www.letscine.com/action/history/addmovie"),
        RATE_MOVIE("https://www.letscine.com/action/movie/rate");

        String url;

        URLS(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

}
