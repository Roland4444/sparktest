package DSL

import java.util

import DSL.DSLProcessor.Login
import DSL.abstractions.Processor

class DSLMap {
  var MapProcessor: util.Map[String, Processor] = new util.HashMap[String, Processor]
  MapProcessor.put("login", new Login)
}
