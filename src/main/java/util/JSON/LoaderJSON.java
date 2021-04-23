package util.JSON;

import abstractions.RequestMessage;
import fr.roland.DB.Executor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoaderJSON {
    private Executor executor;
    public LoaderJSON(Executor executor){
        this.executor = executor;
    }
    public ArrayList<Object> loaderJSON() throws SQLException {
        ResultSet res = executor.submit("select * from test;");
        ArrayList result = new ArrayList();
        while (res.next()){
            result .add(res.getObject(2));
        }
        return result;
    };

    public String loadAll2JSON() throws SQLException {
        ArrayList param = new ArrayList();
         /// ResultSet select = executor.submit("select * from requests ORDER BY counter;" );
        //     JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        int i = 0;
        for (int j=0; j<30; j++) {
            ResultSet select = executor.submit("select * from requests ORDER BY counter;");

            while (select.next())  {
                JSONObject rowJSON = new JSONObject();
                rowJSON.put("id", ++i);//select.getObject("counter"));
                rowJSON.put("datetimerequest", "'" + select.getObject("datetimerequest") + "'");
                rowJSON.put("initialdata", select.getObject("initialdata"));
                rowJSON.put("datetimeupdate", "'" + select.getObject("datetimeupdate") + "'");
                rowJSON.put("updateddata", select.getObject("updateddata"));
                rowJSON.put("datetimeapprove", "'" + select.getObject("datetimeapprove") + "'");
                rowJSON.put("comment", select.getObject("comment"));
                list.add(rowJSON);
            }
            ;
        }
        return list.toJSONString();
    };

    public static  String loadPzu(RequestMessage req) throws ParseException {
        Object obj = new JSONParser().parse(req.JSONed);
        JSONObject jo = (JSONObject) obj;
        return (String) jo.get("pzu");
    };
    public String LoadResult2JSON(String result) throws SQLException {
        ArrayList param = new ArrayList();
        param.add(result);
        ResultSet select = executor.executePreparedSelect("select * from requests where id = ?",param);
   //     JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        while (select.next()){
            JSONObject rowJSON = new JSONObject();
            rowJSON.put("id", select.getObject("counter"));
            rowJSON.put("datetimerequest", "'"+select.getObject("datetimerequest")+"'");
            rowJSON.put("initialdata", select.getObject("initialdata"));
            rowJSON.put("datetimeupdate", "'"+select.getObject("datetimeupdate")+"'");
            rowJSON.put("updateddata", select.getObject("updateddata"));
            rowJSON.put("datetimeapprove", "'"+select.getObject("datetimeapprove")+"'");
            rowJSON.put("comment", select.getObject("comment"));
            list.add(rowJSON);
        }
        return list.toJSONString();
    };

    public String LoadResult2JSONOne(String result) throws SQLException {
        ArrayList param = new ArrayList();
        param.add(result);
        ResultSet select = executor.executePreparedSelect("select * from requests where id = ?",param);
        //     JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        if (select.next()){
            JSONObject rowJSON = new JSONObject();
            rowJSON.put("id", select.getObject("counter"));
            rowJSON.put("datetimerequest", "'"+select.getObject("datetimerequest")+"'");
            rowJSON.put("initialdata", select.getObject("initialdata"));
            rowJSON.put("datetimeupdate", "'"+select.getObject("datetimeupdate")+"'");
            rowJSON.put("updateddata", select.getObject("updateddata"));
            rowJSON.put("datetimeapprove", "'"+select.getObject("datetimeapprove")+"'");
            rowJSON.put("comment", select.getObject("comment"));
            list.add(rowJSON);
        }
        return list.toJSONString();
    };

}
