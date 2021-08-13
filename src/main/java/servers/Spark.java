package servers;
import DSLGuided.requestsx.PSA.PSADSLProcessor;
import DSLGuided.requestsx.PSA.PSASearchProcessor;
import DSLGuided.requestsx.WProcessor.WProcessor;
import org.jetbrains.annotations.NotNull;
import spark.ModelAndView;
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;
import spark.utils.IOUtils;
import util.Deps;
import util.Saver;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static spark.Spark.*;
public class Spark {
    public static Map map_ = new HashMap<>();
    public static VelocityTemplateEngine eng =  new VelocityTemplateEngine();
    public static ModelAndView OK = new ModelAndView(map_, "OK.html");
    public static ModelAndView BAD = new ModelAndView(map_, "Bad.html");
    public static ModelAndView SOCKET = new ModelAndView(map_, "websocket.html");
    public static Map<String, Object> model = new HashMap<>();

    public static void main(String[] args) throws InterruptedException, SQLException, IOException, ClassNotFoundException {
        System.out.println("€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€");
        System.out.println("€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€");
        System.out.println("€€€   Production RF Version with PSA DSL MODULE  €€");
        System.out.println("€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€");
        System.out.println("€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€");
        Class.forName("com.mysql.cj.jdbc.Driver");
        Class.forName("org.postgresql.Driver");
        staticFiles.location("/public");
        Deps deps = new Deps();
        deps.echoWebSocket =  EchoWebSocket.class;
        var template =  new VelocityTemplateEngine();
        webSocket("/echo", EchoWebSocket.class);

        get("/getpsanumber", (req,res)-> {
            var reqs = req.queryParams("depid");
            return deps.DSL.getPSADSLProcessor().getPSANumberviaDSL(reqs);
        });



        get("/getpsanumber", (req,res)-> {return deps.loginchecker.test();});

        get("/checkpassport", (req,res)->{
            var pass = req.queryParams("pass");
            return deps.PSAClient.checkpass(pass);
        });


        get("/psaproc", (req,res)-> {   ///@ PSAX.html
            var reqs = req.queryParams("input");
            var colorreq = req.queryParams("colorinput");
            System.out.println("colorreq::\n\n\n"+colorreq);
            System.out.println(reqs);
            return deps.PSAClient.getClientNameAndID(reqs);});

        post("/checkpost", (req,res)-> {
            var serie = req.queryParams("serie");
            var number = req.queryParams("number");
            System.out.println("SERIES::"+serie);
            System.out.println("number::"+number);

            return "OK";});

        post("/paystatus", (req,res)-> {
            var cardNumber = req.queryParams("cardNumber");
            var psaId = req.queryParams("psaId");
            System.out.println("cardNumber::"+cardNumber);
            System.out.println("psaId::"+psaId);

            return "OK";});

        post("/colorpsa", (req,res)-> {
            var data = req.queryParams("data");
            var uuid = req.queryParams("uuid");
            var summary = req.queryParams("summary");
            System.out.println("data::"+data);
            System.out.println("uuid::"+uuid);
            System.out.println("summary::"+summary);
            var DSLforPSA = deps.DSL.getDSLforObject("psa", "server");
            var reqs = deps.DSL.getDslProcessors().get("psa");
            PSADSLProcessor.Companion.processColorPSA(data, uuid, DSLforPSA, (PSADSLProcessor) reqs);
            return "OK";});

        get("/passport", (req,res)-> {
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "passport.html"));});

        get("/psapage", (req,res)-> {
            var reqs = req.queryParams("psanumber");
            model.clear();
            model.put("psanumber", reqs);
            model.put("initial", reqs);
//
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "psapage.html"));});

        get("/launchtest", (req,res)->{
            byte[] msg = Files.readAllBytes(new File("w.bin.temp").toPath());
            HashMap data = (HashMap) Saver.Companion.restored(msg);
            System.out.println("DATA::\n\n");
            var proc = (WProcessor) deps.DSL.getDslProcessors().get("wprocessor");
            var dsl = deps.DSL.getDSLforObject("wprocessor", "server");
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
            return "ok";
        });

        post("/loadw", (req,res)->{
            byte[] msg = req.bodyAsBytes();
            Saver.Companion.write(msg, "w.bin.temp");
            HashMap data = (HashMap) Saver.Companion.restored(msg);
            System.out.println("DATA::\n\n");
            var proc = (WProcessor) deps.DSL.getDslProcessors().get("wprocessor");
            var dsl = deps.DSL.getDSLforObject("wprocessor", "server");
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
                        String.valueOf(data.get("ID"))
                );
            }
            return "ok";
        });

        post("/loginpsa", (req,res)->{
            var login = req.queryParams("login");
            var password = req.queryParams("password");
            System.out.println("login:: "+login+"\npassword::"+password);
            if (deps.loginchecker.checkpsalogin(login, password)){
                res.cookie("depid", deps.loginchecker.getpsadepid(login));
                res.cookie("userpsa", login);
                res.redirect("/psa");
            }
            res.redirect("/psalogin");
            return "OK";
        });

        post("/testresend", (req,res) ->{
            var T1 = req.queryParams("t1");
            var T2 = req.queryParams("t2");
            var T3 = req.queryParams("t3");

            var PLATE_NUMBER = req.queryParams("PLATE_NUMBER");
            var PRICEPER_KG = req.queryParams("PRICEPER_KG");

            System.out.println("TEST1::"+T1);
            System.out.println("TEST2::"+T2);
            System.out.println("TEST3::"+T3);

            System.out.println("PLATE_NUMBER::"+PLATE_NUMBER);
            System.out.println("PRICEPER_KG::"+PRICEPER_KG);


            return "OK";
        });

        get("/psa", (req,res)-> {
            System.out.println(req.cookies());
            var se = req.cookie("userpsa");
//            var depid = req.cookie("depid");
//            if (depid == null){
//                System.out.println("DEPID NULL!!!");
//            }
//            if (depid.equals("")){
//                System.out.println("DEPID EQUALS '''''!!!");
//            }
            if (se == null)
                res.redirect("/psalogin");
            System.out.println(se);
            model.clear();
            model.put("idbutton", 44);
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "psax.html"));});

        post("/psasearch", (req,res)-> {
            var reqs = req.queryParams("search");
            var se = req.cookie("userpsa");
            var depid = deps.loginchecker.getpsadepid(se);
            System.out.println("depid::"+depid);
            if (( depid == null) ) {
                System.out.println("UNRESTRICTED!!!");
                return PSASearchProcessor.Companion.search(reqs, deps.DSL.getPSASearchProcessor());
            }
            else {
                System.out.println("Restricting deps!!!");
                return PSASearchProcessor.Companion.search(reqs, deps.DSL.getPSASearchProcessor(),depid);
            }
        });
        post("/psasetclient", (req,res)-> {
            var name  = req.queryParams("name");
            var psanumber  = req.queryParams("psanumber");
            var idclient  = req.queryParams("idclient");
            var type = req.queryParams("type");

            deps.PSAClient.updateclient(name, psanumber, idclient, type);

            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "psapage.html"));});

        post("/draftpsa", (req, res)->{
            System.out.println("RECEIVED draft psa request");
            HashMap<String, String> params = new HashMap<>();
            System.out.println("PARAMS!!!!!");
            System.out.println(req.queryParams("Brutto"));
            System.out.println(req.queryParams("Sor"));
            System.out.println(req.queryParams("Metal"));
            System.out.println(req.queryParams("DepId"));
            System.out.println(req.queryParams("PlateNumber"));
            System.out.println(req.queryParams("UUID"));
            System.out.println(req.queryParams("Type"));
            System.out.println(req.queryParams("Section"));
            System.out.println("\n\n\n\n\nCUSTOMER::"+req.queryParams("Customer"));
            System.out.println("\n\n\n\n\nPrice::"+req.queryParams("Price"));


            params.put("Brutto", req.queryParams("Brutto"));
            params.put("Sor", req.queryParams("Sor"));
            params.put("Metal", req.queryParams("Metal"));
            params.put("DepId", req.queryParams("DepId"));
            params.put("PlateNumber", req.queryParams("PlateNumber"));
            params.put("UUID", req.queryParams("UUID"));
            params.put("Type", req.queryParams("Type"));
            params.put("Section", req.queryParams("Section"));
            var DSLforSMS = deps.DSL.getDSLforObject("psa", "server");
            var reqs = deps.DSL.getDslProcessors().get("psa");
            PSADSLProcessor.Companion.createdraftPSA(params, DSLforSMS, (PSADSLProcessor) reqs);
            return "OK";
        });

        get("/echo2", (req,res)->{
            var header = req.headers("Authorization");
            System.out.println("HEADER::"+header);
            return "echo";
        });

        post("/echo2", (req,res)->{
            var header = req.headers("Authorization");
            System.out.println("HEADER::"+header);
            return "echo";
        });

        post("/completepsa", (req, res)->{
            System.out.println("RECEIVED complete psa request");
            HashMap<String, String> params = new HashMap<>();
            params.put("Sor", req.queryParams("Sor"));
            params.put("Tara", req.queryParams("Tara"));
            params.put("UUID", req.queryParams("UUID"));
            if (req.queryParams("Price")!=null)
                params.put("Price", req.queryParams("Price"));
            var DSLforSMS = deps.DSL.getDSLforObject("psa", "server");
            var reqs = deps.DSL.getDslProcessors().get("psa");
            PSADSLProcessor.Companion.completePSA(params, DSLforSMS, (PSADSLProcessor) reqs);
            return "OK";
        });

        post("/transfer", (req, res)->{
            var DSL = deps.DSL.getDSLforObject("wprocessor", "server");
            String id = req.queryParams("depid");
            WProcessor  reqs = (WProcessor) deps.DSL.getDslProcessors().get("wprocessor");
            return Saver.Companion.savedToBLOB(WProcessor.Companion.getTransfers(DSL, reqs, id));
        });

        post("/transferprocess", (req, res)->{
            String uuidTransfer = req.queryParams("UUIDTransfer");
            System.out.println("CATCHED TRANSFER DATA");
            System.out.println("uuidTransfer");
            System.out.println(uuidTransfer);
            return "OK";
        });



        post("/psatransferprocess", (req, res)->{
            return "OK";
        });

        get("upload", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "upload.html"));
        });

        post("/api/upload", (req, res) -> {
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("D:/tmp"));
            Part filePart = req.raw().getPart("myfile");
            try (InputStream inputStream = ((Part) filePart).getInputStream()) {
                OutputStream outputStream = new FileOutputStream("./" + filePart.getSubmittedFileName());
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
            }
            return "File uploaded and saved.";
        });

        get("todo", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "todo.html"));
        });


        get("users", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "users.html"));
        });

        get("re", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "re.html"));
        });

        get("react", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "react.html"));
        });

        get("dyreact", (req, res) -> {
            model.clear();
            model.put("dynoids", deps.react.blobId(1000));
            model.put("dymounts", deps.react.blobScript(1000));
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "dyreact.html"));
        });
        get("oneriot", (req,res)->{
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "oneriot.html"));
        });

        get("onereact", (req,res)->{
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "onereact.html"));
        });

        get("ajson", (req,res)->{
            model.clear();
            String params = req.queryParams("params");
           // System.out.println("AJAX called get");
            return deps.LoaderJSON_.loadAll2JSON();
        });


        get("pass2", (req,res)->{
            model.clear();
            String user = req.session().attribute("user");
            System.out.println("USER"+user);
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "pass.html"));
        });
        get("pass", (req, res)->{
            if (check(req)){
                String user = req.session().attribute("user");
                String pass = req.queryParams("pass");
            //    deps.loginchecker.updatepass(user, pass);
                return "OK";
            }
            res.status(500);
            return "BAD";
        //    return "-";
        });

        post("ajson", (req,res)->{
            model.clear();
            String params = req.queryParams("params");

            System.out.println("AJAX called get");
            return deps.LoaderJSON_.loadAll2JSON();
        });

        get("uuid", (req, res)->{
           return deps.timeBasedUUID.generate();
        });

        get("emptyajax", (req,res)->{
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "emptyajax.html"));
        });

        get("ajax", (req,res)->{
            model.clear();
            String params = req.queryParams("params");

            System.out.println("AJAX called get");
            return "worked; param ="+params;
        });
        get("aj", (req,res)->{
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "ajax.html"));
        });
        get("simple", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "simple.html"));
        });

        get("genriot", (req, res) -> {
            model.clear();
            String components = "<my-component attr=\"142\"></my-component>\n" +
                    "<my-component attr=\"146\"></my-component>\n" +
                    "<my-component attr=\"148\"></my-component>";
            model.put("components", components);
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "genriot.html"));
        });

        get("custom9", (req, res)->{
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "custom9.html"));
        });

        get("timer", (req, res)->{
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "timer.html"));
        });

        get("getriot", (req, res)->{
            String login = req.queryParams("value");
            return login;
        });

        post("ps", (req, res)->{
            String depid = req.queryParams("depid");
            return depid;
        });

        get("psalogin", (req, res)->{
            System.out.println(req.cookies());
            res.cookie("user", "lohness");
            var user = req.cookie("userpsa");
            var se = req.cookie("se");
            if (se == null)
                System.out.println("se is null");
            System.out.println(user);
            System.out.println(se);

            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "psalogin.html"));
        });


        get("riot", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "riot.html"));
        });

        get("riotsocket", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "riotsocket.html"));
        });


        get("requests8", (req, res) -> {
            model.clear();
            model.put("requests", deps.irp.DumpRequestToHTMLTable8());
            System.out.println(deps.irp.DumpRequestToHTMLTable8());
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "requests.html"));
        });

        get("requestsx", (req, res) -> {
            String dsl = new String(Files.readAllBytes(Path.of("rules")));
            var  reqs =  deps.DSL.getDslProcessors().get("requests");
            model.clear();
            model.put("requests", deps.irp.DumpRequestToHTMLTableReact());
            reqs.setOuttemplate( new VelocityTemplateEngine().render(
                    new ModelAndView(model, "requestsx.html")));
            System.out.println("DSL::>>"+dsl);
            return reqs.render(dsl);
           /* model.clear();
            model.put("requests", deps.irp.DumpRequestToHTMLTableReact());
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "requestsx.html")); */
        });

        get("dsl", (req, res) -> {
            String dsl = new String(Files.readAllBytes(Path.of("rules")));
            var  reqs =  deps.DSL.getDslProcessors().get("requests");
            model.clear();
            model.put("requests", deps.irp.DumpRequestToHTMLTableReact());
            reqs.setOuttemplate( new VelocityTemplateEngine().render(
                    new ModelAndView(model, "requestsx.html")));
            System.out.println("DSL::>>"+dsl);
            return reqs.render(dsl);
        });


        get("emptyws", (req, res) -> {
            model.clear();
            model.put("requests", "");
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "emptyws.html"));
        });

        get("dump", (req, res) -> {
            model.clear();

            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "dump.html"));
        });

        get("requests", (req, res) -> {
            model.clear();
            model.put("requests", deps.irp.DumpRequestToHTMLTable8usingmatrixhardcoded());
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "requests.html"));
        });

        get("zzz", (req, res) -> {
            model.clear();
            model.put("requests", deps.irp.DumpRequestToHTMLTable8usingmatrixhardcoded());
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "call.html"));
        });

        get("websocket", (req, res) -> eng.render(SOCKET));

        redirect.get("/", "login.area");
        get("/hello", (req, res) -> "Hello World");
        get("login.area", (req, res) -> {
            boolean authenticated=true;
            // ... check if authenticated
            if (!authenticated) {
                halt(401, "You are not welcome here");
            }

            return eng.render(
                    new ModelAndView(map_, "login.html")
            );
        });
        get("/check", (req, res) ->{
            if (check(req))
                return eng.render(OK);
            return eng.render(BAD);
        });
        get("/approve", (req,res)->{
            System.out.print("1AAPPROVE \n\n\n");
            if (check(req)){  /// <<update in prod
                String id = req.queryParams("id");
                System.out.print("AAPPROVE \n\n\n");
                deps.orp.approve(id);
            //    res.redirect("/requests");


                //return eng.render(OK);
            }
            return eng.render(BAD);
        });


        get("/react", (req,res)->{
            if (check(req)){
                String id = req.queryParams("id");
                deps.orp.approve(id);
                res.redirect("/requests");


                //return eng.render(OK);
            }
            return eng.render(BAD);
        });


        get("/logout", (req,res)->{
            req.session().attribute("logined", false);
            res.redirect("/login.area");
            return "";
        });

        get("/decline", (req,res)->{
            if (check(req)){
                String id = req.queryParams("id");
                deps.orp.decline(id);
                res.redirect("/requests");
                //return eng.render(OK);
            }
            return eng.render(BAD);
        });
        get("/android", (req,res)->{
            System.out.println("ANDROID GET!");
            return "responce";
        });

        post("/login", (req, res) -> {
            System.out.println(req.pathInfo());
            System.out.println("IN LOGIN AREA");
            String login = req.queryParams("login");
            String pass = req.queryParams("password");
            if (deps.loginchecker.checklogin(login, pass)) {
                req.session().attribute("logined", true);
                req.session().attribute("user", login);
                System.out.println((String) req.session().attribute("user"));
          //////////      model.clear();
            ///    model.put("requests", deps.irp.DumpRequestToHTMLTable8());
            ///    System.out.println(deps.irp.DumpRequestToHTMLTable8());
                model.clear();
         //////////////       model.put("requests", deps.irp.DumpRequestToHTMLTableReact());
       ////////////////         char first = Character.toUpperCase(login.charAt(0));
        ///////////////        String uppercasedUser =  first + login.substring(1);
        ///////////////        model.put("user", uppercasedUser);
          ////////////////      return new VelocityTemplateEngine().render(
           //////////////             new ModelAndView(model, "requestsx.html"));
                String pseudoProc = deps.DSL.getUrltoDSLProc().get(req.pathInfo());
                var reqs = deps.DSL.getDslProcessors().get(pseudoProc);
                String dsl = deps.DSL.getDSLforObject(pseudoProc, login);   ///// new String(Files.readAllBytes(Path.of("rules")));
                model.put("requests", deps.irp.DumpRequestToHTMLTableReact());
                char first = Character.toUpperCase(login.charAt(0));
                String uppercasedUser =  first + login.substring(1);
                model.put("user", uppercasedUser);
                reqs.setOuttemplate( new VelocityTemplateEngine().render(
                        new ModelAndView(model, "requestsx.html")));
                System.out.println("DSL::>>"+dsl);
                return reqs.render(dsl).toString();
           //     model.put("requests", deps.irp.DumpRequestToHTMLTable8usingmatrixhardcoded());
           //     return new VelocityTemplateEngine().render(
           //             new ModelAndView(model, "requests.html"));
            }
            else
                req.session().attribute("logined", false);

                return eng.render(BAD);
        });

        get("/attr", (req,res)->{
            Set<String> attr = req.session().attributes();
            attr.forEach(a -> {System.out.println(a);});
            if (attr.isEmpty())
                req.session().attribute("logined", false);
            return eng.render(OK);
        });

        get("/send", (req, res) ->{
            EchoWebSocket.sendall("2019-09-0709:26:42багайцев");
            return eng.render(OK);
        });

        get("oldrequests", (req, res) -> {
            ResultSet resultSet = deps.irp.loadrequestsSet();
            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                sb.append("<tr>");
                String comment = resultSet.getString(7);
                for (int t = 1; t <= 6; t++) {
                    sb.append("<td>");
                    if (t == 3) {
                        sb.append("\"" + comment + "\"");
                        sb.append(resultSet.getString(t));
                        sb.append("</td>");
                    }
                    sb.append("</tr>");
                }
            }
            model.clear();
            model.put("requests", sb.toString());
            System.out.println(sb.toString());
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "requests.html"));
        });
/*
        File uploadDir = new File("upload");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist



        get("/upload", (req, res) ->
                "<form method='post' enctype='multipart/form-data'>" // note the enctype
                        + "    <input type='file' name='uploaded_file' accept='.png'>" // make sure to call getPart using the same "name" in the post
                        + "    <button>Upload picture</button>"
                        + "</form>"
        );

        post("/upload", (req, res) -> {

            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            logInfo(req, tempFile);
            return "<h1>You uploaded this image:<h1><img src='" + tempFile.getFileName() + "'>";

        });

    }

    // methods used for logging
    private static void logInfo(Request req, Path tempFile) throws IOException, ServletException {
        System.out.println("Uploaded file '" + getFileName(req.raw().getPart("uploaded_file")) + "' saved as '" + tempFile.toAbsolutePath() + "'");
    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

*/

}
    public static boolean check(Request req){
        Set<String> attr = req.session().attributes();
        attr.forEach(a -> {System.out.println(a);});
        if (attr.isEmpty())
            req.session().attribute("logined", false);
        if (req.session().attribute("logined").equals(true))
            return true;
        return false;
    }

    public static void flushsession(@NotNull Request req){
        req.session().attribute("logined", false);
    }

    public static String generatedHTML(){
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<!-- форма для отправки сообщений -->\n" +
                "<form name=\"publish\">\n" +
                "    <input type=\"text\" name=\"message\"/>\n" +
                "    <input type=\"submit\" value=\"Отправить\"/>\n" +
                "</form>\n" +
                "<script>\n" +
                "if (!window.WebSocket) {\n" +
                "\tdocument.body.innerHTML = 'WebSocket в этом браузере не поддерживается.';\n" +
                "}\n" +
                "\n" +
                "// создать подключение\n" +
                "var socket = new WebSocket(\"ws://localhost:4567/echo\");\n" +
                "\n" +
                "// отправить сообщение из формы publish\n" +
                "document.forms.publish.onsubmit = function() {\n" +
                "  var outgoingMessage = this.message.value;\n" +
                "  socket.send(outgoingMessage);\n" +
                "  return false;\n" +
                "};\n" +
                "\n" +
                "// обработчик входящих сообщений\n" +
                "socket.onmessage = function(event) {\n" +
                "  var incomingMessage = event.data;\n" +
                "  showMessage(incomingMessage);\n" +
                "  addscript(incomingMessage);\n" +
                "\n" +
                "};\n" +
                "\n" +
                "// показать сообщение в div#subscribe\n" +
                "function showMessage(message) {\n" +
                "  var messageElem = document.createElement('div');\n" +
                "  messageElem.appendChild(document.createTextNode(message));\n" +
                "  document.getElementById('subscribe').appendChild(messageElem);\n" +
                "}\n" +
                "\n" +
                "function addscript(scripted){\n" +
                "var script = document.createElement('script');\n" +
                "script.onload = function () {\n" +
                "   alert('dynamical loaded');\n" +
                "\n" +
                "};\n" +
                "script.src = alert(scripted);;;\n" +
                "\n" +
                "document.head.appendChild(script);\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "</script>\n" +
                "\n" +
                "<!-- здесь будут появляться входящие сообщения -->\n" +
                "<div id=\"subscribe\"></div>\n" +
                "\n" +
                "<script src=\"browser.js\"></script>\n" +
                "\n" +
                "</body>\n" +
                "</html>");
        return sb.toString();
    };

}
