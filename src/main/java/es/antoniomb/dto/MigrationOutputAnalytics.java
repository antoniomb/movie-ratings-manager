package es.antoniomb.dto;

/**
 * Created by amiranda on 23/5/17.
 */
public class MigrationOutputAnalytics {

    private String topDirector;
    private String topActor;
    private String topCountry;
    private String topYear;
    private String bestMovies;
    private String worstMovies;
    private String topJoke;

    public String getTopActor() {
        return topActor;
    }

    public void setTopActor(String topActor) {
        this.topActor = topActor;
    }

    public String getTopDirector() {
        return topDirector;
    }

    public void setTopDirector(String topDirector) {
        this.topDirector = topDirector;
    }

    public String getTopCountry() {
        return topCountry;
    }

    public void setTopCountry(String topCountry) {
        this.topCountry = topCountry;
    }

    public String getTopYear() {
        return topYear;
    }

    public void setTopYear(String topYear) {
        this.topYear = topYear;
    }

    public String getBestMovies() {
        return bestMovies;
    }

    public void setBestMovies(String bestMovies) {
        this.bestMovies = bestMovies;
    }

    public String getWorstMovies() {
        return worstMovies;
    }

    public void setWorstMovies(String worstMovies) {
        this.worstMovies = worstMovies;
    }

    public String getTopJoke() {
        return topJoke;
    }

    public void setTopJoke(String topJoke) {
        this.topJoke = topJoke;
    }
}
