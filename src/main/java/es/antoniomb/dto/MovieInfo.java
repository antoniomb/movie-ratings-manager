package es.antoniomb.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by amiranda on 18/09/16.
 */
public class MovieInfo {

    public String id;
    public String title;
    public String year;
    public String rate;
    public String date;
    public String country;
    public String director;
    public List<String> actors;
    private boolean shortMovie;
    private boolean TVSerie;

    public MovieInfo() {
    }

    public MovieInfo(String title, String year) {
        this.title = title;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public void setShortMovie(boolean shortMovie) {
        this.shortMovie = shortMovie;
    }

    public boolean isShortMovie() {
        return shortMovie;
    }

    public void setTVSerie(boolean TVSerie) {
        this.TVSerie = TVSerie;
    }

    public boolean isTVSerie() {
        return TVSerie;
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", rate='" + rate + '\'' +
                ", date='" + date + '\'' +
                ", country='" + country + '\'' +
                ", director='" + director + '\'' +
                ", actors=" + StringUtils.join(actors.toArray(), ",") +
                '}';
    }

}
