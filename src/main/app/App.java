package app;

import users.Student;

public class App {
    public static void main(String[] args) {
        Student s = new Student("Shamim", "2521228042", "shamim252@northsouth.edu", "shamim252");
        s.enrollCourse("java-OOP", "CSE215", "SAM3", "RA 8:00-9:30");
        s.enrollCourse("Python-OOP", "CSE216", "Nova", "MW 9:40-11:10");
        s.showInfo();
    }
}
