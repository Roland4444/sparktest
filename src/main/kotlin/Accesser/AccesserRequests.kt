package Accesser

import servers.Spark
import spark.ModelAndView
import spark.template.velocity.VelocityTemplateEngine
import util.Deps

class AccesserRequests {
    companion object {
        /*fun Olga(): String?{

        }*/

        fun access(login: String, deps: Deps): String? {
            when (login) {
                "guest" -> return "у гостей недостатоочно прав";
                "igor" -> return "ACCESS DENIED";
                else -> {
                    print("Into Accesser")
                    Spark.model.clear()
                    Spark.model["requests"] = deps.irp.DumpRequestToHTMLTableReact()
                    val first: Char = Character.toUpperCase(login.get(0))
                    val uppercasedUser = first.toString() + login.substring(1)
                    Spark.model["user"] = uppercasedUser
                    return VelocityTemplateEngine().render(
                        ModelAndView(Spark.model, "requestsx.html")
                    )
                }
            }
        }
    }
}