package data;

import core.Assignment;
import core.Course;
import core.Notice;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager instance;

    private static final String DATA_DIR = "src/main/resources/data/";
    private static final String COURSES_FILE = DATA_DIR + "courses.csv";
    private static final String ASSIGNMENTS_FILE = DATA_DIR + "assignments.csv";
    private static final String NOTICES_FILE = DATA_DIR + "notices.csv";
    private static final String ENROLLMENTS_FILE = DATA_DIR + "enrollments.csv";

    private List<Course> courses;
    private List<Assignment> assignments;
    private List<Notice> notices;

    private DataManager() {
        courses = new ArrayList<>();
        assignments = new ArrayList<>();
        notices = new ArrayList<>();

        ensureDataDir();
        loadCourses();
        loadAssignments();
        loadNotices();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // --- Courses ---
    private void loadCourses() {
        courses.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: Code,Name,Instructor,Schedule
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    courses.add(new Course(parts[1], parts[0], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("No courses file found, starting empty.");
        }
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course c) {
        courses.add(c);
        saveCourses();
    }

    public void deleteCourse(int index) {
        if (index >= 0 && index < courses.size()) {
            courses.remove(index);
            saveCourses();
        }
    }

    private void saveCourses() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COURSES_FILE))) {
            for (Course c : courses) {
                bw.write(c.getCourseCode() + "," + c.getCourseName() + "," + c.getInstructor() + "," + c.getSchedule());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Assignments (Global) ---
    private void loadAssignments() {
        assignments.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(ASSIGNMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: Name,Details
                String[] parts = line.split(",", 2); // Split only on first comma
                if (parts.length >= 2) {
                    assignments.add(new Assignment(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("No assignments file found.");
        }
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignment(Assignment a) {
        assignments.add(a);
        saveAssignments();
    }

    public void deleteAssignment(int index) {
        if (index >= 0 && index < assignments.size()) {
            assignments.remove(index);
            saveAssignments();
        }
    }

    private void saveAssignments() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ASSIGNMENTS_FILE))) {
            for (Assignment a : assignments) {
                // Basic CSV escaping for details logic omitted for simplicity of university
                // project,
                // assuming simple text without newlines/commas for now.
                bw.write(a.getName() + "," + a.getDetails());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Notices ---
    private void loadNotices() {
        notices.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(NOTICES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: Title,Date,Content
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    notices.add(new Notice(parts[0], parts[2], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("No notices file found.");
        }
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public void addNotice(Notice n) {
        notices.add(n);
        saveNotices();
    }

    public void deleteNotice(int index) {
        if (index >= 0 && index < notices.size()) {
            notices.remove(index);
            saveNotices();
        }
    }

    private void saveNotices() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NOTICES_FILE))) {
            for (Notice n : notices) {
                bw.write(n.getTitle() + "," + n.getDate() + "," + n.getContent());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Enrollments ---
    // UserID -> List of CourseCodes
    public List<String> getEnrolledCourseCodes(String studentId) {
        List<String> codes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ENROLLMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(studentId)) {
                    codes.add(parts[1]);
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return codes;
    }

    public void enrollStudent(String studentId, String courseCode) {
        // Append to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ENROLLMENTS_FILE, true))) {
            File f = new File(ENROLLMENTS_FILE);
            if (f.length() > 0)
                bw.newLine();
            bw.write(studentId + "," + courseCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
