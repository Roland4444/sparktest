package util;

import DSLGuided.requestsx.DSL;
import DSLGuided.requestsx.SMS.SMSDSLProcessor;
import DSLGuided.requestsx.WProcessor.WProcessor;
import Message.abstractions.BinaryMessage;
import abstractions.Cypher;
import abstractions.OnRequest;
import abstractions.RequestMessage;
import abstractions.TestAction;
import org.json.simple.parser.ParseException;
import servers.EchoWebSocket;
import servers.ServerAktor;
import test.TestThread;
import util.DB.PSAClient;
import util.DB.DataBaseHelper;
import util.DB.ProductionUPDATE;
import util.JSON.LoaderJSON;
import util.processors.InputRequestProcessor;
import util.processors.OutputResponceProcessor;
import util.react.ReactBlob;
import util.readfile.Readfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Deps {
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
    private Readfile Incomming;
    public IDHelper idh;
    public Class<EchoWebSocket> echoWebSocket;
    public OutputResponceProcessor orp;
    public DataBaseHelper requests;
    public DataBaseHelper users;
    public PSAClient PSAClient;
    public ProductionUPDATE prod;
    public LoaderJSON LoaderJSON_;
    public ReactBlob react = new ReactBlob();
    public ArrayList<String> subscribers;
    public timeBasedUUID timeBasedUUID;
    private abstractions.Settings setts;
    public TestThread TestThread;
    public DSLGuided.requestsx.DSL DSL;

    public void initTestThread(){
        System.out.println("****************************\nSTARTING TEST THREAD!!!\n****************************");
        TestThread = new TestThread(20);
        TestThread.testAaction = new TestAction() {
            @Override
            public void action() throws IOException {
                System.out.println("""
                ****************************TEST ACTION!!!!""");

                byte[] msg = Files.readAllBytes(new File("w.bin.temp").toPath());
                HashMap data = (HashMap) Saver.Companion.restored(msg);
                System.out.println("DATA::\n\n");
                var name = "wprocessor";
                var proc = (WProcessor) DSL.getDslProcessors().get(name);
                var dsl = DSL.getDSLforObject(name, "server");
                System.out.println("extracted DSL::"+dsl);
                WProcessor.Companion.resend(dsl, proc, data);
                if ((data.get("FIRST_SNAPSHOT")!=null)&&(data.get("SECOND_SNAPSHOT")!=null)){
                    System.out.println("EXTRACTED PARAMS!!!");
                    WProcessor.Companion.saveImages(
                            dsl,
                            proc,
                            (byte[]) data.get("FIRST_SNAPSHOT") ,
                            (byte[]) data.get("SECOND_SNAPSHOT"),
                            String.valueOf(data.get("DEPART_ID")),
                            String.valueOf(data.get("DATE")),
                            String.valueOf(data.get("WAYBILL"))
                    );
                }
            }
        };
        TestThread.start();
    }
    public Deps() throws InterruptedException, SQLException, IOException {
        if (!new File(binprops).exists()){
            System.out.println("Binnary settings file not exist");
            return;
        }
        DSL = new DSL();
        timeBasedUUID = new timeBasedUUID();
        PSAClient = new PSAClient("https://passport.avs.com.ru/");
        PSAClient.exec = DSL.getPSAConnector().executor;
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
            this.loginchecker.PSAConnector = DSL.getPSAConnector();
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
                var reqs = DSL.getSMSDSLProcessor();
                SMSDSLProcessor.Companion.sendSMS(msg, DSLforSMS, (SMSDSLProcessor) reqs);
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