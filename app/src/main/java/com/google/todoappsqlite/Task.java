package com.google.todoappsqlite;

import android.os.Parcel;
import android.os.Parcelable;

public class Task  {
    private long Id;
    private String Title, Start, End, Description;

    public Task(long id, String title, String start, String end, String description) {
        Id = id;
        Title = title;
        Start = start;
        End = end;
        Description = description;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setStart(String start) {
        Start = start;
    }

    public void setEnd(String end) {
        End = end;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public long getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getStart() {
        return Start;
    }

    public String getEnd() {
        return End;
    }

    public String getDescription() {
        return Description;
    }

}
