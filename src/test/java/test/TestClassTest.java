package test;

import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.*;

public class TestClassTest {

    @Test
    public void sum() {
        assertEquals(2, TestClass.sum(2,0).intValue());
        BiFunction<Integer, Integer> = TestClass::sum;
        assertNotEquals(null, TestClass.compute(TestClass::sum,4));
        System.out.println("COMPUTE=>"+ TestClass.compute(TestClass::sum,4));
    }
}