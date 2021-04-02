package util.DB;

import fr.roland.DB.Executor;

import java.sql.SQLException;
import java.util.ArrayList;

public class ClientFinder {
    public Executor exec;
    public ClientFinder(String url, String login, String pass) throws SQLException {
        exec  = new Executor(url, login, pass) ;
    };
    public String getCompanyID(String input){
        return "";
    };
    public String getClientName(String input) throws SQLException {
        ArrayList param = new ArrayList();
/////////process coimpany
        param.add("%"+input+"%");
        var res = exec.executePreparedSelect("SELECT `name` FROM `psa`.`company` WHERE `inn` LIKE ?;",param);
        if (res.next())
            return res.getString(1);
/////////process russian poassport
        res = exec.executePreparedSelect("SELECT * FROM `psa`.`passport` WHERE `series` LIKE ? AND `number` LIKE ?;",processPassportField(input, 4, false));//, `number` LIKE ?
        if (res.next())
            return res.getString("fname")+" "+res.getString("mname")+" "+res.getString("lname");
///////process another
        res = exec.executePreparedSelect("SELECT * FROM `psa`.`passport` WHERE `series` LIKE ? AND `number` LIKE ?;", processPassportField(input, 2, true));//, `number` LIKE ?
        if (res.next())
            return res.getString("fname")+" "+res.getString("mname")+" "+res.getString("lname");

        res = exec.executePreparedSelect("SELECT * FROM `psa`.`passport` WHERE `series` LIKE ? AND `number` LIKE ?;",        processPassportField(input, 3, true));//, `number` LIKE ?
        if (res.next())
            return res.getString("fname")+" "+res.getString("mname")+" "+res.getString("lname");
        return "";
    };

    ArrayList processPassportField(String input, int seriesLength, boolean ignoreDigits){
        var res = new ArrayList<>();
        var sb_series =  new StringBuilder();
        var sb_number = new StringBuilder();
        int seriescounter = 0;
        for (int i=1; i<=input.length(); i++){
            if ((checkdigit(input.charAt(i-1))||ignoreDigits) && (++seriescounter <seriesLength))
                sb_series.append(input.charAt(i-1));
            else
                sb_number.append(input.charAt(i-1));
        }
        res.add(sb_series.toString());
        res.add(sb_number.toString());
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
