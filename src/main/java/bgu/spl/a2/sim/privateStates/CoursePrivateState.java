package bgu.spl.a2.sim.privateStates;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState implements java.io.Serializable{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {

		registered= new Integer(0);
		availableSpots= new Integer(0);
		regStudents= new LinkedList<String>();
		prequisites= new LinkedList<String>();
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}

	public void setAvailableSpots(int num){
		availableSpots= new Integer(num);
	}

	public void setPrerequisites(List<String> pre){
		prequisites= pre;
	}

	 public boolean inc(){
		if(availableSpots>0) {
			registered = new Integer(registered.intValue() + 1);
			availableSpots= new Integer(availableSpots.intValue()-1);
			return true;
		}
		return false;
	}
	public boolean dec(){
		if(registered>0) {
			registered = new Integer(registered.intValue() - 1);
			availableSpots= new Integer(availableSpots.intValue()+1);
			return true;
		}
		return false;
	}

	public void addStudent(String studentName){
		if(!regStudents.contains(studentName)){
			regStudents.add(studentName);
		}
	}
	public void removeStudent(String studentName){
		if(regStudents.contains(studentName))
			regStudents.remove(studentName);
	}
}
