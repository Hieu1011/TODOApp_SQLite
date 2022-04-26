package com.google.todoappsqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TaskDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "TaskDbHelper";
    private static final String DATABASE_NAME = "mytask.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TASK = "task";

    public TaskDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Create table");
        String queryCreateTable = "CREATE TABLE " + TABLE_TASK + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title VARCHAR (255) NOT NULL, " +
                "dateStart VARCHAR (255) NOT NULL, " +
                "dateEnd VARCHAR (255) NOT NULL, " +
                "description VARCHAR (255) " +
                ")";

        sqLiteDatabase.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(sqLiteDatabase);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id, title, dateStart, dateEnd, description from task", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long taskID = cursor.getLong(0);
            String taskTitle = cursor.getString(1);
            String taskDateStart = cursor.getString(2);
            String taskDateEnd = cursor.getString(3);
            String taskDescription = cursor.getString(4);

            tasks.add(new Task(taskID, taskTitle, taskDateStart, taskDateEnd, taskDescription));
            cursor.moveToNext();
        }
        cursor.close();
        return tasks;
    }

    public Task getTaskByID(long ID) {
        Task task = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id, title, dateStart, dateEnd, description from task where id = ?",
                new String[]{ID + ""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long taskID = cursor.getLong(0);
            String taskTitle = cursor.getString(1);
            String taskDateStart = cursor.getString(2);
            String taskDateEnd = cursor.getString(3);
            String taskDescription = cursor.getString(4);

            task = new Task(taskID, taskTitle, taskDateStart, taskDateEnd, taskDescription);
        }
        cursor.close();
        return task;
    }

    void insertTask(@NonNull Task task) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO task (title, dateStart, dateEnd, description ) VALUES (?,?,?,?)",
                new String[]{task.getTitle(), task.getStart() , task.getEnd(), task.getDescription()+ ""});
    }

    void updateTask(@NonNull Task task) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE task SET title=?, dateStart = ?, dateEnd = ?, description = ? where id = ?",
                new String[]{task.getTitle(), task.getStart() , task.getEnd(), task.getDescription() + "", task.getId() + ""});
    }


    void deleteTaskByID(long taskID) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM task where id = ?", new String[]{String.valueOf(taskID)});
    }
}
