package bgu.spl.a2;

import bgu.spl.a2.sim.actions.Transmission;
import bgu.spl.a2.sim.privateStates.BankStates;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class ActorThreadPoolTest {

    @Test
    public void submit() {

    }


    @Test
    public void shutdown() {
    }

    @Test
    public void start() {

        ActorThreadPool pool = new ActorThreadPool(2);
        Action<String> trans = new Transmission(100, "A","B","bank2", "bank1");
        pool.start();
        pool.submit(trans, "bank1", new BankStates());
        CountDownLatch e = new CountDownLatch(1);
        System.out.println("CountDownLatch started");
        trans.getResult().subscribe(()-> {
            e.countDown();
        });
        System.out.println("action subscribed");
        try{
            System.out.println("Try await");
            e.await();
        }
        catch (InterruptedException ex){}
        try{
            System.out.println("Try shutdown");
            pool.shutdown();
        }
        catch (InterruptedException ex){}
    }
}
