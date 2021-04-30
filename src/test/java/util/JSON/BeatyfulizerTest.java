package util.JSON;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BeatyfulizerTest {
    @Test
    public void testparser() throws ParseException {
        String input = "{\"8\":{\"weight\":435.16,\"cost\":150281.2,\"median\":345.35},\"5\":{\"weight\":863.04,\"cost\":487617.6,\"median\":565}}";
        JSONParser parcer = new JSONParser();
        JSONArray parced = (JSONArray) parcer.parse(input);
        assertNotEquals(null, parced);
    }
}