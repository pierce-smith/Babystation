package com.piercesmith.babystation;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class AddLog extends AppCompatActivity {

    // Instantiating the form variables
    DatabaseHelper db;
    EditText etTitle, etChild, etComments;
    Button confirm;
    RadioButton feeding, changing, milestone, other;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        // Setting form variables
        etTitle = (EditText) findViewById(R.id.txtTitle);
        etChild = (EditText) findViewById(R.id.txtChild);
        etComments = (EditText) findViewById(R.id.txtComments);
        feeding = (RadioButton) findViewById(R.id.rbtnFeeding);
        changing = (RadioButton) findViewById(R.id.rbtnChanging);
        milestone = (RadioButton) findViewById(R.id.rbtnMilestone);
        other = (RadioButton) findViewById(R.id.rbtnOther);
        confirm = (Button) findViewById(R.id.btnConfirm);

        Confirm();
    }

    public void Confirm() {
        // If confirm is clicked, validate data
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    // Retrieve data
                    String title = etTitle.getText().toString();
                    String child = etChild.getText().toString();
                    String comments = etComments.getText().toString();

                    // Checking for valid data
                    if (TextUtils.isEmpty(title)) {
                        etTitle.setError("The title for the log cannot be empty.");
                        return;
                    }
                    else if (TextUtils.isEmpty(child)) {
                        etChild.setError("You must enter your child's name.");
                        return;
                    }
                    else {
                        // Getting category selection
                        setCategory();

                        // Getting and setting current time
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy 'at' hh:mm a.", Locale.getDefault());
                        String time = sdf.format(new Date());

                        // Checking if data insertion was successful
                        boolean success = db.insertData(title, child, category, comments, time);
                        if (success == true) {
                            // Success message
                            Toast.makeText(getApplicationContext(), "Log added successfully!", Toast.LENGTH_LONG).show();

                            // Reset textboxes to empty strings
                            etTitle.setText("");
                            etChild.setText("");
                            etComments.setText("");
                        }
                        else {
                            showAlertDialog(v);
                        }
                        return;
                    }
                } catch (SQLException e) {
                    showAlertDialog(v);
                    e.printStackTrace();
                }
            }
        });
    }

    public void setCategory() {
        if (feeding.isChecked()) {
            category = "Feeding";
        }
        else if (changing.isChecked()) {
            category = "Changing";
        }
        else if (milestone.isChecked()) {
            category = "Milestone";
        }
        else if (other.isChecked()) {
            category = "Other";
        }
    }

    public void showAlertDialog(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Unexpected error!");
        alert.setMessage("An unexpected error occurred. Please contact technical support.");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No action needed
            }
        });
        alert.create().show();
    }
}
