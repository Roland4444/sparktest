package util;


import fr.roland.DB.Executor;

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
        ArrayList arr = new ArrayList();
        arr.add(Integer.valueOf(ID));
        ResultSet res = executor.executePreparedSelect("SELECT * FROM requests where counter=?", arr);
        if (res.next())
            return (String) res.getObject(1);
        return null;
    };
}