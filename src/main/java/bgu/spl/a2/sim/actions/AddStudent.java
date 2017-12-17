package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddStudent extends Action<Boolean>{

    public AddStudent(String studentName){
        _actionName= studentName;
    }

    @Override
    protected void start() {
        StudentPrivateState sps= new StudentPrivateState();
        sendMessage(null, _actionName, _privateState);
        //((DepartmentPrivateState)_privateState)
    }
}
