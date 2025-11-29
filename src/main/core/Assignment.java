package core;

public class Assignment {
    private String name;
    private String details;

    public Assignment(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void showInfo() {
        System.out.println("Name: " + name);
        System.out.println("Details: " + details);
    }
}
