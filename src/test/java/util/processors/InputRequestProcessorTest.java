package util.processors;

import abstractions.Condition;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static util.processors.InputRequestProcessor.transformlongString;

public class InputRequestProcessorTest {
    InputRequestProcessor irp = new InputRequestProcessor();
    OutputResponceProcessor orp = new OutputResponceProcessor();
    public String filename = "temp.bin";
    public String id ="12";
    public Condition cond = Condition.APPROVED;
    @Test
    public void getStatus() throws IOException {
        orp.saveStatus(filename, id, cond);
        Condition rest =irp.getStatus(id, filename);
        assertEquals(cond, rest);
        assertEquals(null, irp.getStatus("ioioio", "fdjghjfdhgjdfhgfdj"));
    }


    @Test
    public void removeStatus() throws IOException {
        orp.saveStatus(filename, id, cond);
        Condition rest =irp.getStatus(id, filename);
        assertEquals(cond, rest);
        assertEquals(null, irp.getStatus("ioioio", "fdjghjfdhgjdfhgfdj"));
        irp.removeStatus(id, filename);
        assertEquals(null, irp.getStatus("ioioio", filename));

    }

    @Test
    public void transformlongStringtest() {
        String input222 = "realylongcommentary";
        String etalon = "realy<br>longc<br>ommen<br>tary";
        assertEquals(etalon, transformlongString(input222,5));
    }
}