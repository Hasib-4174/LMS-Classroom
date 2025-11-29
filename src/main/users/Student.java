package users;
import helpers.*;
import java.util.ArrayList;

public class Student extends User{
    private String studentId;
    private ArrayList<String> enrolledCourses;
    private int enrolledCoursesCount;

    public Student(String name, String studentId, String email, String password) {
        super(name, email, password);
        this.studentId = studentId;
        this.enrolledCourses = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public ArrayList<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(ArrayList<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void enrollCourse(String courseCode) {
        if (!enrolledCourses.contains(courseCode)) {
            enrolledCourses.add(courseCode);
        }
    }

    public void removeCourse(String courseId) {
        enrolledCourses.remove(courseId);
    }

    @Override
    public void showInfo() {
        Sout.println("Name: " + getName());
        Sout.println("Student ID: " + this.studentId);
        for(String course : enrolledCourses)
            Sout.print(course + " ");
        Sout.println();
        Sout.println("Email: " + getEmail());
    }

}
