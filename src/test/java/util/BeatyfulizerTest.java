package util;

import org.json.simple.parser.ParseException;
import org.junit.Test;
import se.roland.JSON.ParcedJSON;
import util.JSON.Beatyfulizer;

import static org.junit.Assert.*;

public class BeatyfulizerTest {
    String oldaddress = "http://127.0.1.1:12215/";
    String trimmed = "http://127.0.1.1:12215";
    @Test
    public void comperaJSON() throws ParseException {
        String first ="{\"Date\": \"2020-11-03\", \"Mode\": \"Приемка\", \"Tara\": \"0.0\", \"Time\": \"10:56:50\", \"Netto\": \"6.75\", \"Trash\": \"0.0\", \"Brutto\": \"13.5\", \"Metall\": \"Алюминий хлам\", \"Comment\": \"сушкмвич\", \"Clogging\": \"50.0\", \"Complete\": \"Да\", \"Waybill_number\": \"2\"}";
        String second ="{\"Date\": \"2020-11-03\", \"Mode\": \"Приемка\", \"Tara\": \"0.0\", \"Time\": \"10:56:50\", \"Netto\": \"6.75\", \"Trash\": \"0.0\", \"Brutto\": \"13.5\", \"Metall\": \"Алюминий 000\", \"Comment\": \"сушкмвич\", \"Clogging\": \"50.0\", \"Complete\": \"Да\", \"Waybill_number\": \"2\"}";
        assertNotEquals(null,  Beatyfulizer.compareundschoneJSON(ParcedJSON.parse(first), ParcedJSON.parse(second)) );
        System.out.println(Beatyfulizer.compareundschoneJSON(ParcedJSON.parse(first), ParcedJSON.parse(second)));
    }

    @Test
    public void trimAddress() {
        String oldaddress = "http://127.0.1.1:12215/";
        String trimmed = "http://127.0.1.1:12215";
        assertEquals(trimmed, Beatyfulizer.trimAddress(oldaddress));
    }

    @Test
    public void testTrimAddress() {
        System.out.println(oldaddress.charAt(oldaddress.length()-2));
    }
}