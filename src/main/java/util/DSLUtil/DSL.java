package util.DSLUtil;

import DSLGuided.requestsx.PSA.PSAConnector;
import DSLGuided.requestsx.PSA.PSADSLProcessor;
import DSLGuided.requestsx.PSA.PSASearchProcessor;
import DSLGuided.requestsx.RequestsDSLProcessor;
import DSLGuided.requestsx.SMS.SMSDSLProcessor;
import DSLGuided.requestsx.WProcessor.WProcessor;
import Message.abstractions.BinaryMessage;
import se.roland.abstractions.RolesStorage;

import java.io.*;
import java.util.HashMap;

public class DSL {
    public String dslfile;
    public DSL() {
        prepareMap();
        prepareDSLProcessors();
    };
    public DSL(String dslfile_) throws IOException {
        if (new File(dslfile_).exists())
            rs = (RolesStorage) BinaryMessage.restored(BinaryMessage.readBytes(dslfile_));
        else {
            rs = new RolesStorage();
            BinaryMessage.write(BinaryMessage.savedToBLOB(rs), dslfile_);
        }
        this.dslfile = dslfile_;
        prepareDSLProcessors();
        prepareMap();
    };
    public RequestsDSLProcessor RequestsDSLProcessor;
    public PSADSLProcessor PSADSLProcessor;
    public SMSDSLProcessor SMSDSLProcessor;
    public PSASearchProcessor PSASearchProcessor;
    public PSAConnector PSAConnector;
    public HashMap<String, DSLGuided.requestsx.DSLProcessor> dslProcessors;
    public HashMap<String, String> urltoDSLProc;
    public WProcessor WProcessor;
    public RolesStorage rs;

    public void prepareDSLProcessors(){
        RequestsDSLProcessor = new RequestsDSLProcessor();
        SMSDSLProcessor = new SMSDSLProcessor();
        PSADSLProcessor = new PSADSLProcessor();
        PSAConnector = new PSAConnector();
        PSASearchProcessor = new PSASearchProcessor();
        WProcessor = new WProcessor();

        dslProcessors = new HashMap<>();
        dslProcessors.put("requests", RequestsDSLProcessor);
        dslProcessors.put("sms", SMSDSLProcessor);
        dslProcessors.put("psa", PSADSLProcessor);
        dslProcessors.put("psaconnector", PSAConnector);
        dslProcessors.put("psasearch", PSASearchProcessor);
        dslProcessors.put("wprocessor", WProcessor);


    }

    public void prepareMap(){
        urltoDSLProc = new HashMap<>();
        urltoDSLProc.put("/login", "requests");
    }



    public String getDSLforObject(String nameObject, String User) throws IOException {
        File f = new File("rules"+File.separator+User+".rules");
        if (!f.exists())
            return "";
        FileReader fr = new FileReader(f.getPath());
        BufferedReader br = new BufferedReader(fr);
        String line;
        while((line = br.readLine()) != null){
           String str = "'"+nameObject+"'";
           if (line.indexOf(str)>-1) {
               br.close();
               fr.close();
               return line;
           }
        }
        br.close();
        fr.close();
        return "";
    }


}
