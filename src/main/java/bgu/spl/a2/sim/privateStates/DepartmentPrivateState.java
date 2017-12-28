package bgu.spl.a2.sim.privateStates;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe department's private state
 */
public class DepartmentPrivateState extends PrivateState implements Serializable {
	private static final long serialVersionUID = 1L;
	private  List<String> courseList;
	private List<String> studentList;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public DepartmentPrivateState() {
		courseList= new LinkedList<String>();
		studentList= new LinkedList<String>();
	}

	public List<String> getCourseList() {
		return courseList;
	}

	public List<String> getStudentList() {
		return studentList;
	}

	public void addCourse(String courseName){
		if(!courseList.contains(courseName))
			courseList.add(courseName);
	}

	public void addStudent(String studentName){
		if(!studentList.contains(studentName))
			studentList.add(studentName);
	}
	
}
