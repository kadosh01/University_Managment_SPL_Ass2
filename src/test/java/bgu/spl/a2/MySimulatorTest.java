/**
 * @author Aviv Metz
 * @author Nadav Brama
 * @author Hod Alpert
 * Credits goes to nadav and hod since i'm heavily using the models their wrote.
 */

package bgu.spl.a2;

import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.*;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class MySimulatorTest {
//    @Rule
//    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Parameterized.Parameters
    public static List<Object[]> data() {
        //Change number of runs by changing the variable currently set to 4000.
        return Arrays.asList(new Object[4000][0]);
    }

    @Test
    public void main() throws IOException {
        Simulator.main(new String[]{"SimulatorTest.json"});
        try (InputStream fin = new FileInputStream("result.ser");
             ObjectInputStream ois = new ObjectInputStream(fin)) {
            HashMap<String, PrivateState> data = (HashMap<String, PrivateState>) ois.readObject();
            data.forEach((String actor, PrivateState state) -> {
//                System.out.println(actor + ": ");
//                System.out.print("History: ");
//                state.getLogger().forEach((String s) -> {
//                    System.out.print(s + ", ");
//                });
//                System.out.println("");
                if (state instanceof StudentPrivateState) {
                    StudentPrivateState casted_student_state = (StudentPrivateState)state;
                    switch (actor) {
                        case "Sign to pref 4": {
                            testSigntoPref4(casted_student_state, actor);
                            break;
                        }
                        case "Sign to pref 5": {
                            testSignToPref5(casted_student_state, actor);
                            break;
                        }
                        case "1Sign to pref 3 or 4": {
                            testSigntoCourse3or4(casted_student_state, actor);
                            break;
                        }
                        case "2Sign to pref 3 or 4": {
                            testSigntoCourse3or4(casted_student_state, actor);
                            break;
                        }
                        case "Sign to closed course with spaces": {
                            testSignToCourseWithAddedSpace(casted_student_state, actor);
                            break;
                        }
                        case "not registered to any course1": {
                            testNobodyRegistered1(casted_student_state, actor);
                            break;
                        }
                        case "not registered to any course2": {
                            testNobodyRegistered2(casted_student_state, actor);
                            break;
                        }
                        case "not registered to any course3": {
                            testNobodyRegistered3(casted_student_state, actor);
                            break;
                        }
                        case "SuccessSignFromA": {
                            testSuccessSigFromA(casted_student_state, actor);
                            break;
                        }
                        case "SuccessSignFromB": {
                            testSuccessSigFromB(casted_student_state, actor);
                            break;
                        }
                        case "FailSignFromA": {
                            testFailSigFromA(casted_student_state, actor);
                            break;
                        }
                        case "Cant sign to any of pref": {
                            testCantSignToAnyOfPref(casted_student_state, actor);
                            break;
                        }
                        case "Should not be registered": {
                            testShouldntBeRegistered(casted_student_state, actor);
                            break;
                        }
                        case "Should be registered": {
                            testShouldbeRegistered(casted_student_state, actor);
                            break;
                        }

                    }
                } else if (state instanceof CoursePrivateState){
                    CoursePrivateState casted_state = (CoursePrivateState)state;
                    switch (actor) {
                        case "pref1": {
                            testCoursepref1(casted_state, actor);
                            break;
                        }
                        case "pref2": {
                            testCoursepref2(casted_state, actor);
                            break;
                        }
                        case "pref3": {
                            testCoursepref3(casted_state, actor);
                            break;
                        }
                        case "pref4": {
                            testCoursepref4(casted_state, actor);
                            break;
                        }
                        case "pref5": {
                            testCoursepref5(casted_state, actor);
                            break;
                        }
                        case "closed course with added spaces": {
                            testClosedCourseWithOpenSpaces(casted_state, actor);
                            break;
                        }
                        case "open to close": {
                            testOpenToClose(casted_state, actor);
                            break;
                        }
                        case "room for 2": {
                            testPlaceForTwo(casted_state, actor);
                            break;
                        }
                        case "SPL": {
                            testSPLregisterAndUnregister(casted_state, actor);
                            break;
                        }
                    }
                }
//                System.out.println("----------------");
            });

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
//        System.out.println("-----------------Test End------------------");
    }

//    private void printCourse(CoursePrivateState state) {
//        System.out.print("prequisites: ");
//        state.getPrequisites().forEach((String s) -> {
//            System.out.print(s + ", ");
//        });
//        System.out.print('\n' + "students: ");
//        state.getRegStudents().forEach((String s) -> {
//            System.out.print(s + ", ");
//        });
//        System.out.print('\n' + "Registered: ");
//        System.out.println(state.getRegistered());
//        System.out.print("available spaces: ");
//        System.out.println(state.getAvailableSpots());
//    }



//    private void printStudent(StudentPrivateState state) {
//        System.out.print("Grades: ");
//        state.getGrades().forEach((String s, Integer grade) -> {
//            System.out.print(s + ": " + grade + ", ");
//        });
//        System.out.print('\n' + "Signature: ");
//        System.out.println(state.getSignature());
//    }

//    private void printDepartment(DepartmentPrivateState state) {
//        System.out.print("Courses: ");
//        state.getCourseList().forEach((String s) -> {
//            System.out.print(s + ", ");
//        });
//        System.out.print('\n' + "Students: ");
//        state.getStudentList().forEach((String s) -> {
//            System.out.print(s + ", ");
//        });
//        System.out.println("");
//    }

    public void testCoursepref1(CoursePrivateState course_state, String course_name){
        assertTrue("Nobody should be registered to pref1", course_state.getAvailableSpots() == 40);
        assertTrue("Nobody should be registered to pref1", course_state.getRegistered() == 0);
        assertTrue("Nobody should be registered to pref1", course_state.getRegStudents().size() == 0);
    }

    public void testCoursepref2(CoursePrivateState course_state, String course_name){
        assertTrue("Nobody should be registered to pref2", course_state.getAvailableSpots() == 0);
        assertTrue("Nobody should be registered to pref2", course_state.getRegistered() == 0);
        assertTrue("Nobody should be registered to pref2", course_state.getRegStudents().size() == 0);
    }

    public void testCoursepref3(CoursePrivateState course_state, String course_name){
        assertTrue("One student should be registered to pref3", course_state.getAvailableSpots() == 0);
        assertTrue("Exactly one student should be registered to pref3, testing the inner variable", course_state.getRegistered() == 1);
        assertTrue("Exactly one student should be registered to pref3, testing the inner student list.", course_state.getRegStudents().size() == 1);
        assertTrue("One of these two students should be registered here (Jump to this line)",
                course_state.getRegStudents().contains("1Sign to pref 3 or 4") ||
                        course_state.getRegStudents().contains("2Sign to pref 3 or 4"));
    }

    public void testCoursepref4(CoursePrivateState course_state, String course_name){
        assertTrue("38 Places need to be remaining after signing 2 students in pref 4", course_state.getAvailableSpots() == 38);
        assertTrue("2 Students should be registered, testing the variable inside in pref 4", course_state.getRegistered() == 2);
        assertTrue("2 Students should be registered, testing the list's size in pref 4", course_state.getRegStudents().size() == 2);
        assertTrue("One of these two students should be registered here (Jump to this line)",
                (course_state.getRegStudents().contains("1Sign to pref 3 or 4") ||
                        course_state.getRegStudents().contains("2Sign to pref 3 or 4")) &&
                        course_state.getRegStudents().contains("Sign to pref 4"));
    }

    public void testCoursepref5(CoursePrivateState course_state, String course_name){
        assertTrue("One student in pref 5", course_state.getAvailableSpots() == 399);
        assertTrue("One student in pref 5, testing variable", course_state.getRegistered() == 1);
        assertTrue("One student in pref 5, testing inner list.", course_state.getRegStudents().size() == 1);
        assertTrue("Specific student should be registered to pref 5", course_state.getRegStudents().contains("Sign to pref 5"));
    }

    //Might not happen, verify.
    public void testClosedCourseWithOpenSpaces(CoursePrivateState course_state, String course_name){
        assertTrue("Opening new places in a closed course", course_state.getAvailableSpots() == -1);
        assertTrue("Signing a student to a closed course since there are places",
                !course_state.getRegStudents().contains("Sign to closed course with spaces"));
        assertTrue("Inner student list isn't zero in a closed course.", course_state.getRegStudents().size() == 0);
    }

    public void testSPLregisterAndUnregister(CoursePrivateState course_state, String course_name){
        assertTrue("(Register -> Unregister)*5 should end with student unregisted",
                !course_state.getRegStudents().contains("Should not be registered"));
        assertTrue("(Unregister -> Register)*5 should end with student registed",
                course_state.getRegStudents().contains("Should be registered"));
    }

    public void testOpenToClose(CoursePrivateState course_state, String course_name){
        assertTrue("Nobody should be registered to a course after closed, registered num", course_state.getRegistered() == 0);
        assertTrue("Nobody should be registered to a course after closed, registered list", course_state.getRegStudents().size() == 0);
        assertTrue("Number of places in a closed course should be -1", course_state.getAvailableSpots() == -1);
    }

    public void testPlaceForTwo(CoursePrivateState course_state, String course_name){
        assertTrue("Two students should be registered to this course", course_state.getRegistered() == 2);
        assertTrue("Place should be available", course_state.getAvailableSpots() == 0);
        assertTrue("Only two out of three should be registered",
                (course_state.getRegStudents().contains("competition1") &&
                        course_state.getRegStudents().contains("competition2")) ||
                        (course_state.getRegStudents().contains("competition1") &&
                                course_state.getRegStudents().contains("competition3")) ||
                        (course_state.getRegStudents().contains("competition3") &&
                                course_state.getRegStudents().contains("competition2")));
    }

    public void testNobodyRegistered1(StudentPrivateState student_state, String student_name){
        assertTrue("Shouldn't be registered to anything.",student_state.getGrades().size() == 0);
    }

    public void testNobodyRegistered2(StudentPrivateState student_state, String student_name){
        assertTrue("Shouldn't be registered to anything, 2nd student",student_state.getGrades().size() == 0);
    }

    public void testNobodyRegistered3(StudentPrivateState student_state, String student_name){
        assertTrue("Shouldn't be registered to anything, 3rd student",student_state.getGrades().size() == 0);
    }

    public void testSuccessSigFromA(StudentPrivateState student_state, String student_name){
        assertTrue("SuccessSignFromA student should have success sig from computer A",
                student_state.getSignature() == 111111111);
    }

    public void testSuccessSigFromB(StudentPrivateState student_state, String student_name){
        assertTrue("SuccessSignFromB should have success sig from computer B",
                student_state.getSignature() == 222222222);
    }

    public void testFailSigFromA(StudentPrivateState student_state, String student_name){
        assertTrue("FailSignFromA student should have success sig from computer A",
                student_state.getSignature() == 999999999);
    }

    public void testSigntoPref4(StudentPrivateState student_state, String student_name){
        assertTrue("Should be registered to pref 4",
                student_state.getGrades().containsKey("pref4"));
        assertTrue("Should be registered to pref 4 with grade 50",
                student_state.getGrades().get("pref4") == 50);
    }

    public void testSignToPref5(StudentPrivateState student_state, String student_name){
        assertTrue("Should be registered to pref 5",
                student_state.getGrades().containsKey("pref5"));
        assertTrue("Should be registered to pref 5 with grade -1 or null", //If you failed this test, it's fine. Hussein said
                student_state.getGrades().get("pref5") == -1 ||
                        student_state.getGrades().get("pref5") == null);		//He would check it.
    }

    public void testSigntoCourse3or4(StudentPrivateState student_state, String student_name){
        assertTrue("Should be registered to either course pref 4 or pref 3",
                student_state.getGrades().containsKey("pref4") || student_state.getGrades().containsKey("pref3"));
        if (student_state.getGrades().containsKey("pref4")){
            assertTrue("Should be registered to pref 4 with grade 50",
                    student_state.getGrades().get("pref4") == 50);
        }
        else {
            assertTrue("Should be registered to pref 3 with grade -1 or null",
                    student_state.getGrades().get("pref3") == -1 ||
                            student_state.getGrades().get("pref3") == null);
        }

    }

    public void testCantSignToAnyOfPref(StudentPrivateState student_state, String student_name){
        assertTrue("Shouldn't be registed to anything",
                student_state.getGrades().size() == 0);
    }

    public void testSignToCourseWithAddedSpace(StudentPrivateState student_state, String student_name){
        assertTrue("Shouldn't be registered to a closed course after places were added to it",
                student_state.getGrades().size() == 0);
    }

    public void testShouldbeRegistered(StudentPrivateState student_state, String student_name){
        assertTrue("Should be registered after a cycle or unregister->register",
                student_state.getGrades().size() == 1);
        assertTrue("Should be registered after a cycle or unregister->register, checking if key exists",
                student_state.getGrades().containsKey("SPL"));
        assertTrue("Should be registered after a cycle or unregister->register, checking if grade is correct",
                student_state.getGrades().get("SPL") == 99);
    }

    public void testShouldntBeRegistered(StudentPrivateState student_state, String student_name){
        assertTrue("Shouldnt be registered after a cycle or register->unregister",
                student_state.getGrades().size() == 0);
        assertTrue("Shouldnt be registered after a cycle or register->unregister",
                !student_state.getGrades().containsKey("SPL"));
        assertTrue("Shouldnt be registered after a cycle or register->unregister",
                student_state.getGrades().get("SPL") == null);
    }

}