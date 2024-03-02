package com.example.notes.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note {

    private String title;
    private String desc;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int priority;

    public Note(String title, String desc, int priority) {
        this.title = title;
        this.desc = desc;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }
}
