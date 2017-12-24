package bgu.spl.a2;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

import bgu.spl.a2.PrivateState;
import com.google.gson.Gson;

import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class checkResultSer implements Serializable {
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		FileInputStream fr;
		ObjectInputStream ois;
		Gson gson = new Gson();
		HashMap<String, PrivateState> res = new HashMap<>();
		Vector<PrivateState> p =new Vector<>();
		try {
			fr = new FileInputStream("result.ser");
			ois = new ObjectInputStream(fr);
			res=(HashMap<String, PrivateState>) ois.readObject();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("{");
		
		//printing departments
		System.out.println("\"Departments\" : [");
		for (String dep : res.keySet()){
			String s = res.get(dep).getClass().getName();
			if (res.get(dep).getClass().getName() == "bgu.spl.a2.sim.privateStates.DepartmentPrivateState") {
				System.out.println("{");
				System.out.println(" \"Department\" : \"" + dep + "\",");
				System.out.print("\"actions\":[");
				String logger = "";
				for(String log : res.get(dep).getLogger())
					logger += "\"" + log + "\", ";
				logger = logger.substring(0, Math.max(0, logger.length() - 1));
				logger += "],";
				System.out.println(logger);
				
				System.out.print("\"courseList\": [");
				String courseList = "";
				for(String course : ((DepartmentPrivateState)res.get(dep)).getCourseList())
					courseList += "\"" + course + "\",";
				courseList = courseList.substring(0, Math.max(0, courseList.length() - 1));
				courseList += "],";
				System.out.println(courseList);
				
				System.out.print("\"studentList\": [");
				String studentList = "";
				for(String student : ((DepartmentPrivateState)res.get(dep)).getStudentList())
					studentList += "\"" + student + "\",";
				studentList = studentList.substring(0, Math.max(0, studentList.length() - 1));
				studentList += "]";
				System.out.println(studentList);
				System.out.println("},");
			}
		}
		System.out.println("],");
		//printing courses
		System.out.println("\"Courses\" : [");
		for (String course : res.keySet()){
			String s = res.get(course).getClass().getName();
			if (res.get(course).getClass().getName() == "bgu.spl.a2.sim.privateStates.CoursePrivateState") {
				System.out.println("{");
				System.out.println(" \"Course\" : \"" + course + "\",");
				String logger = "\"actions\":[";
				for(String log : res.get(course).getLogger())
					logger += "\"" + log + "\",";
				logger = logger.substring(0, Math.max(0, logger.length() - 1));
				logger += "],";
				System.out.println(logger);
				
				System.out.println("\"availableSpots\": \"" + ((CoursePrivateState)res.get(course)).getAvailableSpots().toString() +"\",");
				System.out.println("\"registered\": \"" + ((CoursePrivateState)res.get(course)).getRegistered().toString() +"\",");
				
				String studentList = "\"regStudents\" : [";
				for(String student : ((CoursePrivateState)res.get(course)).getRegStudents())
					studentList += "\"" + student + "\",";
				studentList = studentList.substring(0, Math.max(0, studentList.length() - 1));
				studentList += "],";
				System.out.println(studentList);
				
				String preqsList = "\"prequisites\" : [ ";
				for(String preq : ((CoursePrivateState)res.get(course)).getPrequisites())
					preqsList += "\"" + preq + "\", ";
				preqsList = preqsList.substring(0, Math.max(0, preqsList.length() - 1));
				preqsList += "]";
				System.out.println(preqsList);
				System.out.println("},");
			}
		}
		System.out.println("],");
		//printing students
		System.out.println("\"Students\": [");
		for (String student : res.keySet()){
			String s = res.get(student).getClass().getName();
			if (res.get(student).getClass().getName() == "bgu.spl.a2.sim.privateStates.StudentPrivateState") {
				System.out.println("{");
				System.out.println(" \"Student\": \"" + student + "\",");
				String logger = "\"actions\" : [ ";
				for(String log : res.get(student).getLogger())
					logger += "\"" + log + "\",";
				logger = logger.substring(0, Math.max(0, logger.length() - 1));
				logger += "],";
				System.out.println(logger);
								
				String gradesList = "\"grades\" : [ ";
				for(String course : ((StudentPrivateState)res.get(student)).getGrades().keySet())
					gradesList += "\"(" + course + "," + ((StudentPrivateState)res.get(student)).getGrade(course) + ")" +"\", ";
				gradesList = gradesList.substring(0, Math.max(0, gradesList.length() - 1));
				gradesList += "],";
				System.out.println(gradesList);
				
				System.out.println("\"signature\": \"" + ((StudentPrivateState)res.get(student)).getSignature() +"\",");
				System.out.println("},");
			}
		}
		System.out.println("]");
		System.out.println("}");
	}
}
