package util.DB;

import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class PSAClientTest {

    @Test
    public void sendPost() throws IOException {
        System.out.println(PSAClient.sendPost("1203", "855467","https://passport.avs.com.ru/"));

    }

    @Test
    public void checkpass() throws SQLException, IOException, ClassNotFoundException {
        var PSAClient = new PSAClient("jdbc:mysql://192.168.0.121:3306/psa", "root", "123", "https://passport.avs.com.ru/");
        PSAClient.checkpass("1203855467");
    }

    @Test
    public void testGetStatusText() throws SQLException, ClassNotFoundException {
        var input = "{\"Id\":\"\",\"Serie\":\"1210\",\"Number\":\"322280\",\"Status\":1,\"StatusText\":\"является действующим.\"}\n";
        var input2 = "{\"Id\":\"\",\"Serie\":\"1203\",\"Number\":\"855467\",\"Status\":0,\"StatusText\":\"ЯВЛЯЕТСЯ НЕДЕЙСТВИТЕЛЬНЫМ!\"}\n";
        var psasearch =  new PSAClient("jdbc:mysql://192.168.0.121:3306/psa", "root", "123", "https://passport.avs.com.ru/");
        var etalon = "является действующим.";
        var etalon2 = "ЯВЛЯЕТСЯ НЕДЕЙСТВИТЕЛЬНЫМ!";
        assertEquals(etalon, psasearch.getStatusText(input));
        assertEquals(etalon2, psasearch.getStatusText(input2));

    }
    @Test
    public void  testcheckpass() throws SQLException, IOException, ClassNotFoundException {
        var psasearch =  new PSAClient("jdbc:mysql://192.168.0.121:3306/psa", "root", "123", "https://passport.avs.com.ru/");
        System.out.println(psasearch.checkpass("4544545545"));
        assertTrue(psasearch.checkpass("4544545545"));
//        assertFalse(psasearch.checkpass("1203855467"));
        assertTrue(psasearch.checkpass("120385556"));

    }

}