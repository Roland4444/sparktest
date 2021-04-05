package util.DB;

import abstractions.ClientType;
import fr.roland.DB.Executor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PSAClient {
    public Executor exec;
    public final String delimiter = "#";
    public final String info_start = "::{";
    public final String info_finish = "}.";

    public PSAClient(String url, String login, String pass) throws SQLException {
        System.out.println("CLIENT SEARCHER REFACTORED");
        exec  = new Executor(url, login, pass) ;
    };

    public void updateclient(String name, String psanumber, String idclient, String type) throws SQLException {
        System.out.println("name=="+name);
        System.out.println("psanumber=="+psanumber);
        System.out.println("idclient=="+idclient);
        System.out.println("type=="+type);

        switch (type){
            case "P"-> updateClient(psanumber, name , idclient);
            case "C"-> updateCompany(psanumber, name, idclient);
        };
        return;


    }

    private void updateCompany(String psanumber, String name , String idclient) throws SQLException {
        PreparedStatement stmt = exec.getConn().prepareStatement("UPDATE psa set company_id = ?, client = ?   WHERE id = ?");
        stmt.setString(1, idclient);
        stmt.setString(2, name);
        stmt.setString(3, psanumber);
        System.out.println(stmt);
        stmt.executeUpdate();
    }

    private void updateClient(String psanumber, String name, String idclient) throws SQLException {
        PreparedStatement stmt = exec.getConn().prepareStatement("UPDATE psa set passport_id = ?, client = ?   WHERE id = ?");
        stmt.setString(1, idclient);
        stmt.setString(2, name);
        stmt.setString(3, psanumber);
        System.out.println(stmt);
        stmt.executeUpdate();
    }

    public ClientType getType(String input) throws SQLException {
        ArrayList param = new ArrayList();
        param.add("%"+input+"%");
        var res = exec.executePreparedSelect("SELECT `inn` FROM `psa`.`company` WHERE `name` LIKE ?;",param);
        if (res.next())
            return ClientType.Company;
        return ClientType.Person;
    };


    public String clientId(ClientType type, String name){
        ArrayList param = new ArrayList();
        return "";
    //    param.add("%"+input+"%");
      //  var res = exec.executePreparedSelect("SELECT `passport_id` FROM `psa`.`company` WHERE `inn` LIKE ?;",param);
    //    if (res.next())
    }

    public void updatePerson(String input) {
        ArrayList param = new ArrayList();
        param.add("%"+input+"%");
     //   var res = exec.executePreparedSelect("SELECT `name` FROM `psa`.`company` WHERE `inn` LIKE ?;",param);
       // if (res.next())
     //   var passportId =
    }

    public String processCOmpanyRequest(ResultSet res) throws SQLException {
        return res.getString("name")+info_start+"'C':"+"'"+res.getString("id")+"'"+info_finish;

    };

    public String processPassportRequest(ResultSet res) throws SQLException {
        return res.getString("fname")+delimiter+res.getString("mname")+delimiter+res.getString("lname")+info_start+"'P':"+"'"+res.getString("id")+"'"+info_finish;
    };



    public String getClientNameAndID(String input) throws SQLException {
        ArrayList param = new ArrayList();
        param.add("%"+input+"%");
        var res = exec.executePreparedSelect("SELECT * FROM `psa`.`company` WHERE `inn` LIKE ?;",param);
        if (res.next())
            return processCOmpanyRequest(res);
        res = exec.executePreparedSelect("SELECT * FROM `psa`.`passport` WHERE `series` LIKE ? AND `number` LIKE ?;",processPassportField(input, 4, false));
        if (res.next())
            return processPassportRequest(res);
        res = exec.executePreparedSelect("SELECT * FROM `psa`.`passport` WHERE `series` LIKE ? AND `number` LIKE ?;", processPassportField(input, 2, true));
        if (res.next())
            return processPassportRequest(res);
        res = exec.executePreparedSelect("SELECT * FROM `psa`.`passport` WHERE `series` LIKE ? AND `number` LIKE ?;", processPassportField(input, 3, true));
        if (res.next())
            return processPassportRequest(res);
        return "";
    };

    ArrayList processPassportField(String input, int seriesLength, boolean ignoreDigits){
        var res = new ArrayList<>();
        StringBuilder sb_series = new StringBuilder();
        StringBuilder sb_number = new StringBuilder();
        int seriescounter = 0;
        for (int i=1; i<=input.length(); i++){
            if ((checkdigit(input.charAt(i-1))||ignoreDigits) && (seriescounter <seriesLength)) {
                seriescounter++;
                sb_series.append(input.charAt(i - 1));
            }
            else
                sb_number.append(input.charAt(i-1));
        }
        res.add("%"+sb_series.toString()+"%");
        res.add("%"+sb_number.toString()+"%");
        System.out.println("SERIES::>"+sb_series.toString());
        System.out.println("number::>"+sb_number.toString());
        return res;
    };

    boolean checkdigit(char input){
        var result = false;
        result = switch (input){
            case  '0' -> true;
            case  '1' -> true;
            case  '2' -> true;
            case  '3' -> true;
            case  '4' -> true;
            case  '5' -> true;
            case  '6' -> true;
            case  '7' -> true;
            case  '8' -> true;
            case  '9' -> true;
            default -> false;
        };
        return result;
    }
}
