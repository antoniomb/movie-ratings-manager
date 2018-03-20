package es.antoniomb.utils;

import es.antoniomb.dto.MigrationOuputComplexAnalytics;
import es.antoniomb.dto.MovieInfo;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsComplexUtils {

    private static final String JOKE_ACTOR = "Nicolas Cage";
    private static final int TOP_VALUES = 50;
    private static final int TOP_AVG_VALUES = 20;
    public static DecimalFormat FORMATTER = new DecimalFormat("0.00");

    public static MigrationOuputComplexAnalytics calculateAnalytics(List<MovieInfo> moviesInfo) {
        MigrationOuputComplexAnalytics analytics = new MigrationOuputComplexAnalytics();

        Map<String, List<MigrationOuputComplexAnalytics.Movie>> directors = new HashMap<>();
        Map<String, List<MigrationOuputComplexAnalytics.Movie>> actors = new HashMap<>();
        Map<String, MigrationOuputComplexAnalytics.TotalAvg> country = new HashMap<>();
        Map<String, MigrationOuputComplexAnalytics.TotalAvg> year = new HashMap<>();
        Map<String, MigrationOuputComplexAnalytics.TotalAvg> yearByRatingDate = new HashMap<>();
        Map<String, Integer> ratings = new HashMap<>();
        Map<String, Integer> topMovies = new HashMap<>();
        Map<String, Integer> worstMovies = new HashMap<>();
        Map<String, Integer> jokeActor = new HashMap<>();

        for (MovieInfo movieInfo : moviesInfo) {

            topDirector(directors, movieInfo);
            topActor(actors, jokeActor, movieInfo);
            topCountry(country, movieInfo);
            topYear(year, movieInfo);
            topYearByRatingDate(yearByRatingDate, movieInfo);
            ratingsDist(ratings, movieInfo);
            topMovies(topMovies, movieInfo);
            worstMovies(worstMovies, movieInfo);
        }
        analytics.setDirectors(calculateTop(directors));
        analytics.setActors(calculateTop(actors));
        analytics.setCountries(calculateTopAvg(sortMapMoviesByValue(country), false));
        analytics.setYears(calculateTopAvg(sortMapByKey(year), false));
        analytics.setYearsChart(prepareForChart(sortMapByKey(year)));
        analytics.setYearsByRatingDate(calculateTopAvg(sortMapByKey(yearByRatingDate), true));
        analytics.setYearsByRatingDateChart(prepareForChart(sortMapByKey(yearByRatingDate)));
        analytics.setRatingDist(calculateTop(ratings, true));
        analytics.setRatingChart(prepareForChart(ratings));
        analytics.setBestMovies(calculateTop(topMovies, false));
        analytics.setWorstMovies(calculateTop(worstMovies, false));
        analytics.setJokeActor(calculateTop(jokeActor, false));

        return analytics;
    }

    private static void worstMovies(Map<String, Integer> worstMovies, MovieInfo movieInfo) {
        if (movieInfo.getRate().equals("1")) {
            worstMovies.put(movieInfo.getTitle(), Integer.valueOf(movieInfo.getYear()));
        }
    }

    private static void topMovies(Map<String, Integer> topMovies, MovieInfo movieInfo) {
        if (movieInfo.getRate().equals("10")) {
            topMovies.put(movieInfo.getTitle(), Integer.valueOf(movieInfo.getYear()));
        }
    }

    private static void ratingsDist(Map<String, Integer> year, MovieInfo movieInfo) {
        if (year.containsKey(movieInfo.getRate())) {
            Integer count = year.get(movieInfo.getRate());
            year.put(movieInfo.getRate(), ++count);
        }
        else {
            year.put(movieInfo.getRate(), 1);
        }
    }

    private static void topYear(Map<String, MigrationOuputComplexAnalytics.TotalAvg> year, MovieInfo movieInfo) {
        MigrationOuputComplexAnalytics.Movie movie = MigrationOuputComplexAnalytics.Movie.of(
                movieInfo.getTitle(), movieInfo.getYear(), movieInfo.getRate());
        if (year.containsKey(movieInfo.getYear())) {
            MigrationOuputComplexAnalytics.TotalAvg count = year.get(movieInfo.getYear());
            count.addRating(movieInfo.getRate());
            count.getMovies().add(movie);
            year.put(movieInfo.getYear(), count);
        }
        else {
            year.put(movieInfo.getYear(),
                    new MigrationOuputComplexAnalytics.TotalAvg(movieInfo.getRate(), movie));
        }
    }

    private static void topYearByRatingDate(Map<String, MigrationOuputComplexAnalytics.TotalAvg> yearByRatingDate, MovieInfo movieInfo) {
        String ratingDate = movieInfo.getDate().split("-")[0];
        MigrationOuputComplexAnalytics.Movie movie = MigrationOuputComplexAnalytics.Movie.of(
                movieInfo.getTitle(), movieInfo.getYear(), movieInfo.getRate());
        if (yearByRatingDate.containsKey(ratingDate)) {
            MigrationOuputComplexAnalytics.TotalAvg count = yearByRatingDate.get(ratingDate);
            count.addRating(movieInfo.getRate());
            count.getMovies().add(movie);
            yearByRatingDate.put(ratingDate, count);
        }
        else {
            yearByRatingDate.put(ratingDate,
                    new MigrationOuputComplexAnalytics.TotalAvg(movieInfo.getRate(),movie));
        }
    }

    private static void topCountry(Map<String, MigrationOuputComplexAnalytics.TotalAvg> country, MovieInfo movieInfo) {
        MigrationOuputComplexAnalytics.Movie movie = MigrationOuputComplexAnalytics.Movie.of(
                movieInfo.getTitle(), movieInfo.getYear(), movieInfo.getRate());
        if (country.containsKey(movieInfo.getCountry())) {
            MigrationOuputComplexAnalytics.TotalAvg count = country.get(movieInfo.getCountry());
            count.addRating(movieInfo.getRate());

            count.getMovies().add(movie);
            country.put(movieInfo.getCountry(), count);
        }
        else {
            country.put(movieInfo.getCountry(),
                    new MigrationOuputComplexAnalytics.TotalAvg(movieInfo.getRate(),movie));
        }
    }

    private static void topActor(Map<String, List<MigrationOuputComplexAnalytics.Movie>> actors, Map<String, Integer> jokeActor, MovieInfo movieInfo) {
        for (String actor : movieInfo.getActors()) {
            if (actors.containsKey(actor)) {
                actors.get(actor).add(MigrationOuputComplexAnalytics.Movie.of(movieInfo.getTitle(),movieInfo.getYear(),movieInfo.getRate()));
            }
            else {
                actors.put(actor, new ArrayList<>(List.of(
                        MigrationOuputComplexAnalytics.Movie.of(movieInfo.getTitle(),movieInfo.getYear(),movieInfo.getRate()))));
            }
            if (actor.equals(JOKE_ACTOR)) {
                jokeActor.put(movieInfo.getTitle() + " - " + movieInfo.getRate(), Integer.valueOf(movieInfo.getYear()));
                break;
            }
        }
    }

    private static void topDirector(Map<String, List<MigrationOuputComplexAnalytics.Movie>> directors, MovieInfo movieInfo) {
        if (directors.containsKey(movieInfo.getDirector())) {
            directors.get(movieInfo.getDirector()).add(MigrationOuputComplexAnalytics.Movie.of(
                    movieInfo.getTitle(),movieInfo.getYear(),movieInfo.getRate()));
        }
        else {
            directors.put(movieInfo.getDirector(), new ArrayList<>(List.of(
                    MigrationOuputComplexAnalytics.Movie.of(movieInfo.getTitle(),movieInfo.getYear(),movieInfo.getRate()))));
        }
    }

    private static Map<String, List<MigrationOuputComplexAnalytics.Movie>> calculateTop(
            Map<String, List<MigrationOuputComplexAnalytics.Movie>> itemMap) {

        Map<String, Integer> itemMapBySize = new HashMap();
        for (Map.Entry<String, List<MigrationOuputComplexAnalytics.Movie>> entry : itemMap.entrySet()) {
            itemMapBySize.put(entry.getKey(), entry.getValue().size());
        }
        TreeMap<String, Integer> sortedMapBySize = sortMapByValue(itemMapBySize);

        Map<String, List<MigrationOuputComplexAnalytics.Movie>> sortedMap = new LinkedHashMap<>();
        int i = 0;
        for (Map.Entry<String,Integer> item : sortedMapBySize.entrySet()) {
            sortedMap.put(item.getKey(), itemMap.get(item.getKey()).stream().
                    sorted((o1, o2) -> o1.getYear().compareTo(o2.getYear())).
                    collect(Collectors.toList()));
            if (++i >= TOP_VALUES) {
                break;
            }
        }
        return sortedMap;
    }

    private static Map<String, String> calculateTop(Map<String, Integer> itemMap, boolean percentage) {
        TreeMap<String, Integer> sortedMap = sortMapByValue(itemMap);

        Map<String, String> sortedTop = new LinkedHashMap<>();
        int i = 0;
        int total = sortedMap.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<String,Integer> item : sortedMap.entrySet()) {
            if (percentage) {
                String percentageValue = " - " + FORMATTER.format((item.getValue() * 100.0f) / total) + "%";
                sortedTop.put(item.getKey(), " (" + item.getValue() + percentageValue + ")");
            }
            else {
                sortedTop.put(item.getKey(), item.getValue().toString());
            }
            if (++i >= TOP_VALUES) {
                break;
            }
        }
        return sortedTop;
    }

    private static Map<String, List<MigrationOuputComplexAnalytics.Movie>> calculateTopAvg(
            Map<String, MigrationOuputComplexAnalytics.TotalAvg> itemMap, boolean yearRatio) {

        Map<String, List<MigrationOuputComplexAnalytics.Movie>> sortedMap = new LinkedHashMap<>();
        Map<String, String> avgKeys = getTopAvgKeys(itemMap, true, yearRatio);

        for (Map.Entry<String, MigrationOuputComplexAnalytics.TotalAvg> item : itemMap.entrySet()) {
            sortedMap.put(item.getKey() + " - " + avgKeys.get(item.getKey()),
                    itemMap.get(item.getKey()).getMovies().stream().
                            sorted((o1, o2) -> reverseComparator(o1.getRating(), o2.getRating())).
                            limit(TOP_AVG_VALUES).
                            collect(Collectors.toList()));
        }

        return sortedMap;
    }

    private static Map<String, String> getTopAvgKeys(Map<String, MigrationOuputComplexAnalytics.TotalAvg> itemMap,
                                                     boolean percentage, boolean yearRatio) {

        ValueAvgComparator bvc = new ValueAvgComparator(itemMap);
        TreeMap<String, MigrationOuputComplexAnalytics.TotalAvg> sortedMap = new TreeMap<>(bvc);
        sortedMap.putAll(itemMap);

        Map<String, String> sortedTop = new LinkedHashMap<>();
        int i = 0;
        int total = sortedMap.values().stream().mapToInt(MigrationOuputComplexAnalytics.TotalAvg::getHits).sum();
        for (Map.Entry<String,MigrationOuputComplexAnalytics.TotalAvg> item : sortedMap.entrySet()) {
            if (percentage) {
                String percentageValue = " - " + FORMATTER.format((item.getValue().getHits() * 100.0f) / total) + "%";
                String value = " (" + item.getValue().getHits() + percentageValue + ") - " + item.getValue().avg();
                if (yearRatio) {
                    float yearDays = 365f;
                    if (ZonedDateTime.now().getYear() == Integer.valueOf(item.getKey())) {
                        yearDays = ZonedDateTime.now().getDayOfYear();
                    }
                    value+= " (" + FORMATTER.format(item.getValue().getHits() / yearDays) + " movies/day)";
                }
                sortedTop.put(item.getKey(), value);
            }
            else {
                sortedTop.put(item.getKey(), item.getValue().toString());
            }
        }
        return sortedTop;
    }

    private static Map<Integer, Integer> prepareForChart(TreeMap<String, MigrationOuputComplexAnalytics.TotalAvg> data) {
        Map<Integer, Integer> chartData = new LinkedHashMap<>();
        for (Map.Entry<String, MigrationOuputComplexAnalytics.TotalAvg> entry : data.entrySet()) {
            chartData.put(Integer.valueOf(entry.getKey()), entry.getValue().getMovies().size());
        }
        return chartData;
    }

    private static Map<Integer,Integer> prepareForChart(Map<String, Integer> ratings) {
        Map<Integer, Integer> chartData = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : ratings.entrySet()) {
            chartData.put(Integer.valueOf(entry.getKey()),entry.getValue());
        }
        return chartData;
    }

    private static TreeMap<String, MigrationOuputComplexAnalytics.TotalAvg> sortMapByKey(Map<String, MigrationOuputComplexAnalytics.TotalAvg> itemMap) {
        TreeMap<String, MigrationOuputComplexAnalytics.TotalAvg> sortedMapByKey =
                new TreeMap<>(AnalyticsComplexUtils::reverseComparator);
        sortedMapByKey.putAll(itemMap);
        return sortedMapByKey;
    }

    private static TreeMap<String, Integer> sortMapByValue(Map<String, Integer> itemMap) {
        ValueComparator bvc = new ValueComparator(itemMap);
        TreeMap<String, Integer> sortedMap = new TreeMap<>(bvc);
        sortedMap.putAll(itemMap);
        return sortedMap;
    }

    private static Map<String, MigrationOuputComplexAnalytics.TotalAvg> sortMapMoviesByValue(
            Map<String, MigrationOuputComplexAnalytics.TotalAvg> itemMap) {
        Map<String, Integer> itemMapBySize = new HashMap();
        for (Map.Entry<String, MigrationOuputComplexAnalytics.TotalAvg> entry : itemMap.entrySet()) {
            itemMapBySize.put(entry.getKey(), entry.getValue().getMovies().size());
        }
        TreeMap<String, Integer> sortedMapBySize = sortMapByValue(itemMapBySize);

        Map<String, MigrationOuputComplexAnalytics.TotalAvg> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String,Integer> item : sortedMapBySize.entrySet()) {
            sortedMap.put(item.getKey(), itemMap.get(item.getKey()));
        }
        return sortedMap;
    }

    private static int reverseComparator(String o1, String o2) {
        return Integer.valueOf(o1).compareTo(Integer.valueOf(o2)) * -1;
    }

}
