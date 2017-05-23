package es.antoniomb.utils;

import es.antoniomb.dto.MigrationOutputAnalytics;
import es.antoniomb.dto.MovieInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by amiranda on 23/5/17.
 */
public class AnalyticsUtils {

    public static MigrationOutputAnalytics calculateAnalytics(List<MovieInfo> moviesInfo) {
        MigrationOutputAnalytics analytics = new MigrationOutputAnalytics();
        analytics.setTopDirector(AnalyticsUtils.calculateTopDirectors(moviesInfo));
        analytics.setTopActor(AnalyticsUtils.calculateTopActors(moviesInfo));
        analytics.setTopCountry(AnalyticsUtils.calculateTopCountry(moviesInfo));
        analytics.setTopYear(AnalyticsUtils.calculateTopYear(moviesInfo));
        analytics.setBestMovies(AnalyticsUtils.calculateBestMovies(moviesInfo));
        analytics.setWorstMovies(AnalyticsUtils.calculateWorstMovies(moviesInfo));
        analytics.setTopJoke(AnalyticsUtils.calculateJokeTop(moviesInfo));
        return analytics;
    }

    public static String calculateTopDirectors(List<MovieInfo> moviesInfo) {
        Map<String, Integer> directors = new HashMap<>();
        for (MovieInfo movieInfo : moviesInfo) {
            if (directors.containsKey(movieInfo.getDirector())) {
                Integer count = directors.get(movieInfo.getDirector());
                directors.put(movieInfo.getDirector(), ++count);
            }
            else {
                directors.put(movieInfo.getDirector(), 1);
            }
        }
        return calculateTop(directors);
    }

    public static String calculateTopActors(List<MovieInfo> moviesInfo) {
        Map<String, Integer> actors = new HashMap<>();
        for (MovieInfo movieInfo : moviesInfo) {
            for (String actor : movieInfo.getActors()) {
                if (actors.containsKey(actor)) {
                    Integer count = actors.get(actor);
                    actors.put(actor, ++count);
                }
                else {
                    actors.put(actor, 1);
                }
            }
        }
        return calculateTop(actors);
    }

    public static String calculateTopCountry(List<MovieInfo> moviesInfo) {
        Map<String, Integer> country = new HashMap<>();
        for (MovieInfo movieInfo : moviesInfo) {
            if (country.containsKey(movieInfo.getCountry())) {
                Integer count = country.get(movieInfo.getCountry());
                country.put(movieInfo.getCountry(), ++count);
            }
            else {
                country.put(movieInfo.getCountry(), 1);
            }
        }
        return calculateTop(country);
    }

    public static String calculateTopYear(List<MovieInfo> moviesInfo) {
        Map<String, Integer> year = new HashMap<>();
        for (MovieInfo movieInfo : moviesInfo) {
            if (year.containsKey(movieInfo.getYear())) {
                Integer count = year.get(movieInfo.getYear());
                year.put(movieInfo.getYear(), ++count);
            }
            else {
                year.put(movieInfo.getYear(), 1);
            }
        }
        return calculateTop(year);
    }

    public static String calculateBestMovies(List<MovieInfo> moviesInfo) {
        List<String> topMovies = new ArrayList<>();
        for (MovieInfo movieInfo : moviesInfo) {
            if (movieInfo.getRate().equals("10")) {
                topMovies.add(movieInfo.getTitle());
            }
        }
        return StringUtils.join(topMovies.toArray(), ", ");
    }

    public static String calculateWorstMovies(List<MovieInfo> moviesInfo) {
        List<String> worstMovies = new ArrayList<>();
        for (MovieInfo movieInfo : moviesInfo) {
            if (movieInfo.getRate().equals("1")) {
                worstMovies.add(movieInfo.getTitle());
            }
        }
        return StringUtils.join(worstMovies.toArray(), ", ");
    }

    public static String calculateJokeTop(List<MovieInfo> moviesInfo) {
        List<String> joke = new ArrayList<>();
        for (MovieInfo movieInfo : moviesInfo) {
            for (String actor : movieInfo.getActors()) {
                if (actor.equals("Nicolas Cage")) {
                    joke.add(movieInfo.getTitle());
                    break;
                }
            }
        }
        if (joke.isEmpty()) {
            return "none... seriously?";
        }
        return StringUtils.join(joke.toArray(), ", ");
    }

    private static String calculateTop(Map<String, Integer> itemMap) {
        ValueComparator bvc = new ValueComparator(itemMap);
        TreeMap<String, Integer> sortedMap = new TreeMap<>(bvc);
        sortedMap.putAll(itemMap);
        String top = "";
        int i = 0;
        for (Map.Entry<String,Integer> item : sortedMap.entrySet()) {
            if (i++ == 24 || i == sortedMap.entrySet().size()) {
                top+=item.getKey()+"("+item.getValue()+")";
                break;
            }
            top+=item.getKey()+"("+item.getValue()+")"+", ";
        }
        return top;
    }

}
