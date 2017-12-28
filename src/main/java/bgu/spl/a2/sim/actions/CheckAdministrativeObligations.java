package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joseph on 20/12/2017.
 */
public class CheckAdministrativeObligations extends Action<Boolean> {
    private List<String> _studentsList;
    private List<String> _conditions;
    private String _computerName;

    public CheckAdministrativeObligations(String departmentName, List<String> students , String computerName, List<String> conditions){
       setActionName("Administrative Check");
        _actorID= departmentName;
        _studentsList= students;
        _conditions= conditions;
        _computerName= computerName;
    }

    @Override
    protected void start() {
        if(Warehouse.getInstance().getMutex(_computerName)!=null) {
            Promise<Computer> promise = Warehouse.getInstance().getMutex(_computerName).down();
            promise.subscribe(() -> {
                List<Action<Boolean>> actions = new LinkedList<Action<Boolean>>();
                for (String id : _studentsList) {
                    Action<Boolean> act = new Action<Boolean>() {
                        @Override
                        protected void start() {
                            StudentPrivateState sps = (StudentPrivateState) _pool.getActors().get(id);
                            Computer comp = promise.get();
                            sps.setSignature(comp.checkAndSign(_conditions, sps.getGrades()));
                            complete(true);
                        }
                    };
                    actions.add(act);
                    sendMessage(act, id, new StudentPrivateState());
                }
                then(actions, () -> {
                    complete(true);
                    Warehouse.getInstance().getMutex(_computerName).up();
                });
            });
        }
        else{
            complete(false); //computer does'nt exist
        }
    }
}
