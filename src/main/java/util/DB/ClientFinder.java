package util.DB;

import fr.roland.DB.Executor;

import java.sql.SQLException;

public class ClientFinder {
    public Executor exec;
    public ClientFinder(String url, String login, String pass) throws SQLException {
        exec  = new Executor(url, login, pass) ;
    };
    public String getClientName(String input){


        var res = exec.executePreparedSelect(                "SELECT `name` FROM `table` WHERE `inn` LIKE '%?%;");


        return "";
    };
}
