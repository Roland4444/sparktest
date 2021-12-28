package util;

import DSLGuided.requestsx.PSA.PSAConnector;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class LoginProcessorTest {


    @Test
    public void extractedcheck() throws SQLException {
        String hash = "$2a$10$TBcK.P2mRucu8ejT39X1yerb5dHKSZdilQ5bUoDEc00tUOEIAR1ZS";
        String pass = "Igor123456";
        LoginProcessor login = new LoginProcessor();
        assertEquals(true, login.checkbcryptpass(pass, hash));
    }


    @Test
    public void testsecurerandom() throws NoSuchProviderException, NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("NativePRNGBlocking", "SUN");
        byte[] b = new byte[10000];
        sr.nextBytes(b);
        for (int i = 0; i<10000; i++){
            System.out.println(b[i]/255.0);
        }
     //   System.out.println(Arrays.toString(b/255));

    }

    @Test
    public void checkpsalogin() throws SQLException {
        String user = "igor";
        String pass = "Igor123456";
        LoginProcessor login = new LoginProcessor();
        var psaconnectordsl = "'psaconnector'=>::psa{'login':'root','pass':'123'},::db{jdbc:mysql://192.168.0.121:3306/psa},::enabled{'true'}." ;
        PSAConnector connector = new PSAConnector();
        connector.r(psaconnectordsl);
        login.PSAConnector = connector;
        assertEquals(true, login.checkpsalogin(user, pass));


    }

    @Test
    public void getpsadepid() throws SQLException {
        String user = "igor";
        LoginProcessor login = new LoginProcessor();
        var psaconnectordsl = "'psaconnector'=>::psa{'login':'root','pass':'123'},::db{jdbc:mysql://192.168.0.121:3306/psa},::enabled{'true'}." ;
        PSAConnector connector = new PSAConnector();
        connector.r(psaconnectordsl);
        login.PSAConnector = connector;
        assertEquals(null, login.getpsadepid("coder"));
        assertEquals("24", login.getpsadepid("denis"));
    }
}