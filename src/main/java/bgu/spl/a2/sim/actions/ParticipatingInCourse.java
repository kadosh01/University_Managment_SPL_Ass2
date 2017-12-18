package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joseph on 17/12/2017.
 */
public class ParticipatingInCourse extends Action<Boolean> {
    protected String _studentId,_courseName;
    protected Integer _grade;
    public ParticipatingInCourse(String studentId,String courseName,Integer grade)
    {
        setActionName("Participate In Course");
        _courseName=courseName;
        _grade=grade;
        _studentId=studentId;
    }

    @Override
    protected void start() {
        List<Action<Boolean>> actions = new LinkedList<>();
        actions.add(new Action<Boolean>() {
            @Override
            protected void start() {

            }
        });
    }

}
