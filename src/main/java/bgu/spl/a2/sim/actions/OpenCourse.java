package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.List;


/**
 * Created by Joseph on 17/12/2017.
 */
public class OpenCourse extends Action<Boolean>{
    private int _availableSpaces;
    List<String> _prerequisites;
    private String _courseName;

    public OpenCourse(int availableSpaces, List<String> prerequisites, String courseName, String departmentName)
    {
        _actorID=departmentName;
        _courseName=courseName;
        _availableSpaces=availableSpaces;
        _prerequisites=prerequisites;
        setActionName("Open Course");
    }

    public void start()
    {
        CoursePrivateState cps= new CoursePrivateState();
        cps.setAvailableSpots(_availableSpaces);
        cps.setPrerequisites(_prerequisites);
        sendMessage(new Action<Boolean>() {

            @Override
            protected void start() {
                complete(true);
            }
        }, _courseName, cps);
        ((DepartmentPrivateState)_privateState).addCourse(_courseName);
        complete(true);

    }


}
