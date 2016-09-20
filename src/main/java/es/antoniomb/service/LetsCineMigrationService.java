package es.antoniomb.service;

import es.antoniomb.dto.MovieInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by amiranda on 18/9/16.
 */
@Service
public class LetsCineMigrationService implements IMigrationService {

    @Override
    public List<MovieInfo> getRatings(String username, String password) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public Integer setRatings(String username, String password, List<MovieInfo> moviesInfo) {
//        throw new RuntimeException("Not implemented!");
        return 0;
    }
}
