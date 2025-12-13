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
                    String name = "Student"; // Default
                    if (parts.length >= 4) {
                        name = parts[3].replace("\"", "");
                    }

                    if (id.equalsIgnoreCase("admin")) {
                        users.add(new Admin(name, email, password));
                    } else {
                        users.add(new Student(name, id, email, password));
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

            String id = "\"" + s.getStudentId() + "\"";
            String email = "\"" + s.getEmail() + "\"";
            String password = "\"" + s.getPassword() + "\"";
            String name = "\"" + s.getName() + "\"";

            File f = new File(CSV_FILE);
            if (f.length() > 0) {
                bw.newLine();
            }

            bw.write(id + "," + email + "," + password + "," + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User validateUser(String id, String password) {
        for (User u : users) {
            String userId = null;
            if (u instanceof Student) {
                userId = ((Student) u).getStudentId();
            } else if (u instanceof Admin) {
                userId = "admin"; // Hardcoded for Admin
            }

            if (userId != null && userId.equals(id) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null; // Not found or invalid pass
    }
}
