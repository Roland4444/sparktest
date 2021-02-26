package DSLGuided.requestsx;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class requestsTest {

    @Test
    public void parseDSL() {
        var req = new requests();
        String dsl ="'requests' => ::read{}, ::write{}, ::create{}.";
        assertEquals("read", req.parseroleDSL(dsl));
        ArrayList arr = new ArrayList();
        arr.add("read");
        arr.add("write");
        arr.add("create");
        assertEquals(arr.toString(), req.parseroles(dsl));
    }
}