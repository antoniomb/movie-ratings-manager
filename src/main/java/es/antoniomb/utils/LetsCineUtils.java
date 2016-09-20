package es.antoniomb.utils;

/**
 * Created by amiranda on 21/9/16.
 */
public class LetsCineUtils {

    public enum URLS {
        HOME("http://www.letscine.com"),
        LOGIN_POST("https://www.letscine.com/action/login");

        String url;

        URLS(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

}
