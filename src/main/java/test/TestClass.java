package test;

import fr.roland.DB.Executor;
import java.sql.*;
import java.util.ArrayList;
import java.util.function.Function;

public class TestClass {
    public static void main(String[] args) throws SQLException {
        System.out.println("UU");
       // DataBaseHelper dbh = new DataBaseHelper("jdbc:mysql://localhost/avs?user=avs&password=evbhPoU5JkW9fZyX", true);
        Executor exec  = new Executor();//("jdbc:mysql://localhost:3306/avs", "avs", "evbhPoU5JkW9fZyX") ;
     //   update weighing_items set trash = '?', clogging='?', tare ='?', brutto ='?', metal_id = '?' WHERE weighing_id='ID/';
    //    UPDATE weighings set comment = 'TEST'  WHERE id = '34';//
        ArrayList data = new ArrayList<>();
        data.add("АКБ ПП залитые");
  //      SELECT id FROM `weighings` WHERE waybill = '1' AND date ='2019-08-12' and time = '11:04:45'
        // SELECT * FROM `weighings` WHERE waybill = '?' AND id ='?' AND date ='?' and time = '?';
        ResultSet metals = exec.executePreparedSelect("SELECT * FROM metal where name=?", data);
        while (metals.next()){
            System.out.println(String.valueOf(metals.getObject("id")) + " :::  "+ String.valueOf(metals.getObject("name")));
        }
    }
    public static Integer compute(Function<Integer, Integer> function, Integer value) {
        return function.apply(value);
    }



    public static Integer sum(Integer integer, Integer integer2) {
        return integer+integer2;
    }
}
