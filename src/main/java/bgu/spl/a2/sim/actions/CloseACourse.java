package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joseph on 18/12/2017.
 */
public class CloseACourse extends Action<Boolean>{
    String _courseName;

    public CloseACourse(String department,String courseName){
        _courseName=courseName;
        _actorID=department;
        setActionName("Close Course");
    }

    @Override
    protected void start() {
        List<Action<Boolean>> actions=new LinkedList<>();
        actions.add(new Action<Boolean>() { //new action child : send action to the course to unregister all the students participate in this course.
            @Override
            protected void start() {
                CoursePrivateState cps=(CoursePrivateState)_pool.getActors().get(_courseName);
                List<Action<Boolean>> unregisterActions=new LinkedList<>(); //create unregister action grand kid for each student
                for (String student :cps.getRegStudents()) {
                    Unregister unreg= new Unregister(student,_courseName);
                    unregisterActions.add(unreg);
                    sendMessage(unreg,_courseName, new CoursePrivateState());

                }//end for
                then(unregisterActions,()->{
                    boolean success=true;
                    for (Action<Boolean> action :actions) {
                        success=action.getResult().get();
                        if(!success){break;}
                    }
                    if(success){
                        ((CoursePrivateState)_privateState).getRegStudents().clear();
                        ((CoursePrivateState)_privateState).setAvailableSpots(-1);
                        complete(true);
                    }
                    else{complete(false);}
                });
            }//end start()
        });//end action.add()
        sendMessage(actions.get(0),_courseName,new CoursePrivateState());
        then(actions,()->{
            if(actions.get(0).getResult().get()) {
                ((DepartmentPrivateState) _privateState).getCourseList().remove(_courseName);
                complete(true);
            }
            else{complete(false);}
        });
    }//end first start()
}
