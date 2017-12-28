package bgu.spl.a2.sim.privateStates;

import java.util.HashMap;

import bgu.spl.a2.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState implements java.io.Serializable{

	private HashMap<String, Integer> grades;
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState() {
		grades= new HashMap<String, Integer>();
	}

	public HashMap<String, Integer> getGrades() {
		return grades;
	}

	public Integer getGrade(String course) {
		return grades.get(course);
	}

	public long getSignature() {
		return signature;
	}

	public void setSignature(long sig){
		signature= sig;
	}


}
