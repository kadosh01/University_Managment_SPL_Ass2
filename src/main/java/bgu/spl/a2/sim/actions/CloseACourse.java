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
        UnregStudents unregStudents=new UnregStudents(_courseName);
        actions.add(unregStudents); //new action child : send action to the course to unregister all the students participate in this course.

        sendMessage(actions.get(0),_courseName,new CoursePrivateState());
        then(actions,()->{

            if(actions.get(0).getResult().get()) {
                ((DepartmentPrivateState)_privateState).getCourseList().remove(_courseName);
                complete(true);
            }
            else{complete(false);}
        });
    }//end first start()
}
