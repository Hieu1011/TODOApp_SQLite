package com.google.todoappsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    EditText title, description;
    TextView start, end;
    Button btnStart, btnEnd, btnSave;

    boolean isUpdate;
    long idTask;
    Task task;
    TaskDbHelper taskDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskDbHelper = new TaskDbHelper(this);

        Calendar calendar = Calendar.getInstance();
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH);
        int selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);
        if (isUpdate) {
            idTask = intent.getLongExtra("idTask", 2);
            task = taskDbHelper.getTaskByID(idTask);
        } else {
            findViewById(R.id.buttonDelete).setVisibility(View.GONE);
            task = new Task(0, "", "", "", "");
        }

        title = (EditText) findViewById(R.id.editTextTitle);
        start = (TextView) findViewById(R.id.editTextStart);
        end = (TextView) findViewById(R.id.editTextEnd);
        description = (EditText) findViewById(R.id.editTextDescription);
        btnStart = (Button) findViewById(R.id.buttonStart);
        btnEnd = (Button) findViewById(R.id.buttonEnd);
        btnSave = (Button) findViewById(R.id.buttonSave);

        title.setText(task.getTitle());
        start.setText(task.getStart());
        end.setText(task.getEnd());
        description.setText(task.getDescription());

        findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskDbHelper.deleteTaskByID(task.getId());
                setResult(RESULT_OK);
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        start.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
                datePickerDialog.show();
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        end.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
                datePickerDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().equals("") || start.getText().toString().equals("") || end.getText().toString().equals(""))
                    Toast.makeText(AddTaskActivity.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                else {
                    task.setTitle(title.getText().toString());
                    task.setStart(start.getText().toString());
                    task.setEnd(end.getText().toString());
                    task.setDescription(description.getText().toString());
                    if (isUpdate) {
                        taskDbHelper.updateTask(task);
                    } else {
                        taskDbHelper.insertTask(task);
                    }
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}