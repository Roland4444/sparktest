package abstractions;

import Message.abstractions.BinaryMessage;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SettingsTest {
    Settings setts = new Settings();
    @Test
    public void initSetts() throws IOException {
        setts.AktorPORT="http://127.0.0.1:12121/";
        setts.requestsPOSTGRESConnect="jdbc:postgresql:// 192.168.0.126/requests?user=user2&password=123";
        setts.usersPostgresConnect="jdbc:postgresql:// 192.168.0.126/users?user=user2&password=123";
        BinaryMessage.write(BinaryMessage.savedToBLOB(setts), "setts2.bin");
    }


}