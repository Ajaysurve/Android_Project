package com.example.wattodo;

public class TaskClass {
    String title;
    String date;
    String time;
    String checkbox;
    int id;

    public String getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskClass() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TaskClass(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
        id = this.hashCode();
        this.checkbox = "false";
    }

    public TaskClass(TaskClass item) {
        this.id = item.id;
        this.date = item.date;
        this.time = item.time;
        this.title = item.title;
        this.checkbox = item.checkbox;
    }
}
