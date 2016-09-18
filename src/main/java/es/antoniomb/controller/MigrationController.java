package es.antoniomb.controller;

import es.antoniomb.service.FAMigrationService;
import es.antoniomb.dto.MovieInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by amiranda on 18/09/16.
 */
@Controller
public class MigrationController {

    @Autowired
    private FAMigrationService genericDao;

    @RequestMapping("/dummy")
    @ResponseBody
    MovieInfoDTO endpoint1() throws SQLException {
        return null;
    }


}
