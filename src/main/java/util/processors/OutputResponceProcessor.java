package util.processors;

import Message.abstractions.BinaryMessage;
import abstractions.Condition;
import abstractions.PendingResponces;
import abstractions.ResponceMessage;
import fr.roland.DB.Executor;
import servers.ServerAktor;
import util.Deps;
import util.IDHelper;
import util.readfile.Readfile;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static Message.abstractions.BinaryMessage.*;

public class OutputResponceProcessor {
    public String incomingFolder;
    public Executor executor;
    public ServerAktor jaktor;
    public Readfile Incomming;
    public IDHelper idHelper;
    public void approve(String ID)  {
        System.out.println("ID=>>>"+ID);
        ResponceMessage res = new ResponceMessage();
        try {
            res.ID = idHelper.getIDusingsimpleID(ID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        res.approved = true;
        try {
            System.out.println("address>>>>\n\n\n"+IDHelper.getaddress(incomingFolder, res.ID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writeResponceinDB(res, IDHelper.getaddress(incomingFolder, res.ID));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // jaktor.sendResponce(res);
        try {
            updateDateApprove(res.ID);
            System.out.println("UPDATE date addrove");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            saveStatus(Deps.PendingResponcesFile, res.ID, Condition.APPROVED);
        //    jaktor.async.asyncSend(res);
            System.out.println("send responce");
        } catch (IOException e) {
            System.out.println("\n\n\n\nerroe when send!\n\n\n");
        }
        //jaktor.async.sendAsyncResp(res);

    };

    public void saveStatus(String FileName, String Id, Condition cond) throws IOException {
        if (!new File(FileName).exists()){
            BinaryMessage.write(BinaryMessage.savedToBLOB( new PendingResponces(new HashMap<>())), FileName);
        }
        PendingResponces pend = (PendingResponces) restored(readBytes(FileName));
        if (pend == null){
            pend = new PendingResponces(new HashMap<>());
            BinaryMessage.write(BinaryMessage.savedToBLOB(pend), FileName);
        };
        PendingResponces pend__ = (PendingResponces) restored(readBytes(FileName));
        pend__.ReqMap.put(Id, cond);
        BinaryMessage.write(BinaryMessage.savedToBLOB(pend__), FileName);
    };

    public void writeResponceinDB(ResponceMessage res, String address) throws SQLException {
        ArrayList params = new ArrayList();
        params.add(address);
        System.out.println("\n\n\n"+address);

        PreparedStatement stmt__ = executor.getConn().prepareStatement("SELECT * FROM status where ip=?");
        stmt__.setBytes(1, address.getBytes());
        ResultSet r = stmt__.executeQuery();

        ArrayList params2=new ArrayList();
        params2.add(bool2int(res.approved));
        params2.add(address.getBytes());

        PreparedStatement update = executor.getConn().prepareStatement("UPDATE status set status=?  where ip=?::bytea");
        update.setInt(1, bool2int(res.approved));
        update.setBytes(2, address.getBytes());

        if (r.next()) {
            update.executeUpdate();
        }
        else {
            PreparedStatement stmt = executor.getConn().prepareStatement("INSERT INTO status VALUES( ? , ?) ");
            stmt.setBytes(1, address.getBytes());
            stmt.setInt(2, bool2int(res.approved));
            stmt.executeUpdate();
        }
    };

    public void printarray(ArrayList arr){
        for (int  i=0; i<arr.size(); i++){
            System.out.println(arr.get(i));
        }
    };

    int bool2int(boolean input){
        if (input) return 1;
        return 0;
    }

    public ArrayList swap(ArrayList input){
        ArrayList result = new ArrayList();
        for (int i=input.size()-1; i>=0; i--){
            result.add(input.get(i));
        };
        return  result;
    }

    public void updateDateApprove(String ID) throws SQLException {
        PreparedStatement stmt = executor.getConn().prepareStatement("UPDATE requests SET datetimeapprove = ? WHERE id = ?");
        stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
        stmt.setString(2, ID);
        stmt.executeUpdate();
    }

    public void decline(String ID) throws SQLException, IOException {
        System.out.println("EXECUTE DECLINE @"+ ID);
        System.out.println("ID=>>>"+ID);
        ResponceMessage res = new ResponceMessage();
        try {
            res.ID = idHelper.getIDusingsimpleID(ID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        res.approved = false;
        try {
            System.out.println("address>>>>\n\n\n"+IDHelper.getaddress(incomingFolder, res.ID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writeResponceinDB(res, IDHelper.getaddress(incomingFolder, res.ID));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // jaktor.sendResponce(res);
        try {
            updateDateDecline(res.ID);
            System.out.println("UPDATE date Decline");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            saveStatus(Deps.PendingResponcesFile, res.ID, Condition.DECLINED);
         ///   jaktor.async.asyncSend(res);
            System.out.println("send responce");
        } catch (IOException e) {
            System.out.println("\n\n\n\nerroe when send!\n\n\n");
        }
        //jaktor.async.sendAsyncResp(res);



    };

    public void updateDateDecline(String ID) throws SQLException {
        System.out.println("\n\n\nSetting 0 vua"+ID+"request\n\n\n\n");
        PreparedStatement stmt = executor.getConn().prepareStatement("UPDATE requests SET datetimeapprove = ?, updateddata = ?::jsonb  WHERE id = ?");
        stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
        stmt.setString(2, "{\"a\":\"0\"}");
        stmt.setString(3, ID);
        stmt.executeUpdate();
    }
}