package util.DB;

import fr.roland.DB.Executor;
import util.JSON.ParcedJSON;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ProductionUPDATE {
    public Executor exec;
    public boolean Production;
    public ProductionUPDATE() throws SQLException {

    };
    public void init() throws SQLException {
        System.out.println("\n\n\n\nWARNING!!!!    PRODUCTION MODE!!!");
        exec  = new Executor("jdbc:mysql://localhost:3306/avs", "avs", "evbhPoU5JkW9fZyX") ;;
    }
    public String getMetalID(String metal) throws SQLException {

        PreparedStatement pst = exec.getConn().prepareStatement("SELECT * FROM metal where name=?");
        pst.setString(1, metal);
        ResultSet metals = pst.executeQuery();
        if  (metals.next())
           return String.valueOf(metals.getObject("id"));
        return "-1";
    };

    public int getId(ParcedJSON json) throws SQLException {
        System.out.println("INSIDE gitID");
        ArrayList arr = new ArrayList();
        arr.add(json.Waybill_number);
        arr.add(json.Date);
        arr.add(json.Time);
        var r = exec.executePreparedSelect("SELECT * FROM weighings WHERE waybill = ? AND  date =? and time = ?", arr);
        System.out.println("EXECUTE SELECT");
        if (r.next())
            return (int) r.getObject("id");
        return -1;
    }

    public int getCount(ParcedJSON json) throws SQLException {
        System.out.println("INSIDE gitID");
        ArrayList arr = new ArrayList();
        arr.add(json.Waybill_number);
        var r = exec.executePreparedSelect("SELECT * FROM weighings WHERE waybill = ?", arr);
        System.out.println("EXECUTE SELECT.... COUNTING ITEMS");
        var counter = 0;
        while (r.next())
            counter++;
        return counter;
    }


    public void updateComment(ParcedJSON json) throws SQLException {
        if (!Production)
            return;
        var id = getId(json);
        PreparedStatement stmt = exec.getConn().prepareStatement("UPDATE weighings set comment = ?  WHERE id = ?");
        stmt.setString(1, json.Comment);
        stmt.setLong(2, id);
        stmt.executeUpdate();
    };

    public void productiondelete(ParcedJSON json, ParcedJSON initial) throws SQLException {
        PreparedStatement stmt = exec.getConn().prepareStatement("DELETE from weighing_items  WHERE weighing_id=? AND trash = ? AND clogging=? AND tare =? AND brutto =? AND metal_id=?");// metal_id =

        System.out.println("DELETING ITEM USING SQL::>>");
        System.out.println("DELETE from weighing_items  WHERE weighing_id=<"+getId(json) +">  AND trash =<"+initial.Trash +">  AND clogging= <"+initial.Clogging +"> AND tare =<"+ initial.Tara +"> AND brutto ="+ initial.Brutto+" AND metal_id=<"+ String.valueOf(getMetalID(initial.Metall))+">");
        stmt.setInt(1,  getId(json));

        System.out.println("initial trash:"+ initial.Trash);
        stmt.setBigDecimal(2, new BigDecimal(initial.Trash));

        System.out.println("initial clogging:"+ initial.Clogging);
        stmt.setBigDecimal(3, new BigDecimal(initial.Clogging));

        System.out.println("initial tare:"+ initial.Tara);
        stmt.setBigDecimal(4, new BigDecimal(initial.Tara));

        System.out.println("initial brutto:"+ initial.Brutto);
        stmt.setBigDecimal(5, new BigDecimal(initial.Brutto));

        System.out.println("initial metal_id:"+ initial.Metall);
        stmt.setString(6, String.valueOf(getMetalID(initial.Metall)));
        stmt.executeUpdate();
        if (getCount(initial)<2) {
            var stmt2 = exec.getConn().prepareStatement("DELETE from weighings WHERE id = ?");
            stmt.setLong(1, getId(initial));

            stmt.executeUpdate();
        }

    };

    public void updateweighing_items(ParcedJSON json, ParcedJSON initial) throws SQLException {
        if (!Production)
            return;
        var id = getId(json);
        PreparedStatement stmt = exec.getConn().prepareStatement("update weighing_items set trash = ?, clogging=?, tare =?, brutto =?, metal_id=?  WHERE weighing_id=? AND trash = ? AND clogging=? AND tare =? AND brutto =? AND metal_id=?");// metal_id =
        System.out.println("TRASH:"+ json.Trash);
        stmt.setBigDecimal(1,new BigDecimal(json.Trash) );

        System.out.println("clogging:"+ json.Clogging);
        stmt.setBigDecimal(2, new BigDecimal(json.Clogging) );

        System.out.println("Tara:"+ json.Tara);
        stmt.setBigDecimal(3, new BigDecimal(json.Tara));

        System.out.println("Brutto:"+ json.Brutto);
        stmt.setBigDecimal(4, new BigDecimal(json.Brutto) );

        System.out.println("Metall:"+ getMetalID(json.Metall));
         stmt.setString(5, String.valueOf(getMetalID(json.Metall)));

        System.out.println("id:"+ id);
        stmt.setInt(6, id);

        System.out.println("initial trash:"+ initial.Trash);
        stmt.setBigDecimal(7, new BigDecimal(initial.Trash));

        System.out.println("initial clogging:"+ initial.Clogging);
        stmt.setBigDecimal(8, new BigDecimal(initial.Clogging));

        System.out.println("initial tare:"+ initial.Tara);
        stmt.setBigDecimal(9, new BigDecimal(initial.Tara));

        System.out.println("initial brutto:"+ initial.Brutto);
        stmt.setBigDecimal(10, new BigDecimal(initial.Brutto));

        System.out.println("initial metal_id:"+ initial.Metall);
        stmt.setString(11, String.valueOf(getMetalID(initial.Metall)));
        stmt.executeUpdate();
        updateComment(json);

    };

    public void fullupdate(ParcedJSON json, ParcedJSON initial) throws SQLException {
        if (!Production)
            return;
        if (json.Brutto.equals("0.00")){
            productiondelete(json, initial);
            return;
        }
        updateComment(json);
        updateweighing_items(json, initial);

    }

    //    UPDATE weighings set comment = 'TEST'  WHERE id = '34';//

    //SELECT * FROM `weighings` WHERE waybill = '?' AND id ='?' AND date ='?' and time = '?';
    //update weighing_items set trash = '?', clogging='?', tare ='?', brutto ='?', metal_id = '?' WHERE weighing_id='ID/';
    public static void main(String[] args) throws SQLException {
        System.out.println("METALL!! UPDATE");
        var pr = new ProductionUPDATE();
        var json = new ParcedJSON();
        json.Waybill_number = "1";
        json.Time="11:04:45";
        json.Date="2019-08-12";
        json.Comment="тест";
        json.Metall="АКБ ПП залитые";
        json.Tara="12.00";
        json.Trash="0.30";
        json.Brutto="33.80";
        json.Clogging="6.00";
        System.out.println("EXTRACTED ID=>"+pr.getId(json));
        pr.updateweighing_items(json, json);
      //  pr.directupdate();
    }

};
