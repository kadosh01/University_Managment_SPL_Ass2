package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddStudent extends Action<Boolean>{
    protected String _studentId;

    public AddStudent(String studentId, String departmentName){
        setActionName("Add Student");
        _studentId= studentId;
        _actorID= departmentName;
    }

    @Override
    protected void start() {
        StudentPrivateState sps= new StudentPrivateState();
        sendMessage(null, _studentId, _privateState);
        ((DepartmentPrivateState)_privateState).addStudent(_actionName);
        complete(true);
    }
}
