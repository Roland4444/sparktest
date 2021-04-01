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

    @Test
    public void check() {
       // assertEquals("3A", "3А");//english at rigth
        assertEquals("3А", "3А");  //russian at rigth
    }
}