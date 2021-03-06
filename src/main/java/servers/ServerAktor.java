package servers;

import Message.abstractions.BinaryMessage;
import abstractions.Cypher;
import abstractions.OnRequest;
import abstractions.RequestMessage;
import abstractions.ResponceMessage;
import impl.JAktor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.ParseException;
import servers.threadMessager.ThreadMessager;
import util.JSON.Beatyfulizer;
import util.IDHelper;
import util.processors.InputRequestProcessor;

import java.io.*;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
    public OnRequest onRequest;
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

    public static String sendPost(String serie, String number, String url) throws Exception {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("serie", serie));
        urlParameters.add(new BasicNameValuePair("number", number));
        String responce = "";
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post)) {
            responce = EntityUtils.toString(response.getEntity());
            System.out.println(responce);
        }
        return responce;
    }

    public static String sendPosttest(String data, String uuid, String url) throws Exception {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("data", data));
        urlParameters.add(new BasicNameValuePair("uuid", uuid));
        urlParameters.add(new BasicNameValuePair("summery", "435543543543"));
        String responce = "";
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            responce = EntityUtils.toString(response.getEntity());
            System.out.println(responce);
        }
        return responce;
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
                onRequest.action(req);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                saveinDB(req);
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


    public void saveinDB(RequestMessage requestMessage) throws SQLException, ParseException {
        irp.saveRequestinDB(requestMessage);

    };

}