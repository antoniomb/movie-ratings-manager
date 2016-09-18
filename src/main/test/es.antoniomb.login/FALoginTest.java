package es.antoniomb.login;

import es.antoniomb.service.FAMigrationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by amiranda on 18/9/16.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { FAMigrationService.class })
public class FALoginTest {

    @Autowired
    private FAMigrationService faMigrationService;

    @Test
    public void login(){
        String userId = faMigrationService.login("antoniomiranda", "Abc123456");
        Assert.assertEquals(userId,"4382195");
    }
}
