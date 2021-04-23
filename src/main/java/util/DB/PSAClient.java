package util.DB;

import abstractions.ClientType;
import fr.roland.DB.Executor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PSAClient {
    public Executor exec;
    public final String delimiter = "#";
    public final String info_start = "::{";
    public final String info_finish = "}.";
    public String passcheckurl_ ="";
    public final String  effect_atom = "является действующим";
    public final String uneffect_atom = "ЯВЛЯЕТСЯ НЕДЕЙСТВИТЕЛЬНЫМ";
    public PSAClient(String url, String login, String pass, String passcheckurl_) throws SQLException {
        System.out.println("CLIENT SEARCHER REFACTORED");
        exec  = new Executor(url, login, pass) ;
        this.passcheckurl_ = passcheckurl_;
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
        PreparedStatement stmt = exec.getConn().prepareStatement("UPDATE psa set company_id = ?, client = ?   WHERE uuid = ?");
        stmt.setString(1, idclient);
        stmt.setString(2, name);
        stmt.setString(3, psanumber);
        System.out.println(stmt);
        stmt.executeUpdate();
    }

    private void updateClient(String psanumber, String name, String idclient) throws SQLException {
        PreparedStatement stmt = exec.getConn().prepareStatement("UPDATE psa set passport_id = ?, client = ?   WHERE uuid = ?");
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

    public String processPassportRequest(ResultSet res) throws SQLException, IOException {
        String pass_serie = res.getString("series");
        String pass_number = res.getString("number");
        String type = "P";
        if (checkpass(pass_serie+pass_number))
        {
            type = "P";
        }
        else
            type = "B";
        return res.getString("fname")+delimiter+res.getString("mname")+delimiter+res.getString("lname")+info_start+"'"+type+"':"+"'"+res.getString("id")+"'"+info_finish;
    };



    public String getClientNameAndID(String input) throws SQLException, IOException {
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

    public static String sendPost(String serie, String number, String url) throws IOException {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("serie", serie));
        urlParameters.add(new BasicNameValuePair("number", number));
        String resp = "";
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            resp = EntityUtils.toString(response.getEntity());
        }
        return resp;
    }

    public String getStatusText(String input){
        var first = "\"StatusText\":";
        var index = input.indexOf(first);
        if (index <1)
            return "";
        return input.substring(index+first.length()+1, input.length()-3 );
    }

    public Boolean checkpass(String passport) throws IOException {
        var series = passport.substring(0,4);
        var number = passport.substring(4, passport.length());
        System.out.println("series "+series + "number"+ number);
        var answer = sendPost(series  , number, passcheckurl_);
        var status = getStatusText(answer);
        System.out.println("STATUS "+status);
        switch (status){
            case effect_atom -> {
                return true;
            }
            case uneffect_atom -> {
                return false;
            }
        }
        return false;
    }




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
