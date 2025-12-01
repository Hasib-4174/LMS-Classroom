package users;
import core.*;
import helpers.*;
import java.util.ArrayList;

public class Student extends User{
    private String studentId;
    private ArrayList<Course> enrolledCourses;
    private ArrayList<String> enrolledCoursesCode;
    private int enrolledCoursesCount;

    public Student(String name, String studentId, String email, String password) {
        super(name, email, password);
        this.studentId = studentId;
        this.enrolledCourses = new ArrayList<>();
        this.enrolledCoursesCode = new ArrayList<>();
        this.enrolledCoursesCount = 0;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(ArrayList<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public ArrayList<String> getEnrolledCoursesName() {
        return enrolledCoursesCode;
    }

    public void setEnrolledCoursesName(ArrayList<String> enrolledCoursesName) {
        this.enrolledCoursesCode = enrolledCoursesName;
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
        Sout.println("Name: " + getName());
        Sout.println("Student ID: " + this.studentId);
        for(Course course : enrolledCourses)
            Sout.print(course.getCourseName() + " ");
        Sout.println();
        Sout.println("Email: " + getEmail());
    }

}
