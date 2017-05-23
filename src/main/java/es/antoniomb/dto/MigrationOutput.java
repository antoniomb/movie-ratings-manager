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
    private String ratingAvg;
    private String csv;
    private MigrationOutputAnalytics analytics;

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

    public String getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(String ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public MigrationOutputAnalytics getAnalytics() {
        return analytics;
    }

    public void setAnalytics(MigrationOutputAnalytics analytics) {
        this.analytics = analytics;
    }
}
