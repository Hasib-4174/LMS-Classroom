package auth;

import java.util.Scanner;
import data.UserDataManager;
import users.Student;
import users.User;
import users.Admin;

public class LoginSystem {
    private UserDataManager dataManager;
    private Scanner scanner;

    public LoginSystem() {
        this.dataManager = new UserDataManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== LMS Login System ===");
            System.out.println("1. Login");
            System.out.println("2. Signup (Student Only)");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    signup();
                    break;
                case "3":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = dataManager.validateUser(id, password);

        if (user != null) {
            System.out.println("Login Successful!");
            System.out.println("Welcome, " + user.getName());
            user.showInfo();
        } else {
            System.out.println("Invalid ID or Password.");
        }
    }

    private void signup() {
        System.out.println("\n--- Signup (Student) ---");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // Create new Student
        // Assuming Student constructor is: Name, ID, Email, Password
        Student newStudent = new Student(name, id, email, password);
        
        dataManager.addUser(newStudent);
        System.out.println("Signup Successful! You can now login.");
    }
}
