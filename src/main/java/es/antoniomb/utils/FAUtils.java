package es.antoniomb.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by amiranda on 18/9/16.
 */
public class FAUtils {

    public static String SESSION_COOKIE = "FSID";

    public static SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMM", Locale.ENGLISH);

    public enum URLS {
        HOME("http://www.filmaffinity.com/en/main.php"),
        FILM("http://www.filmaffinity.com/en/film"),
        FILM_SUFIX(".html"),
        LOGIN("http://www.filmaffinity.com/en/login.php"),
        LOGIN_POST("http://filmaffinity.com/en/account.ajax.php?action=login"),
        VOTES("http://www.filmaffinity.com/en/myvotes.php"),
        VOTES_PREFIX("http://www.filmaffinity.com/en/myvotes.php?p="),
        VOTES_SUFIX("&orderby="),
        RATINGS("http://www.filmaffinity.com/en/userratings.php?user_id="),
        PAGE_PREFIX("&p=");

        String url;

        URLS(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
