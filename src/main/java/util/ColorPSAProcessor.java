package util;

import fr.roland.DB.Executor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ColorPSAProcessor {
    public Map<String,String> DepsMap = new HashMap();
    public Executor executor;
    timeBasedUUID timeBasedUUID = new timeBasedUUID();

    public ColorPSAProcessor(){
        initMap();
    }

    public void initMap(){
        DepsMap.put("6", "1");
        DepsMap.put("16", "1");
        DepsMap.put("10", "2");
        DepsMap.put("9", "25");
    }
    public void processColor(String inputJSON) throws ParseException, SQLException {
        var parser = new JSONParser();
        JSONObject js = (JSONObject) parser.parse(inputJSON);
        var inputdepID = js.get("departmentId");
        var realdepID = DepsMap.get(inputdepID);
        String UUID = timeBasedUUID.generate();
        JSONArray vagning = (JSONArray) js.get("weighings");
        while (vagning.iterator().hasNext()){
            var invagning = vagning.iterator().next();
            processinvagning(realdepID, UUID);
        }

    }

    public void processinvagning(String depsId, String guuid) throws SQLException {
        var prepared = executor.getConn().prepareStatement("""
INSERT INTO `weighing` 
(id`,`brutto`,`tare`,`sor`,`price`,`psa_id`,`metal_id`,`client_tare`,`client_sor`,`client_price`,`inspection`,`timestamp`, `uuid`)
                                VALUES
(NULL,     ?,   ?,    ?,     ?,       ?,        ?,          ?,           ?,            ?,            ?,             ?,        ?);
                                
                """  );
       // prepared.setString();

    }
}
