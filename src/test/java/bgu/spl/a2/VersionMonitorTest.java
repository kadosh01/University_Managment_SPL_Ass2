package bgu.spl.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Joseph on 05/12/2017.
 */
public class VersionMonitorTest {
    private VersionMonitor versionMonitor;
    private Thread t1,t2;

    @Before
    public void setup(){
        versionMonitor = new VersionMonitor();
        t1=new Thread(()->{
            try
            {
                versionMonitor.await(versionMonitor.getVersion());
            }
            catch (InterruptedException e){
                System.out.print(("Thread was interrupt"));}
            catch (Exception e){
                fail("Unexpected exception");}
        });

        t2=new Thread(()->{
            try{
                versionMonitor.inc();
            }
            catch (Exception e){fail("Unexpected exception");}
        });


    }
    @After
    public void tearDown() throws Exception {
        System.out.println("Running: tearDown");
        versionMonitor = null;
        t1.interrupt();
        t2.interrupt();
        assertNull(versionMonitor);
    }

    @Test
    public void getVersion() throws Exception {
        System.out.println("Running: Test for getVersion()");
        assertEquals(0,versionMonitor.getVersion());
    }

    @Test
    public void inc() throws Exception {
        System.out.println("Running: Test for inc()");
        versionMonitor.inc();
        assertEquals(1, versionMonitor.getVersion());
        versionMonitor.inc();
        versionMonitor.inc();
        versionMonitor.inc();
        assertEquals(4, versionMonitor.getVersion());
    }
    /**
     * Test Method for {@link VersionMonitor#await(int)} waiting until interrupted
     *
     */
    @Test
    public void awaitInterruptTest() throws Exception {
              t1.start();
              t2.start();
              assertEquals(Thread.State.TERMINATED,t1.getState());
    }
    /**
     * Test Method for {@link VersionMonitor#await(int)} waits because the version never increased
     *
     */
    @Test
    public void awaitWaitTest() throws Exception {
        t1.start();
        assertEquals(Thread.State.WAITING,t1.getState());
    }
}