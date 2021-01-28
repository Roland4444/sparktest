package util;
import org.junit.Test;
import util.DB.DataBaseHelper;

import java.sql.SQLException;
import static org.junit.Assert.*;
public class LoginCheckerTest {

    @Test
    public void checklogin() throws SQLException {
        LoginProcessor check = new LoginProcessor(new DataBaseHelper().executor);
        assertEquals(true, check.checklogin("roman", "123"));
    }
}