package bgu.spl.a2.Gson;

import java.io.Serializable;
import java.util.List;

public class ActionParsing implements Serializable {

    private String Action;
    private String Department;
    private String Course;
    private String Space;
    private List<String> Prerequisites;
    private String Student;
    private List<String> Grade;
    private String Number;
    private List<String> Preferences;
    private String Computer;
    private List<String> Students;
    private List<String> Conditions;

    public String getAction(){return Action;}

    public String getDepartment(){return Department;}

    public String getCourse(){return Course;}

    public String getSpace(){return Space;}

    public List<String> getPrerequisites(){return Prerequisites;}

    public String getStudent(){return Student;}

    public List<String> getGrade(){return Grade;}

    public String getNumber(){return Number;}

    public List<String> getPreferences(){return Preferences;}

    public String getComputer(){return Computer;}

    public List<String> getStudents(){return Students;}

    public List<String> getConditions(){return Conditions;}

}
