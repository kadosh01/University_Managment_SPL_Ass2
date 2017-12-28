/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.util.HashMap;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.Gson.ActionParsing;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import com.google.gson.Gson;
import bgu.spl.a2.Gson.Computer;
import bgu.spl.a2.Gson.Reader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	private static Reader jsonInput;
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
		List<bgu.spl.a2.sim.Computer> computers = new LinkedList<>();
		//parsing json computer
		if(jsonInput.getComputers()!=null) {
			for (Computer comp : jsonInput.getComputers()) {
				bgu.spl.a2.sim.Computer newComputer = new bgu.spl.a2.sim.Computer(comp.getType());
				newComputer.setFailSigSig(comp.getSigFail());
				newComputer.setSuccessSig(comp.getSigSuccess());
				computers.add(newComputer);
			}
		}
		Warehouse warehouse=Warehouse.getInstance(computers);
		//parsing json Actions
		List<List<ActionParsing>> flow= new LinkedList<>();
		flow.add(jsonInput.getPhase1());
		flow.add(jsonInput.getPhase2());
		flow.add(jsonInput.getPhase3());

		actorThreadPool.start();
		int i=0;
		for(List<ActionParsing> phase : flow) {
			int counter = phase.size();
			CountDownLatch count = new CountDownLatch(counter);
			for (ActionParsing act : phase) {
				switch (act.getAction()) {
					case("Open Course"):
					{
						Action openCourse= new OpenCourse(Integer.parseInt(act.getSpace()), act.getPrerequisites(), act.getCourse(), act.getDepartment());
						actorThreadPool.submit(openCourse, act.getDepartment(), new DepartmentPrivateState());
						openCourse.getResult().subscribe(()->{
							count.countDown();
						});
						break;
					}
					case("Add Student"):
					{
						Action addStudent= new AddStudent(act.getStudent(), act.getDepartment());
						actorThreadPool.submit(addStudent, act.getDepartment(), new DepartmentPrivateState());
						addStudent.getResult().subscribe(()->{
							count.countDown();
						});
						break;
					}
					case("Participate In Course"):
					{
						int grade;
						if(act.getGrade().get(0).equals("-"))
							grade= -1;
						else grade= Integer.parseInt(act.getGrade().get(0));
						Action participate= new ParticipatingInCourse(act.getStudent(), act.getCourse(), grade);
						actorThreadPool.submit(participate, act.getCourse(), new CoursePrivateState());
						participate.getResult().subscribe(()->{
							count.countDown();
						});
						break;
					}
					case("Register With Preferences"):
					{
						Action register= new RegisterWithPreferences(act.getStudent(), act.getPreferences(), act.getGrade());
						actorThreadPool.submit(register, act.getStudent(), new StudentPrivateState());
						register.getResult().subscribe(()->{
							count.countDown();
						});
						break;
					}
					case("Unregister"):
					{
						Action unregister= new Unregister(act.getStudent(), act.getCourse());
						actorThreadPool.submit(unregister, act.getCourse(), new CoursePrivateState());
						unregister.getResult().subscribe(()->{
							count.countDown();
						});
						break;
					}
					case("Close Course"):
					{
						Action close= new CloseACourse(act.getDepartment(), act.getCourse());
						actorThreadPool.submit(close, act.getDepartment(), new DepartmentPrivateState());
						close.getResult().subscribe(()->{
							count.countDown();
						});
						break;
					}
					case("Administrative Check"):
					{
						Action administrative= new CheckAdministrativeObligations(act.getDepartment(), act.getStudents(), act.getComputer(), act.getConditions());
						actorThreadPool.submit(administrative, act.getDepartment(), new DepartmentPrivateState());
						administrative.getResult().subscribe(()->{
							count.countDown();
						});
						break;
					}
					case("Add Spaces"):
					{
						Action NewPlaces= new NewPlacesInCourse(act.getCourse(),Integer.parseInt(act.getNumber()));
						actorThreadPool.submit(NewPlaces, act.getCourse(), new CoursePrivateState());
						NewPlaces.getResult().subscribe(()->{
							count.countDown();
						});
						break;
					}
					default: count.countDown();
				}

			}
			try{
				count.await();
			}
			catch (InterruptedException e){

			}
		}
    }
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool= myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		HashMap<String, PrivateState> result=new HashMap<>();
		try {
			actorThreadPool.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(ConcurrentHashMap.Entry<String,PrivateState> entry : actorThreadPool.getActors().entrySet()){
			result.put(entry.getKey(),entry.getValue());
		}

		return  result;

	}

	public static void main(String [] args){
		Gson gson = new Gson();
		try{
			Reader reader= gson.fromJson(new FileReader(args[0]), Reader.class);
			ActorThreadPool atp= new ActorThreadPool(Integer.parseInt(reader.getThreads()));
			attachActorThreadPool(atp);
			jsonInput=reader;
			start();

		}
		catch(FileNotFoundException e){
			System.out.println("Can't parse JSON file");
		}
		//try with resources
		try(FileOutputStream fout=new FileOutputStream("result.ser");ObjectOutputStream oos=new ObjectOutputStream(fout);){

			oos.writeObject(end());

		}
		catch (IOException e){System.out.println(e.getMessage());}

	}
}
