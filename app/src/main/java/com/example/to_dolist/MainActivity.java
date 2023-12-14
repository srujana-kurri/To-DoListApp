package com.example.to_dolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editTextTask;
    private EditText editTextDescription;
    private Button buttonAdd;
    private ListView listViewTasks;
    private List<Todo> todoList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewTasks = findViewById(R.id.listViewTasks);
        todoList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        loadTasks();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTaskDialog(position);
            }
        });

        listViewTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteTask(position);
                Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void loadTasks() {
        todoList.clear();
        todoList.addAll(databaseHelper.getAllTasks());
        TodoAdapter adapter = new TodoAdapter(this, todoList);
        listViewTasks.setAdapter(adapter);
    }

    private void addTask() {
        String task = editTextTask.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        if (!task.isEmpty()) {
            Todo newTask = new Todo(0, task, description, false);
            databaseHelper.addTask(newTask);
            editTextTask.setText("");
            editTextDescription.setText("");
            loadTasks();
        }
    }

    private void showTaskDialog(final int position) {
        final Todo todo = todoList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.custom_task_dialog);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set data to custom dialog layout
        TextView textViewTask = dialog.findViewById(R.id.textViewTask);
        TextView textViewDescription = dialog.findViewById(R.id.textViewDescription);
        Button buttonEdit = dialog.findViewById(R.id.buttonEdit);
        Button buttonDelete = dialog.findViewById(R.id.buttonDelete);

        textViewTask.setText(todo.getTask());
        textViewDescription.setText(todo.getDescription());

        // Set listeners for buttons
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(position);
                dialog.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(position);
                Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void showEditDialog(final int position) {
        final Todo todo = todoList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        // Create a layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.edit_task_dialog, null);

        // Create EditTexts for task and description
        final EditText editTextTask = dialogView.findViewById(R.id.editTextEditTask);
        editTextTask.setText(todo.getTask());
        final EditText editTextDescription = dialogView.findViewById(R.id.editTextEditDescription);
        editTextDescription.setText(todo.getDescription());

        builder.setView(dialogView);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedTask = editTextTask.getText().toString().trim();
                String updatedDescription = editTextDescription.getText().toString().trim();
                if (!updatedTask.isEmpty()) {
                    todo.setTask(updatedTask);
                    todo.setDescription(updatedDescription);
                    databaseHelper.updateTask(todo);
                    loadTasks();
                    Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteTask(int position) {
        Todo todo = todoList.get(position);
        databaseHelper.deleteTask(todo.getId());
        loadTasks();
    }
}



