package com.example.to_dolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {
    private Context context;
    private List<Todo> todoList;

    public TodoAdapter(Context context, List<Todo> todoList) {
        super(context, R.layout.todo_item, todoList);
        this.context = context;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.todo_item, parent, false);

        TextView textViewTask = row.findViewById(R.id.textViewTask);
        textViewTask.setText(todoList.get(position).getTask());

        return row;
    }
}

