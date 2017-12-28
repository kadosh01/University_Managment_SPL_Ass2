package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Joseph on 21/12/2017.
 */
public class RegisterWithPreferences extends Action<Boolean> {
    private List<String> _prefrences, _grades;

    public RegisterWithPreferences(String studentId, List<String> prefrences, List<String> grades) {
        _actorID=studentId;
        _prefrences= prefrences;
        _grades= grades;
        setActionName("Register With Preferences");
    }

    @Override
    protected void start() {
        List<Action<Boolean>> actionsList= new LinkedList<>();
        if(_prefrences.size()<=0 || _grades.size()<=0)
        {complete(false);}
        else {
            if(_grades.get(0).equals("-")){_grades.remove(0);_grades.add(0,"-1");}
            ParticipatingInCourse action = new ParticipatingInCourse(_actorID, _prefrences.get(0), new Integer(Integer.parseInt(_grades.get(0))));
            actionsList.add(action);
            List<String> preferences = _prefrences;
            List<String> grades = _grades;
            sendMessage(action, _prefrences.get(0), new CoursePrivateState());
            then(actionsList, () -> {
                if (action.getResult().get()) {
                    complete(true);
                } else {
                    if (_prefrences.size() <= 0 || _grades.size() <= 0) {
                        complete(false);
                    } else {
                        preferences.remove(0);
                        grades.remove(0);
                        complete(false);
                        RegisterWithPreferences newAction = new RegisterWithPreferences(_actorID, preferences, grades);
                        _privateState.getLogger().remove(_actionName);
                        sendMessage(newAction, _actorID, new StudentPrivateState());
                    }
                }
            });
        }
    }
}
