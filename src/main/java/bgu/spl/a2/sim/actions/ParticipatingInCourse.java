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
         List<Action<Boolean>> actions = new LinkedList<>();
         String course = _actorID;
         Integer grade = _grade;
         List<String> pre = ((CoursePrivateState) _privateState).getPrequisites();
         actions.add(new Action<Boolean>() { // new action child : add the course to student's grades list.

             @Override
             protected void start() {
                 StudentPrivateState sps = (StudentPrivateState) _pool.getActors().get(_studentId);
                 boolean canRegister = true;
                 for (String prerequisite : pre) {
                     if (!sps.getGrades().containsKey(prerequisite)) {
                         canRegister = false;
                         break;
                     }
                 }
                 if (canRegister) {
                     complete(true);
                 } else {
                     complete(false);//in case student doesn't meet the prerequisites
                 }
             }
         });
         sendMessage(actions.get(0), _studentId, new StudentPrivateState());
         then(actions, () -> {
             if (actions.get(0).getResult().get()) {
                 if (((CoursePrivateState) _privateState).inc()) { //if there are available spots
                     List<Action<Boolean>> actions2 = new LinkedList<>();
                     actions2.add(new Action<Boolean>() {
                         @Override
                         protected void start() {
                             StudentPrivateState sps = (StudentPrivateState) _pool.getActors().get(_studentId);
                             sps.getGrades().put(course, grade);
                             complete(true);
                         }
                     });
                     sendMessage(actions2.get(0), _studentId, new StudentPrivateState());
                     then(actions2, () -> {
                         if(((CoursePrivateState) _privateState).getRegStudents().contains(_studentId)){
                             //if student already exists in the course, put back the action in the thread pool
                             complete(false);
                             ParticipatingInCourse newAction= new ParticipatingInCourse(_studentId, _actorID, _grade);
                             _privateState.getLogger().remove(_privateState.getLogger().size()-1);
                             sendMessage(newAction, _actorID, new CoursePrivateState());
                         }
                         else {
                             ((CoursePrivateState) _privateState).addStudent(_studentId);
                             complete(true);
                         }
                     });
                 } else {
                     complete(false); //in case there are no available spots
                 }
             } else {
                 complete(false); //in case the previous action returned false
             }
         });

    }

}
