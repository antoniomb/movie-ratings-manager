package es.antoniomb.service;

import com.opencsv.CSVWriter;
import es.antoniomb.dto.MigrationInput;
import es.antoniomb.dto.MigrationOutput;
import es.antoniomb.dto.MovieInfo;
import es.antoniomb.utils.ValueComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by amiranda on 20/9/16.
 */
@Service
public class MigrationService {

    private static Logger LOGGER = Logger.getLogger(MigrationService.class.getName());

    @Autowired
    private FAMigrationService fa;
    @Autowired
    private IMDBMigrationService imdb;
    @Autowired
    private LetsCineMigrationService letsCine;

    public MigrationService() {
    }

    public MigrationService(FAMigrationService fa, IMDBMigrationService imdb, LetsCineMigrationService letsCine) {
        this.fa = fa;
        this.imdb = imdb;
        this.letsCine = letsCine;
    }

    public MigrationOutput migrate(MigrationInput migrationInfo) {

        MigrationOutput result = new MigrationOutput();

        List<MovieInfo> moviesInfo = getRatings(migrationInfo, result);

        Integer moviesImported = setRatings(migrationInfo, result, moviesInfo);

        processResult(result, moviesInfo, moviesImported);

        return result;
    }

    private List<MovieInfo> getRatings(MigrationInput migrationInfo, MigrationOutput result) {
        List<MovieInfo> moviesInfo = new ArrayList<>();
        try {
            switch (migrationInfo.getSource()) {
                case FILMAFFINITY:
                    moviesInfo = fa.getRatings(migrationInfo);
                    break;
                case IMDB:
                    moviesInfo = imdb.getRatings(migrationInfo);
                    break;
                case LETSCINE:
                    moviesInfo = letsCine.getRatings(migrationInfo);
                    break;
                default:
            }
            result.setSourceStatus(true);
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error getting ratings", e);
            result.setSourceStatus(false);
        }
        return moviesInfo;
    }

    private Integer setRatings(MigrationInput migrationInfo, MigrationOutput result, List<MovieInfo> moviesInfo) {
        Integer moviesImported = 0;
        try {
            switch (migrationInfo.getTarget()) {
                case FILMAFFINITY:
                    moviesImported = fa.setRatings(migrationInfo, moviesInfo);
                    break;
                case IMDB:
                    moviesImported = imdb.setRatings(migrationInfo, moviesInfo);
                    break;
                case LETSCINE:
                    moviesImported = letsCine.setRatings(migrationInfo, moviesInfo);
                    break;
                case CSV:
                    result.setCsv(generateCSV(moviesInfo));
                    result.setMoviesWrited(moviesInfo.size());
                    break;
                case ANALYSIS:
                    result.setMoviesWrited(moviesInfo.size());
                    result.setTopDirector(calculateTopDirectors(moviesInfo));
                    result.setTopActor(calculateTopActors(moviesInfo));
                    result.setTopCountry(calculateTopCountry(moviesInfo));
                    result.setTopYear(calculateTopYear(moviesInfo));
                default:
            }
            result.setTargetStatus(true);
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error setting ratings", e);
            result.setTargetStatus(false);
        }
        return moviesImported;
    }

    private String calculateTopDirectors(List<MovieInfo> moviesInfo) {
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

    private String calculateTopActors(List<MovieInfo> moviesInfo) {
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

    private String calculateTopCountry(List<MovieInfo> moviesInfo) {
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

    private String calculateTopYear(List<MovieInfo> moviesInfo) {
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

    private String calculateTop(Map<String, Integer> itemMap) {
        ValueComparator bvc = new ValueComparator(itemMap);
        TreeMap<String, Integer> sortedMap = new TreeMap<>(bvc);
        sortedMap.putAll(itemMap);
        String top = "";
        int i = 0;
        for (Map.Entry<String,Integer> item : sortedMap.entrySet()) {
            if (i++ == 9 || i == sortedMap.entrySet().size()) {
                top+=item.getKey()+"("+item.getValue()+")";
                break;
            }
            top+=item.getKey()+"("+item.getValue()+")"+", ";
        }
        return top;
    }

    private void processResult(MigrationOutput result, List<MovieInfo> moviesInfo, Integer moviesImported) {
        if (result.getSourceStatus()) {
            result.setMoviesReaded(moviesInfo.size());
            if (!moviesInfo.isEmpty()) {
                result.setRatingAvg(moviesInfo.stream().mapToDouble(m -> Double.valueOf(m.getRate())).average().getAsDouble());
            }
            if (result.getTargetStatus()) {
                result.setMoviesWrited(moviesImported);
            }
        }
    }

    private String generateCSV(List<MovieInfo> moviesInfo) {
        CSVWriter writer = null;
        try {
            StringWriter strWriter = new StringWriter();
            writer = new CSVWriter(strWriter, '\t', CSVWriter.NO_QUOTE_CHARACTER);
            writer.writeNext(new String[]{"id","title","year","ratingDate","rating"});
            for (MovieInfo movieInfo : moviesInfo) {
                String[] row = new String[]{movieInfo.getId(),movieInfo.getTitle(),movieInfo.getYear(),
                                                movieInfo.getDate(),movieInfo.getRate()};
                writer.writeNext(row);
            }
            return strWriter.toString();
        }
        finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error closing csv", e);
            }
        }
    }
}
