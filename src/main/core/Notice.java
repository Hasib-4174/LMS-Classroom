package core;

import java.time.LocalDate;

public class Notice {
    private String title;
    private String content;
    private String date;

    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = LocalDate.now().toString();
    }

    // Constructor for loading from CSV with date
    public Notice(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void showInfo() {
        System.out.println("Notice: " + title + " [" + date + "]");
        System.out.println(content);
    }
}
