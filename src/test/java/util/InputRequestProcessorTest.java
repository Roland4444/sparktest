package util;

import abstractions.RequestMessage;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import util.DB.DataBaseHelper;
import util.processors.InputRequestProcessor;

import java.sql.ResultSet;
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
    public void saveUpdatingRequestinDB() throws SQLException, ParseException {
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

    @Test
    public void testSaveUpdatingRequestinDB() throws SQLException {
        ArrayList<ArrayList> matrix = irp.loadrequests8inmatrix();
        assertNotEquals(null, matrix);
        for (int i=0; i<matrix.size(); i++){
            for (int j=0; j<matrix.get(0).size(); j++){
                System.out.println(matrix.get(i).get(j));
            }
        }
    }



  //  @Test
    public void lengthextract() throws SQLException {
        ArrayList data = new ArrayList();
        data.add(67);
        Object extracted=null;
        ResultSet res = db.executor.executePreparedSelect("SELECT * FROM requests WHERE counter=?", data);
        while (res.next()){
            extracted = res.getObject("updateddata");
        }
        assertNotEquals(null, extracted);
        System.out.println(String.valueOf(extracted));
        System.out.println(String.valueOf(extracted).length());
    }


}