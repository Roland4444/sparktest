package util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import se.roland.JSON.ParcedJSON;

import static org.junit.Assert.*;

public class ParcedJSONTest {

    @Test
    public void parse() throws ParseException {
        String input = "{\"Date\": \"2020-11-05\", \"Mode\": \"Приемка\", \"Tara\": \"0.0\", \"Time\": \"14:16:26\", \"Netto\": \"0.99\", \"Trash\": \"0.1\", \"Brutto\": \"1.1\", \"Metall\": \"Нержавейка\", \"Comment\": \"войт\", \"Clogging\": \"1.0\", \"Complete\": \"Да\", \"Condition\": \"Выгружен\", \"Waybill_number\": \"8\"}";
        System.out.println(input);
        assertEquals("Нержавейка", ParcedJSON.parse(input).Metall);
        Object obj = new JSONParser().parse(input);
        JSONObject jo = (JSONObject) obj;
        String Tara = (String) jo.get("Tara");
        assertEquals("0.0", Tara);
    }
}