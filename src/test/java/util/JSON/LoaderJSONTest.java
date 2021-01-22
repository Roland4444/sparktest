package util.JSON;

import Message.abstractions.BinaryMessage;
import org.eclipse.jetty.util.Loader;
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
}