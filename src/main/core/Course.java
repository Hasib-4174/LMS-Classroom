package core;

import java.util.ArrayList;

public class Course {
    // static for studentIds
    private String courseName;
    private String courseCode;
    private String instructor;
    private String schedule;
    
    private ArrayList<Assignment> assignments;
    // private ArrayList<String> studetnIds;
    // private int enrolledStudetnCount;

    public Course(String courseName, String courseCode, String studetnIds, String instructor, String schedule) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.instructor = instructor;
        this.schedule = schedule;
        
        // this.studetnIds.add(studetnIds);
        // this.enrolledStudetnCount++;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    // public ArrayList<String> getStudetnIds() {
    //     return studetnIds;
    // }

    public void showInfo() {
        System.out.println(courseCode + " - " + courseName);
        // System.out.println("Enrolled: " + enrolledStudetnCount);
    }


}
