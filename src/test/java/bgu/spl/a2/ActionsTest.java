package bgu.spl.a2;

import bgu.spl.a2.Gson.*;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Joseph on 22/12/2017.
 */
public class ActionsTest {
    private ActorThreadPool _pool;
    private Warehouse _warehouse;
    private final int _numOfComputers=3;
    private final String gsonPath="C://Users//Joseph//.IntelliJIdea2017.1//Projects//a2//input-example.json";
    private Reader r;
    private List<bgu.spl.a2.Action> phase1;
    private List<bgu.spl.a2.Action> phase2;
    private List<bgu.spl.a2.Action> phase3;
    @Before
    public void setUp() throws Exception {

        List<Computer> computers=new LinkedList<>();

        _warehouse=Warehouse.getInstance(computers);
        Gson gson = new Gson();
        Type type = new TypeToken<Reader>() {}.getType();
        try{
            //JsonReader jReader = new JsonReader(new FileReader(args[0]));
            Reader reader= gson.fromJson(new FileReader(gsonPath), Reader.class);
            r=reader;
        }
        catch(FileNotFoundException e){
            System.out.println("Can't parse JSON file");
        }
        _pool=new ActorThreadPool(Integer.parseInt(r.getThreads())); // thread pool
         for (int i=0;i<r.getComputers().size();i++){
            computers.add(new Computer(r.getComputers().get(i).getType()));
            computers.get(i).setSuccessSig(r.getComputers().get(i).getSigSuccess());
            computers.get(i).setFailSigSig(r.getComputers().get(i).getSigFail());
        }
        phase1=new LinkedList<>();
        phase2=new LinkedList<>();
        phase3=new LinkedList<>();
        parsing();


    }


    @Test
    public void parse1()
    {
    for (bgu.spl.a2.Action act :phase1){
        _pool.submit(act,act._actorID,act._privateState );
    }
        //check if there is DS department actor
        try {
            assertEquals(true, _pool.getActors().containsKey("CS"));
            System.out.println("check if there is DS department actor : Passed");
        }
        catch (AssertionError ae) {
            System.out.println("check if there is DS department actor : Failed");
        }

        //check if all the action are submitted in the correct order in the logger.
        try {
        for(int i=0;i<phase1.size();i++){
         assertEquals(phase1.get(i)._actionName,_pool.getActors().get("CS").getLogger().get(i));
        }
         System.out.println("check if all the action are submitted in the correct order in the logger. : Passed");
        }
        catch (AssertionError ae) {
            System.out.println("check if all the action are submitted in the correct order in the logger. : Passed");
        }
        //check if the threads catch an action and execute it
        _pool.start();
        try {
            _pool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void parse2()
    {

    }

    private void parsing(){
        for(int i=0;i<3;i++) {
            List<Action> list=new LinkedList<>();
            if(i==0) {list=phase1;}
            if(i==1) {list=phase2;}
            if(i==2) {list=phase3;}
            List<List<bgu.spl.a2.Gson.ActionParsing>> phases=new LinkedList<>();
            phases.add(r.getPhase1());
            phases.add(r.getPhase2());
            phases.add(r.getPhase3());
            for (bgu.spl.a2.Gson.ActionParsing act : phases.get(i)) {
                switch (act.getAction()) {
                    case ("Open Course"): {
                        OpenCourse action = new OpenCourse(Integer.parseInt(act.getSpace()), act.getPrerequisites(), act.getCourse(), act.getDepartment());
                        list.add(action);
                        break;
                    }
                    case ("Add Student"): {
                        AddStudent action = new AddStudent(act.getStudent(), act.getDepartment());
                        list.add(action);
                        break;
                    }
                    case ("Participate In Course"): {
                        ParticipatingInCourse action = new ParticipatingInCourse(act.getStudent(), act.getCourse(), new Integer(act.getGrade().get(0)));
                        list.add(action);
                        break;
                    }
                    case ("Register With Preferences"): {
                        RegisterWithPreferences action = new RegisterWithPreferences(act.getStudent(), act.getPreferences(), act.getGrade());
                        list.add(action);
                        break;
                    }
                    case ("Unregister"): {
                        Unregister action = new Unregister(act.getStudent(), act.getCourse());
                        list.add(action);
                        break;
                    }
                    case ("Close Course"): {
                        CloseACourse action = new CloseACourse(act.getDepartment(), act.getCourse());
                        list.add(action);
                        break;
                    }
                    case ("Administrative Check"): {
                        CheckAdministrativeObligations action = new CheckAdministrativeObligations(act.getDepartment(), act.getStudents(), act.getComputer(), act.getConditions());
                        list.add(action);
                        break;
                    }
                    default:
                }
            }
        }
    }
}
