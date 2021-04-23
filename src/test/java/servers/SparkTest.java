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

    @Test
    public void  uncodechar(){
        var f = new char[]{'\u041f','\u0417','\u0423', 2 ,'\u0411','\u0410','\u0411'};
        String j = "\u041f\u0417\u0423 2 \u0411\u0410\u0411\u0410\u0415\u0412\u0421\u041a\u041e\u0413\u041e \u0426\u041c";
        System.out.println(j);
        System.out.println(f);



    }
}