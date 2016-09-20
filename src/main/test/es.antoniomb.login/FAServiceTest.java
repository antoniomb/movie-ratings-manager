package es.antoniomb.login;

import es.antoniomb.dto.UserInfo;
import es.antoniomb.service.FAMigrationService;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by amiranda on 18/9/16.
 */
public class FAServiceTest {

    private FAMigrationService faMigrationService = new FAMigrationService();

    @Test
    public void login(){
        UserInfo userInfo = faMigrationService.login("antoniomiranda", "Abc123456");

        Assert.assertTrue(userInfo.getCookies().containsKey("FSID"));
    }

    @Test
    public void fillUserInfo() {
        UserInfo userInfo = faMigrationService.login("antoniomiranda", "Abc123456");
        faMigrationService.fillUserInfo(userInfo);

        Assert.assertEquals(userInfo.getUserId(), "4382195");
    }

    @Test
    public void fillMoviesInfo() {
        UserInfo userInfo = faMigrationService.login("antoniomiranda", "Abc123456");
        faMigrationService.fillUserInfo(userInfo);
        faMigrationService.fillMoviesInfo(userInfo);
    }
}
