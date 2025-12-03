package users;
import core.*;
import helpers.*;
import java.util.ArrayList;

public class Student extends User{
    private String studentId;
    private ArrayList<Course> enrolledCourses;
    private ArrayList<String> enrolledCoursesCode;

    public Student(String name, String studentId, String email, String password) {
        super(name, email, password);
        this.studentId = studentId;
        this.enrolledCourses = new ArrayList<>();
        this.enrolledCoursesCode = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(ArrayList<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public ArrayList<String> getEnrolledCoursesCode() {
        return enrolledCoursesCode;
    }

    public void enrollCourse(String courseName, String courseCode, String instructor, String schedule) {
        Course c = new Course(courseName, courseCode, instructor, schedule);
        if (!enrolledCoursesCode.contains(courseCode)) {
            enrolledCourses.add(c);
        }
    }

    public void removeCourse(String courseCode) {
        enrolledCourses.removeIf(course -> course.getCourseCode().equals(courseCode));
    }

    @Override
    public void showInfo() {
        Sout.println("========== Student Info ==========");
        Sout.println("Name: " + getName());
        Sout.println("Student ID: " + this.studentId);
        Sout.println("Email: " + getEmail());
        Sout.println("---------- Courses ----------\n");
        for(int i=0;i<enrolledCourses.size();i++) {
            Sout.print("[" + (i+1)+ "] : ");
            enrolledCourses.get(i).showInfo();
            Sout.println();
        }
        Sout.println("-----------------------------");
    }

}
