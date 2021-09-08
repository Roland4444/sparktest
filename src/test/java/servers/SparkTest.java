package servers;

import org.junit.Test;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static servers.Spark.model;

public class SparkTest {

    @Test
    public void stringcompore(){
        byte[] Arr = new byte[]{(byte) 0xff, (byte) 0xcf};
        String str1 = new String(Arr, Charset.forName("Windows-1251"));
        String str3 = new String(Arr, Charset.forName("Windows-1251"));
        String str2 = "bla";
        byte[] Arr2 = str1.getBytes(Charset.forName("Windows-1251"));
        assertEquals(Arr[0], Arr2[0]);
        assertEquals(Arr[1], Arr2[1]);
        assertTrue(str1.equals(str3));
    }

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