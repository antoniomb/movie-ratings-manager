package es.antoniomb.controller;

import es.antoniomb.dto.MigrationInput;
import es.antoniomb.dto.MigrationOutput;
import es.antoniomb.service.MigrationService;
import es.antoniomb.utils.MigrationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * Created by amiranda on 18/09/16.
 */
@Controller
public class MigrationController {

    @Autowired
    private MigrationService migrationService;

    @RequestMapping(value = "/migrate", method = RequestMethod.POST)
    @ResponseBody
    MigrationOutput migrate(@RequestBody MigrationInput migrationInfo) throws SQLException {

        MigrationUtils.parseWebCode(migrationInfo);

        return migrationService.migrate(migrationInfo);
    }


}
