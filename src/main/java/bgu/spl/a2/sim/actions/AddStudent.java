package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddStudent extends Action<Boolean>{
    protected String _studentId;
    protected String _departmentName;

    public AddStudent(String studentId, String departmentName){
        setActionName("Add Student");
        _studentId= studentId;
        _actorID=departmentName;
        _departmentName= departmentName;
    }

    @Override
    protected void start() {
        StudentPrivateState sps= new StudentPrivateState();
        sendMessage(new Action<Boolean>() {

            @Override
            protected void start() {

                complete(true);
            }
        }, _studentId, sps);
        ((DepartmentPrivateState)_privateState).addStudent(_studentId);
        complete(true);
    }
}
