package es.antoniomb.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;

/**
 * Created by amiranda on 20/9/16.
 */
public class MigrationOutput implements Serializable {

    private Boolean sourceStatus;
    private Boolean targetStatus;
    private Integer moviesReaded;
    private Integer moviesWrited;
    private Double ratingAvg;
    private String topDirector;
    private String topActor;
    private String topCountry;
    private String topYear;
    private String bestMovies;
    private String worstMovies;
    private String csv;

    public MigrationOutput() {
    }

    public Boolean getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(Boolean sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public Boolean getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Boolean targetStatus) {
        this.targetStatus = targetStatus;
    }

    public Integer getMoviesReaded() {
        return moviesReaded;
    }

    public void setMoviesReaded(Integer moviesReaded) {
        this.moviesReaded = moviesReaded;
    }

    public Integer getMoviesWrited() {
        return moviesWrited;
    }

    public void setMoviesWrited(Integer moviesWrited) {
        this.moviesWrited = moviesWrited;
    }

    public Double getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(Double ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

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

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }
}
