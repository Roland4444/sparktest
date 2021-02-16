package servers;

import Message.abstractions.BinaryMessage;
import abstractions.Cypher;
import abstractions.RequestMessage;
import abstractions.ResponceMessage;
import impl.JAktor;
import org.json.simple.parser.ParseException;
import servers.threadMessager.ThreadMessager;
import util.JSON.Beatyfulizer;
import util.IDHelper;
import util.processors.InputRequestProcessor;

import java.io.*;
import java.sql.SQLException;


public class ServerAktor extends JAktor {
    public String incomingFolder;
    public Cypher cypher;
  //  public J11Client clientj11=new J11Client();
    public Class<EchoWebSocket> echoWebSocket;
    public InputRequestProcessor irp;
    public void setCypher(Cypher cypher) {
        this.cypher = cypher;
    }
    public ThreadMessager msg ;
    public AsyncSend async = new AsyncSend() {
        @Override
        public void asyncSend(ResponceMessage resp) throws IOException {
            String address = IDHelper.getaddress(incomingFolder, resp.ID);
            System.out.println("SENDING responce to"+address);
            send(BinaryMessage.savedToBLOB(resp), "http://127.0.1.1:12215");//address);
        }
    };

/*
    public int sendj11(byte[] message, String address) {
        System.out.println("SENDING to>>"+address+"     ::@J11Client");
        try {
          //  return clientj11.send(this.cypher.encrypt(message), address);
        } catch (ConnectException e) {
            System.out.println("SHIT HAPPENS!!"+e);
            return -1;
        } catch (IOException e) {
            System.out.println("SHIT HAPPENS!!"+e);
            return -2;
        }
    }
*/
    @Override
    public int send(byte[] message, String address) throws IOException {
        //System.out.println("SENDING to>>"+address);
        return this.client.send(this.cypher.encrypt(message), address);
      //  return sendj11(message, address);
    }

    @Override
    public void receive(byte[] message_) throws IOException {
        System.out.println("Catched!!!");
        byte[] message = cypher.decrypt(message_);
        FileOutputStream fos = new FileOutputStream("DUMP.INPUT");
        fos.write(message_);
        fos.close();

        RequestMessage req = (RequestMessage) BinaryMessage.restored(message);

        if (req.type.equals(RequestMessage.Type.request)) {
            saveRequest(req);

            try {
                saveinDB(req);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                sendWebsocketAlerts(req.ID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return;
        }
        if (req.type.equals(RequestMessage.Type.update)) {
            try {
               irp.saveUpdatingRequestinDB(req);
            } catch (SQLException | ParseException throwables) {
                throwables.printStackTrace();
            }
            try {
                sendWebsocketAlerts(req.ID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return;
        }
        if (req.type.equals(RequestMessage.Type.ask)) {
            irp.processAsk(req);
            return;
        }


    }

    private void sendWebsocketAlerts(String id) throws SQLException {
        EchoWebSocket.sendall(id);
    }



    public void saveRequest(RequestMessage req) throws IOException {
        System.out.println("Save request");
        FileWriter fw = new FileWriter(incomingFolder+"/"+req.ID, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(req.addressToReply);
        bw.newLine();
        bw.close();
    }

    public void sendResponce(ResponceMessage res) {
        String address = null;
        try {
            address = IDHelper.getaddress(incomingFolder, res.ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("INITIAL ADDRESS\n"+address);
        String address_ = Beatyfulizer.trimAddress(address);
        System.out.println("SENDING responce to\n"+address);
        System.out.println("address  "+address);
        System.out.println("address_ "+address_);
        try {
            send(BinaryMessage.savedToBLOB(res), address);
        } catch (IOException e) {
            System.out.println("SHIT HAPPENS"+e);
        }
    };


    public void saveinDB(RequestMessage requestMessage) throws SQLException {
        irp.saveRequestinDB(requestMessage);

    };

}