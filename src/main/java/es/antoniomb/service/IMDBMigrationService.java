package es.antoniomb.service;

import es.antoniomb.dto.MigrationInput;
import es.antoniomb.dto.MovieInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by amiranda on 18/9/16.
 */
@Service
public class IMDBMigrationService implements IMigrationService {

    @Override
    public List<MovieInfo> getRatings(MigrationInput input) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public Integer setRatings(MigrationInput input, List<MovieInfo> moviesInfo) {
        throw new RuntimeException("Not implemented!");
    }
}
