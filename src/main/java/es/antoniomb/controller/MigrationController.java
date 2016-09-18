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

    @RequestMapping("/endpoint1")
    @ResponseBody
    MovieInfoDTO endpoint1() throws SQLException {
        return genericDao.get();
    }

    @RequestMapping("/endpoint2")
    @ResponseBody
    List<MovieInfoDTO> endpoint2() throws SQLException {
        return genericDao.getList();
    }

    @RequestMapping("/endpoint3")
    @ResponseBody
    List<MovieInfoDTO> endpoint3() throws SQLException {
        List<MovieInfoDTO> list = genericDao.getList();
        list.add(genericDao.get());
        return list;
    }


}
