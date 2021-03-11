package util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DepsTest {

    @Test
    public void initSubscribers() throws IOException {
        byte[] arr =Saver.Companion.readBytes("cred.bin");
        Credential cred = (Credential) Saver.Companion.restored(arr);
        System.out.println(cred.getLogin());
        System.out.println(cred.getPass());

    }
}