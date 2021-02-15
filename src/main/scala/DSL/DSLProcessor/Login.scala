package DSL.DSLProcessor

import spark.template.velocity.VelocityTemplateEngine
import spark.{ModelAndView, Request}
import java.util
import java.util.ArrayList

import DSL.abstractions.Processor
import servers.Spark


class Login extends Processor
{
//  def getHTML(Data : ArrayList[ArrayList]=> )

  def readonly(): Unit ={
    model.put("requests", Spark.deps.irp.DumpRequestToHTMLTableReact)
  }
  def admin(): Unit ={
    model.put("requests", Spark.deps.irp.DumpRequestToHTMLTableReact)
  }
  var model: util.Map[String, AnyRef] = new util.HashMap[String, AnyRef]
  def process(req: Request, DSL: String): AnyRef = {
    val login: String =req.queryParams("login")
    val pass: String = req.queryParams("password")
    req.session.attribute("logined", true)
    req.session.attribute("user", login)
    model.clear
    DSL match {
      case "READONLY" =>readonly()
      case "ADMIN" =>admin()
    }

    val first: Char = Character.toUpperCase(login.charAt(0))
    val uppercasedUser: String = first + login.substring(1)
    model.put("user", uppercasedUser)
    return new VelocityTemplateEngine().render(new ModelAndView(model, "requestsx.html"))
  }


}


