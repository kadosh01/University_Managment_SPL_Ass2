package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joseph on 17/12/2017.
 */
public class ParticipatingInCourse extends Action<Boolean> {
    protected String _studentId;
    protected Integer _grade;

    public ParticipatingInCourse(String studentId,String courseName,Integer grade)
    {
        setActionName("Participate In Course");
        _actorID=courseName;
        _grade=grade;
        _studentId=studentId;
    }

    @Override
    protected void start() {
        if (((CoursePrivateState)_privateState).inc()) { //check if there is available place in the course.
            List<Action<Boolean>> actions = new LinkedList<>();
            actions.add(new Action<Boolean>() { // new action child : add the course to student's grades list.
                @Override
                protected void start() {
                    StudentPrivateState sps=(StudentPrivateState)_pool.getActors().get(_studentId);
                    List<String> pre= ((CoursePrivateState) _privateState).getPrequisites();
                    boolean canRegister= true;
                    for(String prerequisite: pre){
                        if(!sps.getGrades().containsKey(prerequisite)){
                            canRegister=false;
                            break;
                        }
                    }
                    if(canRegister) {
                        sps.getGrades().put(_actorID, _grade);
                        complete(true);
                    }
                    else{
                        complete(false);
                    }
                }
            });
            sendMessage(actions.get(0),_studentId,new StudentPrivateState());
            then(actions,()->{
                if(actions.get(0).getResult().get()){
                    ((CoursePrivateState) _privateState).addStudent(_studentId);
                    complete(true);
                }
                else {
                    ((CoursePrivateState) _privateState).dec();
                    complete(false);
                }
            });
        }//END IF

    }

}
