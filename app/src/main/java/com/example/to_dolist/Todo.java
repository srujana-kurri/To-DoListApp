package com.example.to_dolist;

public class Todo {
    private int id;
    private String task;
    private String description;
    private boolean completed;

    public Todo() {
        // Default constructor
    }

    public Todo(int id, String task, String description, boolean completed) {
        this.id = id;
        this.task = task;
        this.description = description;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
