package util;

import abstractions.RequestMessage;
import abstractions.ResponceMessage;
import fr.roland.DB.Executor;
import servers.ServerAktor;
import util.readfile.Readfile;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class OutputResponceProcessor {
    public Executor executor;
    public ServerAktor jaktor;
    public Readfile Incomming;
    public IDHelper idHelper;
    public void approve(String ID) throws IOException, SQLException {
        System.out.println("ID=>>>"+ID);
        ResponceMessage res = new ResponceMessage();
        res.ID = idHelper.getIDusingsimpleID(ID);
        res.approved = true;
        jaktor.sendResponce(res);
        updateDateApprove(res.ID);
    };

    public void updateDateApprove(String ID) throws SQLException {
       PreparedStatement stmt = executor.getConn().prepareStatement("UPDATE requests SET datetimeapprove = ? WHERE id = ?");
       stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
       stmt.setString(2, ID);
       stmt.executeUpdate();
   } ;

}