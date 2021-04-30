package util.JSON;

import Message.abstractions.BinaryMessage;
import org.eclipse.jetty.util.Loader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import util.DB.DataBaseHelper;


import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class LoaderJSONTest {
    public String binprops = "setts.bin";
    public String declinedid = "2020-12-1513:56:33ошщ";
    private abstractions.Settings setts  = (abstractions.Settings) BinaryMessage.restored(BinaryMessage.readBytes(binprops));
    DataBaseHelper requests = new DataBaseHelper(setts.requestsPOSTGRESConnect, true);//requests = new DataBaseHelper("requests");

    LoaderJSON loader = new LoaderJSON(requests.executor );

    public LoaderJSONTest() throws IOException, SQLException {
    }

    @Test
    public void loadResult2JSON() throws SQLException, IOException {
        assertNotEquals(null, loader.LoadResult2JSONOne("2020-11-0316:18:04витек"));
        System.out.println(loader.LoadResult2JSONOne("2020-11-0316:18:04витек"));
        FileOutputStream fos = new FileOutputStream("JSON.DUMP");
        fos.write(loader.LoadResult2JSON("2020-11-0316:18:04витек").getBytes());
        fos.close();
        FileOutputStream fos2 = new FileOutputStream("JSON.DUMP.ONEUPDATED");
        fos2.write(loader.LoadResult2JSON("2020-11-0316:18:04витек").getBytes());
        fos2.close();
    }

    @Test
    public void loadResult2JSON_declined() throws SQLException, IOException {
        assertNotEquals(null, loader.LoadResult2JSONOne(declinedid));
        System.out.println(loader.LoadResult2JSONOne("2020-11-0316:18:04витек"));
        FileOutputStream fos = new FileOutputStream("JSON.DUMP.DECLINED");
        fos.write(loader.LoadResult2JSON(declinedid).getBytes());
        fos.close();
    }

    @Test
    public void loaderJSON() throws ParseException {
        var json = """
                {"id":37116,"waybill":33,"date":"2021-04-27","time":"17:33:16","comment":"\\u0413\\u043b\\u0430\\u0434\\u043a\\u043e\\u0432","exportId":33,"department":{"id":16,"name":"\\u041f\\u0417\\u0423 1 \\u0410\\u0426\\u041a\\u041a \\u0426\\/\\u041c","value":16,"text":"\\u041f\\u0417\\u0423 1 \\u0410\\u0426\\u041a\\u041a \\u0426\\/\\u041c"},"departmentId":16,"totalMass":17.77,"totalPrice":319.86,"weighings":[{"id":83101,"trash":0,"clogging":6,"tare":0,"brutto":18.9,"metal":{"id":16,"name":"3\\u0410","def":true,"psaid":3},"metalId":16,"mass":17.77,"price":"18.00","totalPrice":319.86,"newPrice":"18.00","calculatedMass":"319.86"}],"customer":48,"totalPaidAmount":319.86,"hasBeenPaid":true,"oldCustomer":48,"uuid":"98a6963b608a4471d1234130d89b301e","summary":{"3":{"weight":17.77,"cost":319.86,"median":18,"psaid":3}}}
                """;
        var parser = new JSONParser();
        var parced = (JSONObject)parser.parse(json);
        assertNotEquals(null, parced);
        System.out.println(parced.get("comment"));
    }
}