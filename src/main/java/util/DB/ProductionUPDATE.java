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

    public void updateComment(ParcedJSON json) throws SQLException {
        if (!Production)
            return;
        var id = getId(json);
        PreparedStatement stmt = exec.getConn().prepareStatement("UPDATE weighings set comment = ?  WHERE id = ?");
        stmt.setString(1, json.Comment);
        stmt.setLong(2, id);
        stmt.executeUpdate();
    };

    public void updateweighing_items(ParcedJSON json) throws SQLException {
        if (!Production)
            return;
        var id = getId(json);
        PreparedStatement stmt = exec.getConn().prepareStatement("update weighing_items set trash = ?, clogging=?, tare =?, brutto =?, metal_id=?  WHERE weighing_id=?");// metal_id =
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

        stmt.executeUpdate();
    };

    public void fullupdate(ParcedJSON json) throws SQLException {
        if (!Production)
            return;
        updateComment(json);
        updateweighing_items(json);
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
        pr.updateweighing_items(json);
      //  pr.directupdate();
    }

};
