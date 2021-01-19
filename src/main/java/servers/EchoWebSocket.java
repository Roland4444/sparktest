package servers;

import Message.abstractions.BinaryMessage;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import util.DB.DataBaseHelper;
import util.JSON.LoaderJSON;

import java.io.*;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;


@WebSocket(maxIdleTime=50000000)
public class EchoWebSocket {
    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    public static String binprops = "setts.bin";
    private static abstractions.Settings setts;

    static {
        try {
            setts = (abstractions.Settings) BinaryMessage.restored(BinaryMessage.readBytes(binprops));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static DataBaseHelper requests;//requests = new DataBaseHelper("requests");

    static {
        try {
            requests = new DataBaseHelper(setts.requestsPOSTGRESConnect, true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public  static LoaderJSON LoaderJSON  = new LoaderJSON(requests.executor );;

    public static void sendall(String id) throws SQLException {
        ArrayList param = new ArrayList();
        param.add(id);
        String req = LoaderJSON.LoadResult2JSON(id);
        System.out.println("SEND MESSAGE::"+req);
        sessions.forEach(a-> {

            try {
                a.getRemote().sendString(req);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @OnWebSocketConnect
    public void connected(Session session) {
        System.out.println("Connceted: " );   // Print message
        sessions.add(session);

    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException, SQLException {
        if (message.equals("load")){
            System.out.println("SEND JSON");

            String data = LoaderJSON.loadAll2JSON();
      //      System.out.println(data);
//            fos.write(data.getBytes());
 //           fos.close();
            session.getRemote().sendString(data);
            return;
       }
       System.out.println("Got: " + message);   // Print message
     session.getRemote().sendString(message); // and send it back

    }


}