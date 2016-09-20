package es.antoniomb.dto;

import java.util.Map;

/**
 * Created by amiranda on 19/9/16.
 */
public class UserInfo {

    private String userId;
    private Integer pages;
    private Integer votes;
    private Map<String, String> cookies;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
