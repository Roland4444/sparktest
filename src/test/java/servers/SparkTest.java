package servers;

import org.junit.Test;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import static org.junit.Assert.*;
import static servers.Spark.model;

public class SparkTest {

    @Test
    public void generatedHTML() {
        new VelocityTemplateEngine().render(
                new ModelAndView(model, "requestsx.html"));
        System.out.println(new VelocityTemplateEngine().render(
                new ModelAndView(model, "requestsx.html")));
    }
}