package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joseph on 28/12/2017.
 */
public class UnregStudents extends Action<Boolean> {

    public UnregStudents(String courseName){
        setActionName("UnregStudents");
        _actorID=courseName;
    }

    @Override
    protected void start() {
        CoursePrivateState cps=(CoursePrivateState)_privateState;
        List<Action<Boolean>> unregisterActions=new LinkedList<>(); //create unregister action for each student
            for (String student : cps.getRegStudents()) {
                Unregister unreg = new Unregister(student, _actorID);
                unregisterActions.add(unreg);
                sendMessage(unreg, _actorID, _privateState);
            }//end for
        then(unregisterActions,()->{
            boolean success=true;
            for (Action<Boolean> action :unregisterActions) {
                success=action.getResult().get();
                if(success==false){break;}
            }
            if(success){
                ((CoursePrivateState)_privateState).getRegStudents().clear();
                ((CoursePrivateState)_privateState).setAvailableSpots(-1);
                complete(true);
            }
            else{complete(false);}
        });

    }
}
