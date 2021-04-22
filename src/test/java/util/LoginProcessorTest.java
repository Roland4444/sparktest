package util;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class LoginProcessorTest {

    @Test
    public void check() throws SQLException {
        String hash = "$2a$10$TBcK.P2mRucu8ejT39X1yerb5dHKSZdilQ5bUoDEc00tUOEIAR1ZS";
        String pass = "Igor123456";
        LoginProcessor login = new LoginProcessor();
        assertEquals(true, login.check(pass, hash));
    }
}