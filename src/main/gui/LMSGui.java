package gui;

import core.Course;
import core.Assignment;
import core.Notice;
import data.UserDataManager;
import data.DataManager;
import users.Student;
import users.Admin;
import users.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LMSGui extends UI {
    private UserDataManager userManager;
    private DataManager dataManager;
    private User currentUser;

    // UI Components for Login
    private JTextField loginIdField;
    private JPasswordField loginPassField;

    // UI Components for Signup
    private JTextField signupNameField;
    private JTextField signupIdField;
    private JTextField signupEmailField;
    private JPasswordField signupPassField;

    // Dashboard references
    private JLabel profileNameLabel;
    private JLabel profileIdLabel;
    private JLabel profileEmailLabel;

    // Card references
    private JPanel coursesCard;
    private JPanel assignmentsCard;
    private JPanel noticesCard;

    // Table references for selection
    private JTable coursesTable;
    private JTable assignmentsTable;
    private JTable noticesTable;

    public LMSGui() {
        super("LMS Classroom", 900, 600);
        userManager = new UserDataManager();
        dataManager = DataManager.getInstance();

        // simple clean theme
        setTheme(new Color(245, 245, 245), Color.BLACK);

        setupLoginPage();
        setupSignupPage();
        setupDashboard();

        showPage("Login");

        setVisible(true);
    }

    private void setupLoginPage() {
        JPanel p = createPage("Login");

        // Center box simulation
        int centerX = 250;
        int startY = 150;

        label(p, "LMS Login", centerX, startY - 50, 300, 30);

        label(p, "User ID:", centerX, startY, 100, 30);
        loginIdField = textField(p, centerX, startY + 30, 300, 30);

        label(p, "Password:", centerX, startY + 70, 100, 30);
        loginPassField = passwordField(p, centerX, startY + 100, 300, 30);

        button(p, "Login", centerX, startY + 150, 140, 35, this::handleLogin);
        button(p, "Signup (Student)", centerX + 160, startY + 150, 140, 35, () -> showPage("Signup"));
    }

    private void setupSignupPage() {
        JPanel p = createPage("Signup");

        int centerX = 250;
        int startY = 100;

        label(p, "Student Signup", centerX, startY - 50, 300, 30);

        label(p, "Full Name:", centerX, startY, 100, 30);
        signupNameField = textField(p, centerX, startY + 30, 300, 30);

        label(p, "Student ID:", centerX, startY + 70, 100, 30);
        signupIdField = textField(p, centerX, startY + 100, 300, 30);

        label(p, "Email:", centerX, startY + 140, 100, 30);
        signupEmailField = textField(p, centerX, startY + 170, 300, 30);

        label(p, "Password:", centerX, startY + 210, 100, 30);
        signupPassField = passwordField(p, centerX, startY + 240, 300, 30);

        button(p, "Register", centerX, startY + 290, 140, 35, this::handleSignup);
        button(p, "Back to Login", centerX + 160, startY + 290, 140, 35, () -> showPage("Login"));
    }

    private void setupDashboard() {
        createSplitPage("Dashboard", 200);

        // --- Left Menu ---
        addLeftMenuButtonLinked("Dashboard", "Profile", 10, 20, 180, 40, "ProfileCard");
        addLeftMenuButtonLinked("Dashboard", "Courses", 10, 70, 180, 40, "CoursesCard");
        addLeftMenuButtonLinked("Dashboard", "Assignments", 10, 120, 180, 40, "AssignmentsCard");
        addLeftMenuButtonLinked("Dashboard", "Notices", 10, 170, 180, 40, "NoticesCard");

        addLeftMenuButton("Dashboard", "Logout", 10, 500, 180, 40, () -> {
            currentUser = null;
            loginIdField.setText("");
            loginPassField.setText("");
            showPage("Login");
        });

        // --- Right: Profile Card ---
        JPanel profileCard = createRightCard("Dashboard", "ProfileCard");
        label(profileCard, "User Profile", 30, 30, 200, 30).setFont(new Font("SansSerif", Font.BOLD, 20));

        label(profileCard, "Name:", 50, 80, 100, 30);
        profileNameLabel = label(profileCard, "---", 150, 80, 300, 30);

        label(profileCard, "ID:", 50, 120, 100, 30);
        profileIdLabel = label(profileCard, "---", 150, 120, 300, 30);

        label(profileCard, "Email:", 50, 160, 100, 30);
        profileEmailLabel = label(profileCard, "---", 150, 160, 300, 30);

        // --- Right: Courses Card ---
        coursesCard = createRightCard("Dashboard", "CoursesCard", new BorderLayout());

        // --- Right: Assignments Card ---
        assignmentsCard = createRightCard("Dashboard", "AssignmentsCard", new BorderLayout());

        // --- Right: Notices Card ---
        noticesCard = createRightCard("Dashboard", "NoticesCard", new BorderLayout());
    }

    private void handleLogin() {
        String id = loginIdField.getText().trim();
        String pass = new String(loginPassField.getPassword()).trim();

        if (id.isEmpty() || pass.isEmpty()) {
            popup("Please enter ID and Password");
            return;
        }

        User user = userManager.validateUser(id, pass);
        if (user != null) {
            currentUser = user;
            updateDashboard(user);
            showPage("Dashboard");
            showRightCard("Dashboard", "ProfileCard");
        } else {
            popup("Invalid Credentials!");
        }
    }

    private void handleSignup() {
        String name = signupNameField.getText().trim();
        String id = signupIdField.getText().trim();
        String email = signupEmailField.getText().trim();
        String pass = new String(signupPassField.getPassword()).trim();

        if (name.isEmpty() || id.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            popup("Please fill all fields");
            return;
        }

        Student s = new Student(name, id, email, pass);
        userManager.addUser(s);

        popup("Signup Successful! Please Login.");

        // clear fields
        signupNameField.setText("");
        signupIdField.setText("");
        signupEmailField.setText("");
        signupPassField.setText("");

        showPage("Login");
    }

    private void updateDashboard(User user) {
        // Update Profile Info
        profileNameLabel.setText(user.getName());

        if (user instanceof Student) {
            profileIdLabel.setText(((Student) user).getStudentId());
        } else {
            profileIdLabel.setText("Admin");
        }

        profileEmailLabel.setText(user.getEmail());

        // --- Update Courses ---
        coursesCard.removeAll();

        JPanel coursesTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        applyTheme(coursesTop);
        label(coursesTop, "Enrolled Courses", 0, 0, 200, 30).setFont(new Font("SansSerif", Font.BOLD, 18));

        if (user instanceof Student) {
            button(coursesTop, "Enroll New Course", 0, 0, 150, 30, this::showEnrollPopup);
        } else {
            // Admin
            button(coursesTop, "Add Course", 0, 0, 150, 30, this::showAddCoursePopup);
            button(coursesTop, "Delete Selected", 0, 0, 150, 30, () -> {
                int row = coursesTable.getSelectedRow();
                if (row != -1) {
                    dataManager.deleteCourse(row);
                    updateDashboard(user);
                    popup("Course Deleted");
                } else {
                    popup("Please select a course to delete");
                }
            });
        }
        coursesCard.add(coursesTop, BorderLayout.NORTH);

        if (user instanceof Student) {
            Student s = (Student) user;
            ArrayList<Course> courses = s.getEnrolledCourses();
            String[] cols = { "Code", "Course Name", "Instructor", "Schedule" };
            Object[][] data = new Object[courses.size()][4];
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                data[i][0] = c.getCourseCode();
                data[i][1] = c.getCourseName();
                data[i][2] = c.getInstructor();
                data[i][3] = c.getSchedule();
            }
            // Student view is read-only regarding deletion here (enrollment is managed
            // elsewhere)
            // But we can still use member variable or just tableFull.
            // For consistency let's use member variable but student can't delete anyway.
            coursesTable = new JTable(new DefaultTableModel(data, cols) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            });
            JScrollPane sp = new JScrollPane(coursesTable);
            applyTheme(sp);
            applyTheme(coursesTable);
            coursesCard.add(sp, BorderLayout.CENTER);

        } else {
            // Admin View All Courses
            List<Course> courses = dataManager.getCourses();
            String[] cols = { "Code", "Course Name", "Instructor", "Schedule" };
            Object[][] data = new Object[courses.size()][4];
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                data[i][0] = c.getCourseCode();
                data[i][1] = c.getCourseName();
                data[i][2] = c.getInstructor();
                data[i][3] = c.getSchedule();
            }

            coursesTable = new JTable(new DefaultTableModel(data, cols) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            });
            JScrollPane sp = new JScrollPane(coursesTable);
            applyTheme(sp);
            applyTheme(coursesTable);
            coursesCard.add(sp, BorderLayout.CENTER);
        }
        coursesCard.revalidate();
        coursesCard.repaint();

        // --- Update Assignments ---
        updateAssignmentsCard(user);

        // --- Update Notices ---
        updateNoticesCard(user);
    }

    private void updateAssignmentsCard(User user) {
        assignmentsCard.removeAll();
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        applyTheme(top);
        label(top, "Assignments Board", 0, 0, 200, 30).setFont(new Font("SansSerif", Font.BOLD, 18));

        if (user instanceof Admin) {
            button(top, "Add Assignment", 0, 0, 150, 30, this::showAddAssignmentPopup);
            button(top, "Delete Selected", 0, 0, 150, 30, () -> {
                int row = assignmentsTable.getSelectedRow();
                if (row != -1) {
                    dataManager.deleteAssignment(row);
                    updateAssignmentsCard(user);
                    popup("Assignment Deleted");
                } else {
                    popup("Please select an assignment to delete");
                }
            });
        }
        assignmentsCard.add(top, BorderLayout.NORTH);

        List<Assignment> list = dataManager.getAssignments();
        String[] cols = { "Name", "Details" };
        Object[][] data = new Object[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i).getName();
            data[i][1] = list.get(i).getDetails();
        }

        // Custom table creation
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        assignmentsTable = new JTable(model);
        JScrollPane sp = new JScrollPane(assignmentsTable);
        applyTheme(sp);
        applyTheme(assignmentsTable);

        assignmentsCard.add(sp, BorderLayout.CENTER);

        assignmentsCard.revalidate();
        assignmentsCard.repaint();
    }

    private void updateNoticesCard(User user) {
        noticesCard.removeAll();
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        applyTheme(top);
        label(top, "Notice Board", 0, 0, 200, 30).setFont(new Font("SansSerif", Font.BOLD, 18));

        if (user instanceof Admin) {
            button(top, "Add Notice", 0, 0, 150, 30, this::showAddNoticePopup);
            button(top, "Delete Selected", 0, 0, 150, 30, () -> {
                int row = noticesTable.getSelectedRow();
                if (row != -1) {
                    dataManager.deleteNotice(row);
                    updateNoticesCard(user);
                    popup("Notice Deleted");
                } else {
                    popup("Please select a notice to delete");
                }
            });
        }
        noticesCard.add(top, BorderLayout.NORTH);

        List<Notice> list = dataManager.getNotices();
        String[] cols = { "Date", "Title", "Content" };
        Object[][] data = new Object[list.size()][3];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i).getDate();
            data[i][1] = list.get(i).getTitle();
            data[i][2] = list.get(i).getContent();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        noticesTable = new JTable(model);
        JScrollPane sp = new JScrollPane(noticesTable);
        applyTheme(sp);
        applyTheme(noticesTable);

        noticesCard.add(sp, BorderLayout.CENTER);

        noticesCard.revalidate();
        noticesCard.repaint();
    }

    // --- Popups ---

    private void showEnrollPopup() {
        // Show list of available courses to enroll
        JDialog d = new JDialog(this, "Enroll Course", true);
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);
        JPanel p = new JPanel(new BorderLayout());

        List<Course> all = dataManager.getCourses();
        String[] names = new String[all.size()];
        for (int i = 0; i < all.size(); i++)
            names[i] = all.get(i).getCourseCode() + " - " + all.get(i).getCourseName();

        JList<String> list = new JList<>(names);
        p.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton b = new JButton("Enroll");
        b.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx != -1) {
                Course c = all.get(idx);
                if (currentUser instanceof Student) {
                    ((Student) currentUser).enrollCourse(c.getCourseName(), c.getCourseCode(), c.getInstructor(),
                            c.getSchedule());
                    updateDashboard(currentUser); // refresh
                    d.dispose();
                    popup("Enrolled in " + c.getCourseCode());
                }
            }
        });
        p.add(b, BorderLayout.SOUTH);

        d.add(p);
        d.setVisible(true);
    }

    private void showAddCoursePopup() {
        // Simple inputs
        JTextField code = new JTextField();
        JTextField name = new JTextField();
        JTextField inst = new JTextField();
        JTextField time = new JTextField();
        Object[] msg = {
                "Code:", code, "Name:", name, "Instructor:", inst, "Schedule:", time
        };
        int res = JOptionPane.showConfirmDialog(this, msg, "Add Course", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            if (!code.getText().isEmpty()) {
                Course c = new Course(name.getText(), code.getText(), inst.getText(), time.getText());
                dataManager.addCourse(c);
                updateDashboard(currentUser);
            }
        }
    }

    private void showAddAssignmentPopup() {
        JTextField name = new JTextField();
        JTextField det = new JTextField();
        Object[] msg = { "Name:", name, "Details:", det };
        int res = JOptionPane.showConfirmDialog(this, msg, "Add Assignment", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            dataManager.addAssignment(new Assignment(name.getText(), det.getText()));
            updateDashboard(currentUser);
        }
    }

    private void showAddNoticePopup() {
        JTextField title = new JTextField();
        JTextArea content = new JTextArea(5, 20);
        Object[] msg = { "Title:", title, "Content:", new JScrollPane(content) };
        int res = JOptionPane.showConfirmDialog(this, msg, "Add Notice", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            dataManager.addNotice(new Notice(title.getText(), content.getText()));
            updateDashboard(currentUser);
        }
    }
}
