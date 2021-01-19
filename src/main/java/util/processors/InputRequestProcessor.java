package util.processors;

import Message.abstractions.BinaryMessage;
import abstractions.Condition;
import abstractions.PendingResponces;
import abstractions.RequestMessage;
import abstractions.ResponceMessage;
import fr.roland.DB.Executor;
import org.json.simple.parser.ParseException;
import servers.ServerAktor;
import util.DB.ProductionUPDATE;
import util.Deps;
import util.JSON.Beatyfulizer;
import util.JSON.LoaderJSON;
import util.JSON.ParcedJSON;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class InputRequestProcessor {
    public ServerAktor jaktor;
    private Executor executor;
    public ProductionUPDATE prod;
    public LoaderJSON loader;

    public InputRequestProcessor(){
    };

    public InputRequestProcessor(Executor executor){
        this.executor = executor;
    }
    public void saveUpdatingRequestinDB(RequestMessage req) throws SQLException, ParseException {
        PreparedStatement stmt = executor.getConn().prepareStatement("UPDATE requests SET updateddata = ?::jsonb, datetimeupdate = ? WHERE id = ?");
        System.out.println("JSON::>"+req.JSONed);
        stmt.setObject(1, req.JSONed);
        stmt.setTimestamp(2, new Timestamp(new Date().getTime()));///new java.sql.Date(new Date().getTime().n));
        stmt.setString(3,  req.ID);
        System.out.println("executing >>>"+ req.ID);
        stmt.executeUpdate();
        prod.fullupdate(ParcedJSON.parse(req.JSONed));

    }

    public void saveRequestinDB(RequestMessage req) throws SQLException {
        PreparedStatement stmt = executor.getConn().prepareStatement("INSERT INTO requests  VALUES (?, ?, ?::jsonb, NULL, NULL, NULL, ?)");
        stmt.setString(1, req.ID);
        stmt.setTimestamp(2, new Timestamp(new Date().getTime()));///new java.sql.Date(new Date().getTime().n));
        long now = new Date().getTime();
        System.out.println();
        Date date = new java.sql.Date(now);
        System.out.println("Date = "+date.toString());
        stmt.setString(3, req.JSONed);
        stmt.setString(4, req.Description);
        System.out.println("executing");
        stmt.executeUpdate();
    }

    public ArrayList<Object> loadrequests() throws SQLException {
        ResultSet res = executor.submit("select * from requests order by DATETIMEREQUEST asc;");
        ArrayList result = new ArrayList();
        while (res.next()){
            for (int i =1; i<6; i++)
          //  result .add(res.getObject(4));
            {
           //     System.out.print(res.getObject(i)+"  ;  ");
                result.add(res.getObject(i));
            }
           // System.out.println("  ;  ");
        }
        return result;
    };

    public ArrayList<Object> loadrequests8() throws SQLException {
        ResultSet res = executor.submit("select * from requests order by counter desc");
        ArrayList result = new ArrayList();
        while (res.next()){
         /*   result.add(res.getObject(8));
            result.add(res.getObject(2));
            result.add(res.getObject(3));
            result.add(res.getObject(4));
            result.add(res.getObject(5));
            result.add(res.getObject(6));
            result.add(res.getObject(7));  old order*/
            result.add(res.getObject(8));
            result.add(res.getObject(2));
            result.add(res.getObject(7));
            result.add(res.getObject(3));
            result.add(res.getObject(6));
            result.add(res.getObject(5));
            result.add(res.getObject(4));
        }
        return result;
    };

    public ArrayList loadrequests8inmatrix() throws SQLException {
        ResultSet res = executor.submit("select * from requests order by counter desc");
        ArrayList result = new ArrayList();
        while (res.next()){
            ArrayList _result = new ArrayList();
            _result.add(res.getObject(8));
            _result.add(res.getObject(2));
            _result.add(res.getObject(7));
            _result.add(res.getObject(3));
            _result.add(res.getObject(6));
            _result.add(res.getObject(5));
            _result.add(res.getObject(4));
            result.add(_result);
        }

        return result;
    };


    public ResultSet loadrequestsSet() throws SQLException {
        return executor.submit("select * from requests order by counter desc;");
    };


    public String DumpRequestToHTMLTable() throws SQLException {
        ArrayList data = loadrequests();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<data.size(); i++){
            if (i%6==0)
                sb.append("<tr>");
            sb.append("<td>"+data.get(i)+"</td>");
           // if (i%5==0)
           //     sb.append("</td>");
        };
        return sb.toString();
    }

    public String DumpRequestToHTMLTable8() throws SQLException {
        ArrayList data = loadrequests8();
        System.out.println("SIZE::"+data.size());
        StringBuilder sb = new StringBuilder();
        int first = 1;
        int number_row=data.size()/7;
        for (int i=0; i<data.size(); i++){
            if (i%7==0) {
                if ((first == 1) && (number_row!=1)){
                    first =0;
                    sb.append("<tr>");
                }
                else
                    sb.append("<td><approvetag number=\""+(number_row--)+"\"></approvetag></td><tr>");
            }
            sb.append("<td>"+data.get(i)+"</td>");

        };
        sb.append("<td><approvetag number=\""+(number_row--)+"\"></approvetag></td><tr>");
        return sb.toString();
    }



    public String DumpRequestToHTMLTable8usingmatrix() throws SQLException {
        ArrayList<ArrayList> data = loadrequests8inmatrix();
        System.out.println("SIZE::"+data.size());
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        int number_row=data.size();
        for (int i=0; i<data.size(); i++){
            for (int j=0; j<data.get(i).size(); j++)
                sb.append("<td>"+data.get(i).get(j)+"</td>");
            sb.append("<td><approvetag number=\""+(number_row--)+"\"></approvetag></td><tr>");
        };
        sb.append("<td><approvetag number=\""+(number_row--)+"\"></approvetag></td><tr>");
        return sb.toString();
    }

    public Condition processRow(StringBuilder sb, ArrayList data) throws ParseException {
        String savedJS = String.valueOf(data.get(5));
        Condition result=Condition.SUSPENDING;
        if (data.get(4)!=null){
            if (savedJS.length()==10) //&& savedJS.length()<5)
                result = Condition.DECLINED;
            else
                result = Condition.APPROVED;

        }
        String savedJSON="";
        String initialJSON = Beatyfulizer.schoneJSON(ParcedJSON.parse(String.valueOf(data.get(3))));
        if (!result.equals(Condition.DECLINED))
            savedJSON = Beatyfulizer.compareundschoneJSON(ParcedJSON.parse(String.valueOf(data.get(5))), ParcedJSON.parse(String.valueOf(data.get(3))));
        int counter = (int) data.get(0);
        sb.append("<tr>");
        sb.append("<td align=\"center\"><div id='"+counter+"0'>"+data.get(0)+"</div></td>");
        sb.append("<td align=\"center\"><div id='"+counter+"1'>"+data.get(1)+"</div></td>");
        sb.append("<td align=\"center\"><div id='"+counter+"2'>"+data.get(2)+"</div></td>");
        sb.append("<td align=\"center\"><div id='"+counter+"3'>"+initialJSON+"</div></td>");
        if (data.get(4)==null)
            sb.append("<td><div id='"+counter+"4'>"+"</div></td>");
        else
            sb.append("<td align=\"center\"><div id='"+counter+"4'>"+data.get(4)+"</div></td>");
        sb.append("<td align=\"center\"><div id='"+counter+"5'>"+savedJSON+ "</div></td>");
        if (data.get(6)==null){
            sb.append("<td><div id='"+counter+"6'>"+"</div></td>");
        }
        else
            sb.append("<td align=\"center\"><div id='"+counter+"6'>"+data.get(6)+"</div></td>");
        return result;

    };

    public Condition processRow(StringBuilder sb, ArrayList data, int numberrow) throws ParseException {
        String savedJS = String.valueOf(data.get(5));
        Condition result=Condition.SUSPENDING;
        if (data.get(4)!=null){
            if (savedJS.length()==10) //&& savedJS.length()<5)
                result = Condition.DECLINED;
            else
                result = Condition.APPROVED;

        }
        String savedJSON="";
        String initialJSON = Beatyfulizer.schoneJSON(ParcedJSON.parse(String.valueOf(data.get(3))));
        if (!result.equals(Condition.DECLINED))
            savedJSON = Beatyfulizer.compareundschoneJSON(ParcedJSON.parse(String.valueOf(data.get(5))), ParcedJSON.parse(String.valueOf(data.get(3))));
        int counter = (int) data.get(0);
        sb.append("<tr id=\""+numberrow+"\">");
        sb.append("<td align=\"center\"><div id='"+counter+"a0'>"+data.get(0)+"</div></td>");
        sb.append("<td align=\"center\"><div id='"+counter+"a1'>"+data.get(1)+"</div></td>");
        sb.append("<td align=\"center\"><div id='"+counter+"a2'>"+data.get(2)+"</div></td>");
        sb.append("<td align=\"center\"><div id='"+counter+"a3'>"+initialJSON+"</div></td>");
        if (data.get(4)==null)
            sb.append("<td><div id='"+counter+"a4'>"+"</div></td>");
        else
            sb.append("<td align=\"center\"><div id='"+counter+"a4'>"+data.get(4)+"</div></td>");
        sb.append("<td align=\"center\"><div id='"+counter+"a5'>"+savedJSON+ "</div></td>");
        if (data.get(6)==null){
            sb.append("<td><div id='"+counter+"a6'>"+"</div></td>");
        }
        else
            sb.append("<td align=\"center\"><div id='"+counter+"a6'>"+data.get(6)+"</div></td>");
        return result;

    };

    public String DumpAllRequestToJSON() throws SQLException {
        return loader.loadAll2JSON();
    };

    public String DumpRequestToHTMLTable8usingmatrixhardcoded() throws SQLException, ParseException {
        ArrayList<ArrayList> data = loadrequests8inmatrix();
        System.out.println("SIZE::"+data.size());
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        int number_row=data.size();
        for (int i=0; i<data.size(); i++){
            String result = String.valueOf(processRow(sb, data.get(i)));
            sb.append("<td><approvetag status=\""+result+"\"number=\""+(number_row--)+"\"></approvetag></td><tr>");
        };
        //sb.append("<td><approvetag number=\""+(number_row--)+"\"></approvetag></td><tr>");
        return sb.toString();
    }

    public String mountScript(){
        String basic = "<script type=\"text/babel\">\n";
        StringBuilder sb= new StringBuilder();
        sb.append(basic);
        sb.append("</script>");
        return sb.toString();

    };

    public String DumpRequestToHTMLTableReact() throws SQLException, ParseException, IOException {
 ////////  FileOutputStream fos = new FileOutputStream("dump.requests");
        ArrayList<ArrayList> data = loadrequests8inmatrix();
        System.out.println("SIZE::"+data.size());
        StringBuilder sb = new StringBuilder();

        int number_row=data.size();
        for (int i=0; i<data.size(); i++){
            String result = String.valueOf(processRow(sb, data.get(i), number_row));
            int numbertag=number_row--;
            sb.append("<td><div id=\"approvetag"+(numbertag)+"\"></div></td><tr>" +
                    " <script type=\"text/babel\">\n" +
                    "const json__={'number':"+numbertag+", 'status':'"+result+"'}; ReactDOM.hydrate(<Approve info={json__}/>, document.getElementById('approvetag"+numbertag+"'));" +
                    "         </script></tr>  ");
  ///////       String dump = "ReactDOM.render(<Approve number=\""+numbertag+"\" status=\""+result+"\"/>, document.getElementById('approvetag"+numbertag+"'));\n" ;
  ///////       fos.write(dump.getBytes() );
        };
 ///////   fos.close();
        return sb.toString();
    }


    public String DumpRequestToHTMLTable8new() throws SQLException, ParseException {
        ArrayList data = loadrequests8();
        System.out.println("SIZE::"+data.size());
        StringBuilder sb = new StringBuilder();
        int first = 1;
        int number_row=data.size()/7;
        for (int i=0; i<data.size(); i++){
            System.out.println("i::>>"+i+"@data::"+data.get(i));
            if (i%7==0) {
                if ((first == 1) && (number_row!=1)){
                    first =0;
                    sb.append("<tr>");
                }
                else sb.append("<td><approvetag number=\""+(number_row--)+"\"></approvetag></td><tr>");

            }
            if ((i%3==0) || (i%5==0)) {
                /////////////old        sb.append("<td>" + data.get(i) + "</td>");

                String load = String.valueOf(data.get(i));
                System.out.println("PARSING::"+load);
                sb.append("<td>"+Beatyfulizer.schoneJSON(ParcedJSON.parse(load))+ "</td>");
                continue;
            }
            sb.append("<td>"+data.get(i)+"</td>");

            // if (i%5==0)
            //     sb.append("</td>");
        };
        sb.append("<td><approvetag number=\""+(number_row--)+"\"></approvetag></td><tr>");
        return sb.toString();
    }

    Condition getStatus(String ID, String FileName) throws IOException {
        if (!new File(FileName).exists())
            return null;
        PendingResponces resp = (PendingResponces) BinaryMessage.restored(BinaryMessage.readBytes(FileName));
        if (resp != null)
            return resp.ReqMap.get(ID);
        else
        return null;
    };

    public void removeStatus(String ID, String FileName) throws IOException {
        PendingResponces resp = (PendingResponces) BinaryMessage.restored(BinaryMessage.readBytes(FileName));
        if (resp == null)
            return ;
        resp.ReqMap.remove(ID) ;
    };

    public void processAsk(RequestMessage req) throws IOException {
        if (getStatus(req.ID, Deps.PendingResponcesFile)==null)
            return;
        ResponceMessage res = new ResponceMessage();
        res.ID = req.ID;
        res.approved=false;
        Condition cond = getStatus(req.ID, Deps.PendingResponcesFile);
        if (cond.equals(Condition.APPROVED))
           res.approved=true;
        System.out.println("SENDING RESPONXE");
        removeStatus(res.ID , Deps.PendingResponcesFile);
        jaktor.sendResponce(res);
        //sendWebsocketAlerts();
    };


}
