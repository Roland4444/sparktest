package test;

import abstractions.TestAction;

import java.io.IOException;

public class TestThread  extends Thread{
    public TestAction testAaction;
    public int delay;
    public TestThread(int delay){
        this.delay = delay;


    }

    public void run(){
        while (true)
        {
            try {
                testAaction.action();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000*delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
