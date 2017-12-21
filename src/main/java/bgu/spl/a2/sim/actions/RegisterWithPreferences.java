package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joseph on 21/12/2017.
 */
public class RegisterWithPreferences extends Action<Boolean> {
    private List<String> _prefrences, _grades;

    public RegisterWithPreferences(String studentId, List<String> prefrences, List<String> grades) {
        _actorID=studentId;
        _prefrences= prefrences;
        _grades= grades;
    }

    @Override
    protected void start() {
        boolean success=false;
        for (int i=0;i<_prefrences.size();i++)
        {
            ParticipatingInCourse action=new ParticipatingInCourse(_actorID, _prefrences.get(i),new Integer(_grades.get(i)));
            sendMessage(action,_prefrences.get(i),new CoursePrivateState());
            List<Action<Boolean>> list=new LinkedList<>();
            list.add(action);
            then(list,()->{
                if (list.get(0).getResult().get())
                {
                    complete(true);
                }
            });
            if(getResult().get()!=null && getResult().get()){
                success=true;
                break;
            }
        }
        if(!success){complete(success);}
    }
}
