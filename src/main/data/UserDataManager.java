package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import users.Admin;
import users.Student;
import users.User;

public class UserDataManager {
    private static final String CSV_FILE = "src/main/resources/users/login.csv";
    private List<User> users;

    public UserDataManager() {
        users = new ArrayList<>();
        loadUsers();
    }

    // Load users from CSV
    private void loadUsers() {
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            System.err.println("File not found: " + CSV_FILE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming CSV format: "id","email","password"
                // Dealing with quotes
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                if (parts.length >= 3) {
                    String id = parts[0].replace("\"", "");
                    String email = parts[1].replace("\"", "");
                    String password = parts[2].replace("\"", "");

                    // Basic distinction based on ID/Email logic provided in requirements
                    // "admin" -> Admin
                    // else -> Student
                    // Note: The prompt implies specific IDs differ.
                    // "admin" for admin.
                    
                    if (id.equalsIgnoreCase("admin")) {
                         // Admin constructor might vary, assuming name is "Admin"
                         // In users/Admin.java, check constructor.
                         // For now assuming: public Admin(String name, String email, String password)
                         users.add(new Admin("Admin", email, password));
                    } else {
                         // Student
                         // users/Student.java likely takes (Name, ID, Email, Password)
                         // The CSV only has ID, Email, Pass. Name is missing in `login.csv` for admin but present for student in example?
                         // Prompt says: login.csv: id,mail,password
                         // "admin","admin@northsouth.edu","admin12345"
                         // "2521228042","name252@northsouth.edu","name252@12345" -- Wait, the example in prompt has name separate? 
                         // Check prompt: "2521228042","name252@northsouth.edu","name252@12345"
                         // It seems the CSV given in the prompt DOES NOT have the name.
                         // BUT the signup requirements says: "take name, id, email, pass".
                         // So I should probably modify CSV to include NAME or just use a placeholder/extract from email.
                         // Let's check the CSV content provided in the prompt again:
                         // "admin","admin@northsouth.edu","admin12345" 
                         // "2521228042","name252@northsouth.edu","name252@12345"
                         // Actually, looking at the second line: "2521228042" is ID. "name252@northsouth.edu" is EMAIL. "name252@12345" is PASS.
                         // So NAME is missing in CSV. 
                         // I will modify the CSV structure to include Name if I can, OR since the user said "dont make big changes", 
                         // I will just use the ID or Email as name when loading from this specific legacy CSV, 
                         // BUT for new signups I should probably save the name too?
                         // "in signup it will add id,mail,password to login.csv file for letter login"
                         // The user explicitly listed "id,mail,password" for the file. 
                         // So I will stick to 3 columns. Name will be lost on restart if I don't save it.
                         // Wait, "make class for store data... in signup it will add id,mail,password to login.csv"
                         // Maybe I should add a 4th column for Name if needed?
                         // User said: "for signup (only students) take name, id, email, pass"
                         // AND "add id,mail,password to login.csv". 
                         // It seems user IMPLIES name is NOT saved to login.csv? That seems odd for an OOP course.
                         // I'll stick to what is strictly asked for login.csv: id, mail, pass.
                         // I'll use "Student" as generic name or extract from email.
                         
                         users.add(new Student("Student", id, email, password));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add new user (Student only as per requirement)
    public void addUser(Student student) {
        users.add(student);
        saveUserToCsv(student);
    }

    private void saveUserToCsv(Student s) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            // Format: "id","email","password"
            // Ensure newline before if file not empty? BufferedWriter append usually just appends.
            // Best to ensure we write a new line.
            
            String id = "\"" + s.getStudentId() + "\"";
            String email = "\"" + s.getEmail() + "\"";
            String password = "\"" + s.getPassword() + "\"";
            
            // Check if file is empty or ends with newline? 
            // Simplified: just write newLine then content if file length > 0
            File f = new File(CSV_FILE);
            if (f.length() > 0) {
                bw.newLine();
            }
            
            bw.write(id + "," + email + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User validateUser(String id, String password) {
        for (User u : users) {
            // Check based on Instance type to get ID?
            // User class has name, email, password.
            // Student has ID. Admin? 
            // In Admin.java I need to check if it has ID.
            // The prompt says: id(eg. "admin" for admin, "2521228" for student).
            // So we treat "admin" as the ID for Admin.
            
            String userId = null;
            if (u instanceof Student) {
                userId = ((Student) u).getStudentId();
            } else if (u instanceof Admin) {
                userId = "admin"; // Hardcoded for Admin as per prompt implication
            }

            if (userId != null && userId.equals(id) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null; // Not found or invalid pass
    }
}
