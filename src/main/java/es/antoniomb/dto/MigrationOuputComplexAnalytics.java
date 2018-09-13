package es.antoniomb.dto;

import es.antoniomb.utils.AnalyticsComplexUtils;

import java.util.*;

public class MigrationOuputComplexAnalytics {

    private Map<String, List<Movie>> directors;
    private Map<String, List<Movie>> actors;
    Map<String, List<Movie>> countries;
    Map<String, List<Movie>> moviesYears;
    Collection<Integer> yearsChartKeys;
    Collection<Integer> yearsChartValues;
    Map<String, List<Movie>> yearsByRatingDate;
    Collection<Integer> yearsByRatingDateChartKeys;
    Collection<Integer> yearsByRatingDateChartValues;
    Map<String, String> ratingDist;
    List<Object[]> ratingChart;
    Map<String, Movie> bestMovies;
    Map<String, Movie> worstMovies;
    Map<String, Movie> jokeActor;
    Map<String, Movie> shortMovies;
    Map<String, Movie> documentaries;
    Map<String, Movie> tvSeries;
    private int totalMovies;
    private int totalTvSeries;
    private int totalDocumentaries;
    private int totalShortMovies;

    public static class Movie {
        private String faId;
        private String title;
        private String year;
        private String rating;

        private Movie(String faId, String title, String year, String rating) {
            this.faId = faId;
            this.title = title;
            this.year = year;
            this.rating = rating;
        }

        public static Movie of(String title, String year, String rating) {
            return new Movie(null, title, year, rating);
        }

        public static Movie of(String faId, String title, String year, String rating) {
            return new Movie(faId, title, year, rating);
        }

        public String getFaId() {
            return faId;
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

        public int getHits() {
            return hits;
        }

        public void addRating(String rating) {
            ++this.hits;
            this.totalRating += Integer.parseInt(rating);
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

    public Map<String, List<Movie>> getMoviesYears() {
        return moviesYears;
    }

    public void setMoviesYears(Map<String, List<Movie>> moviesYears) {
        this.moviesYears = moviesYears;
    }

    public Collection<Integer> getYearsChartKeys() {
        return yearsChartKeys;
    }

    public Collection<Integer> getYearsChartValues() {
        return yearsChartValues;
    }

    public void setYearsChart(Map<Integer, Integer> yearsChart) {
        this.yearsChartKeys = yearsChart.keySet();
        this.yearsChartValues = yearsChart.values();
    }

    public Map<String, List<Movie>> getYearsByRatingDate() {
        return yearsByRatingDate;
    }

    public void setYearsByRatingDate(Map<String, List<Movie>> yearsByRatingDate) {
        this.yearsByRatingDate = yearsByRatingDate;
    }

    public Collection<Integer> getYearsByRatingDateChartKeys() {
        return yearsByRatingDateChartKeys;
    }

    public Collection<Integer> getYearsByRatingDateChartValues() {
        return yearsByRatingDateChartValues;
    }

    public void setYearsByRatingDateChart(Map<Integer, Integer> yearsChart) {
        this.yearsByRatingDateChartKeys = yearsChart.keySet();
        this.yearsByRatingDateChartValues = yearsChart.values();
    }

    public Map<String, String> getRatingDist() {
        return ratingDist;
    }

    public void setRatingDist(Map<String, String> ratingDist) {
        this.ratingDist = ratingDist;
    }

    public List<Object[]> getRatingChart() {
        return ratingChart;
    }

    public void setRatingChart(Map<Integer, Integer> ratingChart) {
        this.ratingChart = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : ratingChart.entrySet()) {
            this.ratingChart.add(Arrays.asList(String.valueOf(entry.getKey()), entry.getValue()).toArray());
        }
    }

    public Map<String, Movie> getBestMovies() {
        return bestMovies;
    }

    public void setBestMovies(Map<String, Movie> bestMovies) {
        this.bestMovies = bestMovies;
    }

    public Map<String, Movie> getWorstMovies() {
        return worstMovies;
    }

    public void setWorstMovies(Map<String, Movie> worstMovies) {
        this.worstMovies = worstMovies;
    }

    public Map<String, Movie> getJokeActor() {
        return jokeActor;
    }

    public void setJokeActor(Map<String, Movie> jokeActor) {
        this.jokeActor = jokeActor;
    }

    public Map<String, Movie> getShortMovies() {
        return shortMovies;
    }

    public void setShortMovies(Map<String, Movie> shortMovies) {
        this.shortMovies = shortMovies;
    }

    public void setDocumentaries(Map<String, Movie> documentaries) {
        this.documentaries = documentaries;
    }

    public Map<String, Movie> getDocumentaries() {
        return documentaries;
    }


    public void setTvSeries(Map<String, Movie> tvSeries) {
        this.tvSeries = tvSeries;
    }

    public Map<String, Movie> getTvSeries() {
        return tvSeries;
    }

    public void setTotalMovies(int totalMovies) {
        this.totalMovies = totalMovies;
    }

    public int getTotalMovies() {
        return totalMovies;
    }

    public void setTotalShortMovies(int totalShortMovies) {
        this.totalShortMovies = totalShortMovies;
    }

    public int getTotalShortMovies() {
        return totalShortMovies;
    }

    public void setTotalDocumentaries(int totalDocumentaries) {
        this.totalDocumentaries = totalDocumentaries;
    }

    public int getTotalDocumentaries() {
        return totalDocumentaries;
    }

    public void setTotalTvSeries(int totalTvSeries) {
        this.totalTvSeries = totalTvSeries;
    }

    public int getTotalTvSeries() {
        return totalTvSeries;
    }
}
