package es.antoniomb.service;

import com.opencsv.CSVWriter;
import es.antoniomb.dto.MigrationInput;
import es.antoniomb.dto.MigrationOutput;
import es.antoniomb.dto.MovieInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
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
                    moviesInfo = fa.getRatings(migrationInfo.getFromUsername(), migrationInfo.getFromPassword());
                    break;
                case IMDB:
                    moviesInfo = imdb.getRatings(migrationInfo.getFromUsername(), migrationInfo.getFromPassword());
                    break;
                case LETSCINE:
                    moviesInfo = letsCine.getRatings(migrationInfo.getFromUsername(), migrationInfo.getFromPassword());
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
                    moviesImported = fa.setRatings(migrationInfo.getToUsername(), migrationInfo.getToPassword(), moviesInfo);
                    break;
                case IMDB:
                    moviesImported = imdb.setRatings(migrationInfo.getToUsername(), migrationInfo.getToPassword(), moviesInfo);
                    break;
                case LETSCINE:
                    moviesImported = letsCine.setRatings(migrationInfo.getToUsername(), migrationInfo.getToPassword(), moviesInfo);
                    break;
                case CSV:
                    result.setCsv(generateCSV(moviesInfo));
                    break;
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

    private void processResult(MigrationOutput result, List<MovieInfo> moviesInfo, Integer moviesImported) {
        if (result.getSourceStatus()) {
            result.setMoviesReaded(moviesInfo.size());
            result.setRatingAvg(moviesInfo.stream().mapToDouble(m -> Double.valueOf(m.getRate())).average().getAsDouble());
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
