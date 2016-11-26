package es.antoniomb.service;

import es.antoniomb.dto.MigrationInput;
import es.antoniomb.dto.MovieInfo;

import java.util.List;

/**
 * Created by amiranda on 20/9/16.
 */
public interface IMigrationService {

    List<MovieInfo> getRatings(MigrationInput input);

    Integer setRatings(MigrationInput input, List<MovieInfo> moviesInfo);

}
