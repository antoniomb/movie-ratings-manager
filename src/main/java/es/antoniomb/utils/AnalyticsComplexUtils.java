package es.antoniomb.utils;

import es.antoniomb.dto.MigrationOuputComplexAnalytics;
import es.antoniomb.dto.MovieInfo;
import org.apache.commons.collections.MapUtils;

import java.text.DecimalFormat;
import java.util.*;

public class AnalyticsComplexUtils {

    private static DecimalFormat formatter = new DecimalFormat("0.0");

    public static MigrationOuputComplexAnalytics calculateAnalytics(List<MovieInfo> moviesInfo) {
        MigrationOuputComplexAnalytics analytics = new MigrationOuputComplexAnalytics();

        Map<String, List<String>> directors = analytics.getDirectors();
        Map<String, List<String>> actors = analytics.getActors();
        for (MovieInfo movieInfo : moviesInfo) {

            //Build director info
            if (directors.containsKey(movieInfo.getDirector())) {
                directors.get(movieInfo.getDirector()).add(buildMovieStr(movieInfo));
            }
            else {
                directors.put(movieInfo.getDirector(), new ArrayList<>(List.of(buildMovieStr(movieInfo))));
            }

            //Build actor info
            for (String actor : movieInfo.getActors()) {
                if (actors.containsKey(actor)) {
                    actors.get(actor).add(buildMovieStr(movieInfo));
                }
                else {
                    actors.put(actor, new ArrayList<>(List.of(buildMovieStr(movieInfo))));
                }
            }
        }

        MapUtil.sortByValue(directors);
        MapUtil.sortByValue(actors);

        return analytics;
    }

    private static String buildMovieStr(MovieInfo movieInfo) {
        return movieInfo.getTitle()+" ("+movieInfo.getYear()+")";
    }

}
