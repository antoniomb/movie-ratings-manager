package es.antoniomb.dto;

import es.antoniomb.utils.ValueComparator;

import java.util.*;

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

    private Map<String, List<Movie>> directors;
    private Map<String, List<Movie>> actors;
    Map<String, String> countries;
    Map<String, String> years;
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

    public Map<String, String> getCountries() {
        return countries;
    }

    public void setCountries(Map<String, String> countries) {
        this.countries = countries;
    }

    public Map<String, String> getYears() {
        return years;
    }

    public void setYears(Map<String, String> years) {
        this.years = years;
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
