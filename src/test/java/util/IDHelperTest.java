package util;

import org.junit.Test;
import util.DB.DataBaseHelper;


import java.sql.SQLException;

import static org.junit.Assert.*;

public class IDHelperTest {
    DataBaseHelper db =  new DataBaseHelper("requests");
    IDHelper idh = new IDHelper(db.executor);

    public IDHelperTest() throws SQLException {
    }

//    @Test
    public void getIDusingsimpleID() throws SQLException {
        String etalon ="2020-11-0213:43:42татьяна";
        String input = "1";
        assertEquals(etalon, idh.getIDusingsimpleID(input));
    }
}