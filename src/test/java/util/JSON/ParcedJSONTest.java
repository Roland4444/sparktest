package util.JSON;

import org.junit.Test;
import se.roland.JSON.ParcedJSON;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class ParcedJSONTest {


    public void requestMessage2JSON() throws IOException {
        String json = ParcedJSON.RequestMessage2JSON(Files.readAllBytes(Path.of("DUMP.INPUT")));
        assertNotEquals(null, json);System.out.print(json);

    }
}