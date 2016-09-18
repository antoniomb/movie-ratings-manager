package es.antoniomb.login;

import es.antoniomb.service.FAMigrationService;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by amiranda on 18/9/16.
 */
public class FALoginTest {

    private FAMigrationService faMigrationService = new FAMigrationService();

    @Test
    public void login(){
        String userId = faMigrationService.login("antoniomiranda", "Abc123456");
        Assert.assertEquals(userId,"4382195");
    }
}
