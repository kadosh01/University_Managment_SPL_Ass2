package bgu.spl.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Joseph on 06/12/2017.
 */
public class PromiseTest {
    private Promise<Integer> promise;
    private int check;

    @Before
    public void setUp() throws Exception {
    promise=new Promise<>();
    check=0;
    }

    @After
    public void tearDown() throws Exception {
        promise=null;
        check=0;
    }

    @Test
    public void get() throws Exception {
        promise.resolve(2);
        assertEquals(promise.get(),new Integer(2));
    }

    @Test
    public void getButNotResolved() throws Exception {
        try{
            promise.get();
            fail("Exception expected");
        }
        catch (IllegalStateException e){ System.out.print(("IllegalStateException was thrown"));}
        catch(Exception e){fail("Unexpacted exception");}
    }

    @Test
    public void isResolved() throws Exception {
        try {
            assertFalse(promise.isResolved());
        }
        catch (Exception e){fail("Unexpacted exception");}
        try {
            promise.resolve(5);
            assertTrue(promise.isResolved());
        }
        catch (Exception e){fail("Unexpacted exception");}
    }

    @Test
    public void resolveMoreThenOnce() throws Exception {
        try {
            promise.resolve(2);
        }
        catch (Exception e){fail("Unexpacted exception");}
            try {
            promise.resolve(5);
            fail("Exception expected");
        }
        catch (IllegalStateException e){ System.out.print(("IllegalStateException was thrown"));}
        catch (Exception e){fail("Unexpacted exception");}
    }

    @Test
    public void resolveOnce() throws Exception {
        try {
            promise.resolve(2);
            assertEquals(promise.get(),new Integer(2));
        }
        catch (Exception e){fail("Unexpacted exception");}
    }


    @Test
    public void subscribe() throws Exception {
        try{
            promise.subscribe(()->{check++;});
            assertEquals(0,check);
            promise.resolve(2);
            assertEquals(1,check);
        }
        catch(Exception e){fail("Unexpacted exception");}
    }

}