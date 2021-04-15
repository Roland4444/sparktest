package servers;

import org.junit.Test;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class ServerAktorTest {

    @Test
    public void ipaddrTest(){
        String ip=null;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        assertNotEquals(null, ip);
        System.out.println(ip);

    };

    @Test
    public void getRollbackTest() throws UnknownHostException {
        ServerAktor aktor = new ServerAktor();
        aktor.setAddress("http://127.0.0.1:12121/");
        assertNotEquals(null, aktor.rollbackAdressURL());
        System.out.println(aktor.rollbackAdressURL());
    }

    @Test
    public void sendPost() throws Exception {
        ServerAktor.sendPost("1203", "855467", "https://passport.avs.com.ru/");
    }
}