package servers;

import org.jetbrains.annotations.NotNull;
import spark.ModelAndView;
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;
import util.Deps;

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

    public static void main(String[] args) throws InterruptedException, SQLException {
        staticFiles.location("/public");
        Deps deps = new Deps();
        deps.echoWebSocket =  EchoWebSocket.class;

        webSocket("/echo", EchoWebSocket.class);


        get("users", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "users.html"));
        });

        get("react", (req, res) -> {
            model.clear();
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "react.html"));
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

        get("requests", (req, res) -> {
            model.clear();
            model.put("requests", deps.irp.DumpRequestToHTMLTable8());
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "requests.html"));
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
            if (check(req)){
                String id = req.queryParams("id");
                deps.orp.approve(id);
                res.redirect("/requests");


                //return eng.render(OK);
            }
            return eng.render(BAD);
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
        get("/login", (req, res) -> {
            String login = req.queryParams("login");
            String pass = req.queryParams("password");
            if (deps.loginchecker.checklogin(login, pass)) {
                req.session().attribute("logined", true);
                model.clear();
            ///    model.put("requests", deps.irp.DumpRequestToHTMLTable8());
            ///    System.out.println(deps.irp.DumpRequestToHTMLTable8());
                model.put("requests", deps.irp.DumpRequestToHTMLTable8usingmatrixhardcoded());
                return new VelocityTemplateEngine().render(
                        new ModelAndView(model, "requests.html"));
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
            EchoWebSocket.sendall();
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
