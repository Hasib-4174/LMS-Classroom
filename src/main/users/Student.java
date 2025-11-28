package users;

public class Student extends User{
    private String studentId;

    public Student(String name, String studentId, String email, String password) {
        super(name, email, password);
    }
    
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public void showInfo() {
        System.out.println("Name: " + getName());
        System.out.println("Student ID: " + this.studentId);
        System.out.println("Email: " + getEmail());
    }

}
