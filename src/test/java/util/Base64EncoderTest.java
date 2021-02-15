package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class Base64EncoderTest {
    Base64Encoder enc = new Base64Encoder();
    String exampleEncoded = "eyJEYXRlIjogIjIwMjAtMTEtMDUiLCAiTW9kZSI6ICLQn9GA0LjQtdC80LrQsCIsICJUYXJhIjogIjAuMCIsICJUaW1lIjogIjE0OjE2OjI2IiwgIk5ldHRvIjogIjAuOTkiLCAiVHJhc2giOiAiMC4xIiwgIkJydXR0byI6ICIxLjEiLCAiTWV0YWxsIjogItCd0LXRgNC20LDQstC10LnQutCwIiwgIkNvbW1lbnQiOiAi0LLQvtC50YIiLCAiQ2xvZ2dpbmciOiAiMS4wIiwgIkNvbXBsZXRlIjogItCU0LAiLCAiQ29uZGl0aW9uIjogItCS0YvQs9GA0YPQttC10L0iLCAiV2F5YmlsbF9udW1iZXIiOiAiOCJ9\n";

    String etalon= "{\"a\": 6, \"first_name\" : \"Sammy\", \"last_name\" : \"Shark\", \"location\" : \"Ocean\"}\n";
    String initial = "eyJhIjogNiwgImZpcnN0X25hbWUiIDogIlNhbW15IiwgImxhc3RfbmFtZSIgOiAiU2hhcmsiLCAibG9jYXRpb24iIDogIk9jZWFuIn0K";

 //   @Test
    public void decode() {
        assertEquals(etalon, enc.decode(initial));
    }

   // @Test
    public void encode(){
        assertEquals(initial, enc.encode(etalon));
        System.out.println(enc.encode("{\"Date\": \"2020-11-05\", \"Mode\": \"Приемка\", \"Tara\": \"0.0\", \"Time\": \"14:16:26\", \"Netto\": \"0.99\", \"Trash\": \"0.1\", \"Brutto\": \"1.1\", \"Metall\": \"Нержавейка\", \"Comment\": \"войт\", \"Clogging\": \"1.0\", \"Complete\": \"Да\", \"Condition\": \"Выгружен\", \"Waybill_number\": \"8\"}"));

    }

   // @Test
    public void encode2(){
        assertEquals(initial, enc.encode(etalon));
        String coded = "{\"a\": 6, \"first_name\" : \"Sammy\", \"last_name\" : \"дроздов\", \"location\" : \"Ocean\"}";
        System.out.println(coded);
        System.out.println(enc.encode("{\"a\": 6, \"first_name\" : \"Sammy\", \"last_name\" : \"дроздов\", \"location\" : \"Ocean\"}"));

    }

    @Test
    public void compare(){
       String viaBouncy = "eyJhIjogNiwgImZpcnN0X25hbWUiIDogIlNhbW15IiwgImxhc3RfbmFtZSIgOiAi0LTRgNC+0LfQtNC+0LIiLCAibG9jYXRpb24iIDogIk9jZWFuIn0=";
       String viaSite =   "eyJhIjogNiwgImZpcnN0X25hbWUiIDogIlNhbW15IiwgImxhc3RfbmFtZSIgOiAi0LTRgNC+0LfQtNC+0LIiLCAibG9jYXRpb24iIDogIk9jZWFuIn0=";
       String viaSitecp1251 = "eyJhIjogNiwgImZpcnN0X25hbWUiIDogIlNhbW15IiwgImxhc3RfbmFtZSIgOiAi5PDu5+Tu4iIsICJsb2NhdGlvbiIgOiAiT2NlYW4ifQ==";
       String viaJS = "eyJhIjogIjYiLCAiZmlyc3RfbmFtZSIgOiAiU2FtbXkiLCAibGFzdF9uYW1lIiA6ICLQtNGA0L7Qt9C00L7QsiIsICJsb2NhdGlvbiIgOiAiT2NlYW4ifQ==";
       assertEquals(viaBouncy, viaSite);

    }


}