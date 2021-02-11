package DSL.abstractions

import spark.Request

trait Processor {
  def process(request: Request, DSL: String): AnyRef
}
