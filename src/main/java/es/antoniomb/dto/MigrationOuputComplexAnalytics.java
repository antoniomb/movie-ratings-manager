package es.antoniomb.dto;

import es.antoniomb.utils.AnalyticsComplexUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MigrationOuputComplexAnalytics {

    public static class Movie {
        private String title;
        private String year;
        private String rating;
        private Movie(String title, String year, String rating) {
            this.title = title;
            this.year = year;
            this.rating = rating;
        }
        public static Movie of(String title, String year, String rating) {
            return new Movie(title, year, rating);
        }
        public String getTitle() {
            return title;
        }
        public String getYear() {
            return year;
        }
        public String getRating() {
            return rating;
        }
    }

    public static class TotalAvg {
        private int hits;
        private int totalRating;
        private List<Movie> movies = new ArrayList<>();
        public TotalAvg(String rating, Movie movie) {
            this.hits = 1;
            this.totalRating = Integer.parseInt(rating);
            this.movies.add(movie);
        }
        public int getHits(){
            return hits;
        }
        public void addRating(String rating) {
            ++this.hits;
            this.totalRating+=Integer.parseInt(rating);
        }
        public List<Movie> getMovies() {
            return movies;
        }
        public void setMovies(List<Movie> movies) {
            this.movies = movies;
        }

        public String avg() {
            return AnalyticsComplexUtils.FORMATTER.format(((totalRating * 100.0f) / hits) / 100);
        }
    }

    private Map<String, List<Movie>> directors;
    private Map<String, List<Movie>> actors;
    Map<String, List<Movie>> countries;
    Map<String, List<Movie>> years;
    Map<String, List<Movie>> yearsByRatingDate;
    Map<String, String> ratingDist;
    Map<String, String> bestMovies;
    Map<String, String> worstMovies;
    Map<String, String> jokeActor;

    public Map<String, List<Movie>> getDirectors() {
        return directors;
    }

    public void setDirectors(Map<String, List<Movie>> directors) {
        this.directors = directors;
    }

    public Map<String, List<Movie>> getActors() {
        return actors;
    }

    public void setActors(Map<String, List<Movie>> actors) {
        this.actors = actors;
    }

    public Map<String, List<Movie>> getCountries() {
        return countries;
    }

    public void setCountries(Map<String, List<Movie>> countries) {
        this.countries = countries;
    }

    public Map<String, List<Movie>> getYears() {
        return years;
    }

    public void setYears(Map<String, List<Movie>> years) {
        this.years = years;
    }

    public Map<String, List<Movie>> getYearsByRatingDate() {
        return yearsByRatingDate;
    }

    public void setYearsByRatingDate(Map<String, List<Movie>> yearsByRatingDate) {
        this.yearsByRatingDate = yearsByRatingDate;
    }

    public Map<String, String> getRatingDist() {
        return ratingDist;
    }

    public void setRatingDist(Map<String, String> ratingDist) {
        this.ratingDist = ratingDist;
    }

    public Map<String, String> getBestMovies() {
        return bestMovies;
    }

    public void setBestMovies(Map<String, String> bestMovies) {
        this.bestMovies = bestMovies;
    }

    public Map<String, String> getWorstMovies() {
        return worstMovies;
    }

    public void setWorstMovies(Map<String, String> worstMovies) {
        this.worstMovies = worstMovies;
    }

    public Map<String, String> getJokeActor() {
        return jokeActor;
    }

    public void setJokeActor(Map<String, String> jokeActor) {
        this.jokeActor = jokeActor;
    }
}
