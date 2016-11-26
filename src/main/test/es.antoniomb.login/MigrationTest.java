package es.antoniomb.login;

import es.antoniomb.dto.MigrationInput;
import es.antoniomb.dto.MigrationOutput;
import es.antoniomb.dto.enums.MigrationWeb;
import es.antoniomb.service.FAMigrationService;
import es.antoniomb.service.IMDBMigrationService;
import es.antoniomb.service.LetsCineMigrationService;
import es.antoniomb.service.MigrationService;
import es.antoniomb.utils.MigrationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by amiranda on 20/9/16.
 */
public class MigrationTest {

    private MigrationService migrationService = new MigrationService(
            new FAMigrationService(),new IMDBMigrationService(),new LetsCineMigrationService());

    @Test
    public void migration(){
        MigrationInput input = new MigrationInput("filmaffinity","antoniomiranda","Abc123456",
                                                  "letscine", "in79phanhibh8vkdmt27s95bv4", null, null, null);

        MigrationUtils.validateParams(input);
        Assert.assertEquals(input.getSource(), MigrationWeb.FILMAFFINITY);
        Assert.assertEquals(input.getTarget(), MigrationWeb.LETSCINE);

        MigrationOutput output = migrationService.migrate(input);

        Assert.assertTrue(output.getSourceStatus());
        Assert.assertTrue(output.getTargetStatus());
//        Assert.assertEquals(output.getMoviesWrited().longValue(), 0L);
    }
}
