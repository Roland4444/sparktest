package util.DSLUtil;

import DSLGuided.requestsx.DSL;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DSLTest {

    @Test
    public void getDSLforObject() throws IOException {
        DSL dsl = new DSL();
        assertEquals("'requests' => ::full{}.", dsl.getDSLforObject("requests","roman"));
    }

    @Test
    public void testGetDSLforObject() {
    }
}