package util.DB;

import abstractions.ClientType;
import fr.roland.DB.Executor;

import java.sql.SQLException;
import java.util.ArrayList;

public class ClientFinder {
    public Executor exec;
    public final String delimiter = "#";
    public ClientFinder(String url, String login, String pass) throws SQLException {
        System.out.println("CLIENT SEARCHER REFACTORED");
        exec  = new Executor(url, login, pass) ;
    };

    public ClientType getType(String input) throws SQLException {
        ArrayList param = new ArrayList();
        param.add("%"+input+"%");
        var res = exec.executePreparedSelect("SELECT `inn` FROM `psa`.`company` WHERE `name` LIKE ?;",param);
        if (res.next())
            return ClientType.Company;
        return ClientType.Person;
    };
    public void updateInfoPSAaboutClient(String input) throws SQLException {
        switch (getType(input)){
            case Person -> updatePerson(input);
            case Company -> updateCompany(input);
        }

    }

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

    public void updateCompany(String input) {

    }

    public String getcompanyIDfrompartial(String input) throws SQLException {
        ArrayList param = new ArrayList();
        param.add("%"+input+"%");
        var res = exec.executePreparedSelect("SELECT `id` FROM `psa`.`company` WHERE `inn` LIKE ?;",param);
        if (res.next())

    };

    public String getClientNameAndID(String input) throws SQLException {
        ArrayList param = new ArrayList();
        param.add("%"+input+"%");
        var res = exec.executePreparedSelect("SELECT `name` FROM `psa`.`company` WHERE `inn` LIKE ?;",param);
        if (res.next())
            return res.getString(1);
        res = exec.executePreparedSelect("SELECT * FROM `psa`.`passport` WHERE `series` LIKE ? AND `number` LIKE ?;",processPassportField(input, 4, false));   ///process russian passport
        if (res.next())
            return res.getString("fname")+delimiter+res.getString("mname")+delimiter+res.getString("lname");
        res = exec.executePreparedSelect("SELECT * FROM `psa`.`passport` WHERE `series` LIKE ? AND `number` LIKE ?;", processPassportField(input, 2, true));    ///process another
        if (res.next())
            return res.getString("fname")+delimiter+res.getString("mname")+delimiter+res.getString("lname");
        res = exec.executePreparedSelect("SELECT * FROM `psa`.`passport` WHERE `series` LIKE ? AND `number` LIKE ?;", processPassportField(input, 3, true));
        if (res.next())
            return res.getString("fname")+delimiter+res.getString("mname")+delimiter+res.getString("lname");
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
