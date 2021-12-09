package util.processors;

import abstractions.Condition;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static util.processors.InputRequestProcessor.transformlongString;

public class InputRequestProcessorTest {
    InputRequestProcessor irp = new InputRequestProcessor();
    OutputResponceProcessor orp = new OutputResponceProcessor();
    public String filename = "temp.bin";
    public String id ="12";
    public Condition cond = Condition.APPROVED;
    @Test
    public void getStatus() throws IOException {
        orp.saveStatus(filename, id, cond);
        Condition rest =irp.getStatus(id, filename);
        assertEquals(cond, rest);
        assertEquals(null, irp.getStatus("ioioio", "fdjghjfdhgjdfhgfdj"));
    }


    @Test
    public void removeStatus() throws IOException {
        orp.saveStatus(filename, id, cond);
        Condition rest =irp.getStatus(id, filename);
        assertEquals(cond, rest);
        assertEquals(null, irp.getStatus("ioioio", "fdjghjfdhgjdfhgfdj"));
        irp.removeStatus(id, filename);
        assertEquals(null, irp.getStatus("ioioio", filename));

    }

    @Test
    public void transformlongStringtest() {
        String input222 = "realylongcommentary";
        String etalon = "realy<br>longc<br>ommen<br>tary";
        assertEquals(etalon, transformlongString(input222,5));
    }

    boolean check(String S){
        if (S==null)
            return false;
        if (S.length()<=2)
            return false;
        return true;
    }

    @Test
    public void testDumpRequestToHTMLTableReact() {
        ArrayList<String> Arr = new ArrayList(Arrays.asList(new String[]{"aa","bbc", "ccb", null, null}));
        ArrayList<String> ArrEtalon = new ArrayList(Arrays.asList(new String[]{"bbczxzc", "ccbzxzc"}));
        Arr.stream().filter(a->check(a)).forEach(System.out::println);
        List Lst = Arr.stream().filter(a->check(a)).collect(
                        ()->new ArrayList<String>(),
                        (list, item)->list.add(item+"zxzc"),
                        (list1, list2)-> list1.addAll(list2));
        System.out.println("""
                dfdsfsdfsf
                """);
        Lst.stream().forEach(System.out::println);
        assertEquals(ArrEtalon, Lst);

    }
}