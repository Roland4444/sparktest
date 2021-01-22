package util;


import fr.roland.DB.Executor;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IDHelper {
    public Executor executor;
    public IDHelper(){

    };
    public IDHelper(Executor executor){
        this.executor = executor;
    };
    public String getIDusingsimpleID(String ID) throws SQLException {
        System.out.println("getIDusingsimpleID::ID:: "+ID);
        ArrayList arr = new ArrayList();
        arr.add(Integer.valueOf(ID));
        ResultSet res = executor.executePreparedSelect("SELECT * FROM requests where counter=?", arr);
        if (res.next())
            return (String) res.getObject("id");
        return null;
    };

    public static  String getaddress(String incomingFolder, String ID) throws IOException {
        System.out.println("Try reading::>"+incomingFolder + "/" + ID);

        FileReader fr = new FileReader(incomingFolder + "/" + ID);
        char[] a = new char[200];
        fr.read(a);
        fr.close();
        return String.valueOf(a);
    }
}
