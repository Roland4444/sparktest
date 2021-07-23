package test;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.*;

public class TestClassTest {

    @Test
    public void requestDir(){
        if (!new File("requests").exists())
            new File("requests").mkdir();
        assertTrue(new File("requests").exists());

    }


}