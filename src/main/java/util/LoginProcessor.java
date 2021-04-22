package util;
import fr.roland.DB.Executor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginProcessor {
    private Executor executor;
    public LoginProcessor(Executor executor) throws SQLException {
        this.executor = executor;
    }
    public LoginProcessor() throws SQLException {

    }

    public void updatepass(String login, String password) throws SQLException {
        PreparedStatement pstm = executor.getConn().prepareStatement("UPDATE users set pass=? where login = ?");
        System.out.println("UPDATING PASSWORD ...@user"+login+'\n');
        pstm.setObject(1, password);
        pstm.setObject(2, login);
        pstm.executeUpdate();
    }
    public String test() throws SQLException {
        var result  = executor.submit("SELECT * FROM users");
        if (result.next())
            return result.getString(1);
        return "hi";
    }

    public boolean checklogin(String login, String password) throws SQLException {
        String pass="____";
        ArrayList list = new ArrayList<String>() {{
            add(login);
        }};

        ResultSet  res = executor.executePreparedSelect("SELECT * from users where login = ?", list);
        if (res.next())
            pass =res.getString("pass");
        if (password.equals(pass))
            return true;
        return false;
    }
    public boolean check(String password, String encoded){
  ///      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  ///      return encoder.matches(password, encoded);
        return true;

    }

}
