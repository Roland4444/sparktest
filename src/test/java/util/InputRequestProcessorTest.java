package util;

import abstractions.RequestMessage;
import org.junit.Test;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class InputRequestProcessorTest {
    DataBaseHelper db =  new DataBaseHelper("requests");
    InputRequestProcessor irp = new InputRequestProcessor(db.executor);
    public InputRequestProcessorTest() throws SQLException {
    }

    @Test
    public void saveRequestinDB() throws SQLException {
        RequestMessage req = new RequestMessage("555" , "must done", "{\"name\": \"roman\"}");
    //    irp.saveRequestinDB(req);
        ArrayList reqs = irp.loadrequests();
        assertNotEquals(0, reqs.size());


    }

    @Test
    public void loadrequests() throws SQLException {
        ArrayList reqs = irp.loadrequests();
        assertNotEquals(0, reqs.size());
    }

    @Test
    public void testSaveRequestinDB() throws SQLException {
        assertNotEquals(null, irp.DumpRequestToHTMLTable());
        System.out.println(irp.DumpRequestToHTMLTable());
    }

    @Test
    public void saveUpdatingRequestinDB() throws SQLException {
        RequestMessage req = new RequestMessage("555" , "must done", "{\"name\": \"roland\"}");
        //    irp.saveRequestinDB(req);
        irp.saveUpdatingRequestinDB(req);
        irp.loadrequests();
    }

    @Test
    public void generaterow() throws SQLException {
        ArrayList reqs = irp.loadrequests8();
    }

    @Test
    public void date() {
        long now = new Date().getTime();
        System.out.println(now);
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("stamp::>>>"+timeStamp);
       // Date date = new java.sql.Date(timeStamp);
       // System.out.println("Date = "+date.toString());

    }
}