package es.antoniomb.service;

import es.antoniomb.dto.MovieInfo;

import java.util.List;

/**
 * Created by amiranda on 20/9/16.
 */
public interface IMigrationService {

    List<MovieInfo> getRatings(String username, String password);

    Integer setRatings(String username, String password, List<MovieInfo> moviesInfo);

}
