package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class NewPlacesInCourse extends Action<Boolean>{

    protected int _additional;

    public NewPlacesInCourse(String courseName, int additional){
        _actorID= courseName;
        _additional= additional;
        setActionName("Add Spaces");
    }

    @Override
    protected void start() {
        int currentSpots= ((CoursePrivateState)_privateState).getAvailableSpots();
        if(currentSpots>-1){
            ((CoursePrivateState)_privateState).setAvailableSpots(currentSpots+_additional);
            complete(true);
        }
        else{
            complete(false); //course is closed, so we can't add spots
        }
    }
}
