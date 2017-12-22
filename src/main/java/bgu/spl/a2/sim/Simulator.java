/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.util.HashMap;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import com.google.gson.Gson;
import bgu.spl.a2.Gson.Computer;
import bgu.spl.a2.Gson.Action;
import bgu.spl.a2.Gson.Reader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

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
		for (Computer comp:jsonInput.getComputers() ) {
			bgu.spl.a2.sim.Computer newComputer= new bgu.spl.a2.sim.Computer(comp.getType());
			newComputer.setFailSigSig(comp.getSigFail());
			newComputer.setSuccessSig(comp.getSigSuccess());
			computers.add(newComputer);
		}
		Warehouse warehouse=Warehouse.getInstance(computers);
		//parsing json Actions
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
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	
	
	public static void main(String [] args){
		Gson gson = new Gson();
		Type type = new TypeToken<Reader>() {}.getType();
		try{
			//JsonReader jReader = new JsonReader(new FileReader(args[0]));
			Reader reader= gson.fromJson(new FileReader(args[0]), Reader.class);
			System.out.println("Reader");
			ActorThreadPool atp= new ActorThreadPool(Integer.parseInt(reader.getThreads()));
			attachActorThreadPool(atp);
			jsonInput=reader;
			start(); //calling start()

		}
		catch(FileNotFoundException e){
			System.out.println("Can't parse JSON file");
		}
	}
}
