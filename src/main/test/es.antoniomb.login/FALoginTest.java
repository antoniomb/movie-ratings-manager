package es.antoniomb.login;

import es.antoniomb.service.FAMigrationService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by amiranda on 18/9/16.
 */
public class FALoginTest {

    private FAMigrationService faMigrationService = new FAMigrationService();

    @Test
    public void login(){
        Map<String, String> cookies = faMigrationService.login("antoniomiranda", "Abc123456");

        Assert.assertTrue(cookies.containsKey("FSID"));
    }

    @Test
    public void getRatings() {
        Map<String, String> cookies = faMigrationService.login("antoniomiranda", "Abc123456");
        faMigrationService.getRatings(cookies);
    }
}
