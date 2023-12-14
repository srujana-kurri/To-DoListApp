package com.example.to_dolist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_DESCRIPTION = "description"; // Added missing constant
    private static final String COLUMN_COMPLETED = "completed";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TASK + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_COMPLETED + " INTEGER DEFAULT 0)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add a new task
    public void addTask(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, todo.getTask());
        values.put(COLUMN_DESCRIPTION, todo.getDescription()); // Added for description
        values.put(COLUMN_COMPLETED, todo.isCompleted() ? 1 : 0); // Convert boolean to integer
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Method to get all tasks
    @SuppressLint("Range")
    public List<Todo> getAllTasks() {
        List<Todo> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                todo.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASK)));
                todo.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))); // Added for description
                todo.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1); // Convert integer to boolean
                taskList.add(todo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }

    // Method to update a task
    public void updateTask(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, todo.getTask());
        values.put(COLUMN_DESCRIPTION, todo.getDescription()); // Added for description
        values.put(COLUMN_COMPLETED, todo.isCompleted() ? 1 : 0);
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
        db.close();
    }

    // Method to delete a task
    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)});
        db.close();
    }
}




