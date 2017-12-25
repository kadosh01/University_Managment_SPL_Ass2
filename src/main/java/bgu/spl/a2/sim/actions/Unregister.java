package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class Unregister extends Action<Boolean>{
    protected String _studentId;

    public Unregister(String studentId, String courseName){
        setActionName("Unregister");
        _actorID= courseName;
        _studentId= studentId;
    }

    @Override
    protected void start() {
        if(((CoursePrivateState)_privateState).getRegStudents().contains(_studentId)){
            ((CoursePrivateState)_privateState).dec();
            String course= _actorID;
            List<Action<Boolean>> actions= new LinkedList<>();
            actions.add(new Action<Boolean>() {
                @Override
                protected void start() {
                    StudentPrivateState sps= (StudentPrivateState)_pool.getActors().get(_studentId);
                    sps.getGrades().remove(course);
                    complete(true);
                }
            });
            sendMessage(actions.get(0), _studentId, new StudentPrivateState());
            then(actions, ()->{
               if(actions.get(0).getResult().get()){
                   ((CoursePrivateState)_privateState).removeStudent(_studentId);
                   complete(true);
               }
               else{
                   ((CoursePrivateState)_privateState).inc();
                   complete(false);
               }
            });
        }
        else{
            sendMessage(this, _actorID, new CoursePrivateState());

        }
    }
}
