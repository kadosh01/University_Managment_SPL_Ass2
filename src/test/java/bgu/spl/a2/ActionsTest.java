package bgu.spl.a2;

import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.actions.OpenCourse;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joseph on 22/12/2017.
 */
public class ActionsTest {
    private ActorThreadPool _pool;
    private Warehouse _warehouse;
    private final int _numOfComputers=3;

    @Before
    public void setUp() throws Exception {
        _pool=new ActorThreadPool(5);
        List<Computer> computers=new LinkedList<>();
        for (int i=0;i<_numOfComputers;i++){
            computers.add(new Computer(String.valueOf((char)(i + 64))));
        }
        _warehouse=Warehouse.getInstance(computers);
    }


    @Test
    public void OpenCourse()
    {

    }
}
