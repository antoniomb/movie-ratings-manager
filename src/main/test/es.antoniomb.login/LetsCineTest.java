package es.antoniomb.login;

import es.antoniomb.dto.UserInfo;
import es.antoniomb.service.LetsCineMigrationService;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by amiranda on 21/9/16.
 */
public class LetsCineTest {

    private LetsCineMigrationService letsCineMigrationService = new LetsCineMigrationService();

    @Test
    public void login(){
        UserInfo userInfo = letsCineMigrationService.login("antoniomiranda", "Abc123456");

        Assert.assertTrue(userInfo.getCookies().containsKey("Elgg"));
    }
}
