package util;
import DSLGuided.requestsx.SMS.SMSDSLProcessor;
import DSLGuided.requestsx.SMS.SendSMS;
import Message.abstractions.BinaryMessage;
import abstractions.Cypher;
import abstractions.OnRequest;
import abstractions.RequestMessage;
import org.json.simple.parser.ParseException;
import servers.EchoWebSocket;
import servers.ServerAktor;
import util.DB.DataBaseHelper;
import util.DB.ProductionUPDATE;
import util.DSLUtil.DSL;
import util.JSON.LoaderJSON;
import util.processors.InputRequestProcessor;
import util.processors.OutputResponceProcessor;
import util.react.ReactBlob;
import util.readfile.Readfile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Deps {
    public util.DSLUtil.DSL DSL;

    public final String lockProd = "prod.bin";
    public final String DSLRules = "rules.bin";
    public static String PendingResponcesFile = "rendresp.bin";
    public LoginProcessor loginchecker;
    public DataBaseHelper dbhelper;
    public Cypher cypher;
    public ServerAktor aktor;
    public InputRequestProcessor irp;
    public String fileprops = "setts.ini";
    public String binprops = "setts.bin";
    public String incomingFolder = "requests";
  //  private Readfile Settings;
    private Readfile Incomming;
    public IDHelper idh;
    public Class<EchoWebSocket> echoWebSocket;
    public OutputResponceProcessor orp;
    public DataBaseHelper requests;
    public DataBaseHelper users;
    public ProductionUPDATE prod;
    public LoaderJSON LoaderJSON_;
    public ReactBlob react = new ReactBlob();
    public ArrayList<String> subscribers;

    private abstractions.Settings setts;

    public Deps() throws InterruptedException, SQLException, IOException {
        if (!new File(binprops).exists()){
            System.out.println("Binnary settings file not exist");
            return;
        }
        DSL = new DSL();
        setts = (abstractions.Settings) BinaryMessage.restored(BinaryMessage.readBytes(binprops));
        System.out.println(setts.AktorPORT+"\n:::\n"+setts.usersPostgresConnect+"\n:::\n"+ setts.requestsPOSTGRESConnect);
        prod = new ProductionUPDATE();
        if (new File(lockProd).exists()){
            prod.Production=true;
            prod.init();
        }
        requests = new DataBaseHelper(setts.requestsPOSTGRESConnect, true);//requests = new DataBaseHelper("requests");
        LoaderJSON_ =  new LoaderJSON(requests.executor);
        users = new DataBaseHelper(setts.usersPostgresConnect, true);
       // Settings = new Readfile(fileprops);

        Incomming = new Readfile(incomingFolder);
        try {
            this.loginchecker = new LoginProcessor( users.executor);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        idh = new IDHelper(requests.executor);
        irp = new InputRequestProcessor(requests.executor);
        irp.prod=prod;
        irp.loader= LoaderJSON_;
        this.cypher = new CypherImpl();
        aktor = new ServerAktor();
        aktor.onRequest = new OnRequest() {
            @Override
            public void action(RequestMessage req) throws IOException, ParseException {
                String msg = "Новый запрос от @"+LoaderJSON.loadPzu(req);
                var DSLforSMS = DSL.getDSLforObject("sms", "server");
                System.out.println("\n\n\nDSL for SMS loaded::"+DSLforSMS+"\n\n\n");
                var reqs = DSL.dslProcessors.get("sms");
                SendSMS.Companion.sendSMS(msg, DSLforSMS, (SMSDSLProcessor) reqs);
            }
        };
        aktor.irp=irp;
        aktor.incomingFolder = incomingFolder;
        aktor.setAddress(setts.AktorPORT);
        aktor.echoWebSocket = echoWebSocket;

        aktor.setCypher(cypher);
        System.out.println("\n\n\n*************************\n****Spawning JAKtor******\n*************************\n\n\n\n");
        aktor.spawn();
        orp = new OutputResponceProcessor();
        orp.Incomming = Incomming;
        orp.jaktor=aktor;
        orp.idHelper=idh;
        orp.executor=requests.executor;
        orp.incomingFolder = incomingFolder;
        irp.jaktor=aktor;
    }


}