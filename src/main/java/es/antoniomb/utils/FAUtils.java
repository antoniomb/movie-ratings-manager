package es.antoniomb.utils;

/**
 * Created by amiranda on 18/9/16.
 */
public class FAUtils {

    public enum URLS {
        HOME("http://www.filmaffinity.com/en/main.php"),
        FILM("http://www.filmaffinity.com/en/film"),
        FILM_SUFIX(".html"),
        LOGIN("http://www.filmaffinity.com/en/login.php"),
        LOGIN_POST("https://filmaffinity.com/en/account.ajax.php?action=login"),
        VOTES("http://www.filmaffinity.com/en/myvotes.php"),
        VOTES_PREFIX("http://www.filmaffinity.com/en/myvotes.php?p="),
        VOTES_SUFIX("&orderby="),
        VOTES_ID("http://www.filmaffinity.com/en/userratings.php?user_id="),
        VOTES_PAGE_SUFIX("&p=");

        String url;

        URLS(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
