package DSL.DSLProcessor

import java.util

import DSL.abstractions.Processor
import spark.{ModelAndView, Request}
import spark.template.velocity.VelocityTemplateEngine

class Temp extends Processor{
  var model: util.Map[String, AnyRef] = new util.HashMap[String, AnyRef]
  def result(foo: String, bar: String, Function:(String, String) => String): String={
    val result = Function(foo, bar)
    s"""<h1>$foo</h1><br>h1>$bar</h1><br>$result"""
  }

  def marinafunc(foo: String, bar: String): String="Σ="+String.valueOf(Integer.valueOf(foo)+Integer.valueOf(bar))
  def guest(a: String, b: String): String=""


  override def process(request: Request, DSL: String): AnyRef = {
    val foo = "2"
    print(String.valueOf(Integer.valueOf(foo)))

    val bar ="5"
    val user = "marina"
  if (user .equals("marina")) {
    model.put("result",result(foo, bar, marinafunc))
  }
  else    model.put("result",result(foo, bar, guest))
    model.put("foo", foo)
    model.put("bar", bar)

    return new VelocityTemplateEngine().render(new ModelAndView(model, "temp.html"))

}

}