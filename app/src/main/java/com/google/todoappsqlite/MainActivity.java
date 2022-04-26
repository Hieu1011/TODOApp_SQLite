package com.google.todoappsqlite;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> taskArrayList;
    ListView listViewTask;
    MyAdapter myAdapter;
    TaskDbHelper taskDbHelper;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        loadDbTask();
                        myAdapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDbHelper = new TaskDbHelper(this);
        taskArrayList = new ArrayList<Task>();
        loadDbTask();
        myAdapter = new MyAdapter(taskArrayList);
        listViewTask = findViewById(R.id.listViewTasks);
        listViewTask.setAdapter(myAdapter);

        findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                intent.putExtra("isUpdate", false);
                activityResultLauncher.launch(intent);
            }
        });

        listViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Task task = (Task) myAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                intent.putExtra("isUpdate", true);
                intent.putExtra("idTask", task.getId());
                activityResultLauncher.launch(intent);
            }
        });
    }
    private void loadDbTask() {
        taskArrayList.clear();
        taskArrayList.addAll(taskDbHelper.getAllTasks());
    }
}