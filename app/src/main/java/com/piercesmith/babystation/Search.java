package com.piercesmith.babystation;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Search extends AppCompatActivity implements UpdateDialog.UpdateDialogListener {

    // Instantiating form variables
    DatabaseHelper db;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    ListView result;
    EditText etSearch, etID;
    Button btnSearch, update, delete;
    TextView found;

    int disabled = Color.parseColor("#999999");
    int enabled = Color.parseColor("#C1E1DC");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listItem = new ArrayList<>();

        // Setting variables
        db = new DatabaseHelper(this);
        etSearch = (EditText) findViewById(R.id.txtSearch);
        etID = (EditText) findViewById(R.id.txtID);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        update = (Button) findViewById(R.id.btnUpdate);
        delete = (Button) findViewById(R.id.btnDelete);
        found = (TextView) findViewById(R.id.tvFound);
        result = (ListView) findViewById(R.id.lvSearchResult);

        etID.setEnabled(false);
        update.setEnabled(false);
        update.setBackgroundColor(disabled);
        delete.setEnabled(false);
        delete.setBackgroundColor(disabled);

        Search();
        Update();
        Delete();
    }

    public void Search() {
        // Searching records
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Make sure that something was entered into txtSearch
                    String search = etSearch.getText().toString();
                    if (TextUtils.isEmpty(search)) {
                        etSearch.setError("You did not enter a title to search.");
                        return;
                    }
                    else {
                        // Good data
                        Cursor c = db.searchData(search);
                        if (c.getCount() == 0) {
                            Toast.makeText(getApplicationContext(), "No log found.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            found.setText("Result(s) found!");
                            etID.setEnabled(true);
                            update.setEnabled(true);
                            update.setBackgroundColor(enabled);
                            delete.setEnabled(true);
                            delete.setBackgroundColor(enabled);

                            StringBuffer buffer = new StringBuffer();
                            while (c.moveToNext()) {
                                listItem.add("\nID: " + c.getString(0) + "\n"
                                        + "Title: " + c.getString(1) + "\n"
                                        + "Child: " + c.getString(2) + "\n"
                                        + "Category: " + c.getString(3) + "\n"
                                        + "Comments: " + c.getString(4) + "\n"
                                        + c.getString(5) + "\n");
                            }
                            c.close();
                            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listItem);
                            result.setAdapter(adapter);
                        }
                    }
                } catch (Exception e) {
                    showAlertDialog(v);
                    e.printStackTrace();
                }
            }
        });
    }

    public void Update() {
        // Updating the record
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    String id = etID.getText().toString();
                    if (TextUtils.isEmpty(id)) {
                        etID.setError("You must enter an ID number to update a log.");
                        return;
                    }
                    else {
                        updateDialog(v);
                    }
                } catch (SQLException e) {
                    showAlertDialog(v);
                    e.printStackTrace();
                }
            }
        });
    }

    public void Delete() {
        // Deleting the searched record
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    String id = etID.getText().toString();
                    if (TextUtils.isEmpty(id)) {
                        etID.setError("You must enter an ID number to delete a log.");
                        return;
                    }
                    else {
                        // Good data
                        Integer deletedRows = db.deleteData(id);
                        if (deletedRows > 0) {
                            etSearch.setText("");
                            etID.setText("");
                            etID.setEnabled(false);
                            update.setEnabled(false);
                            update.setBackgroundColor(disabled);
                            delete.setEnabled(false);
                            delete.setBackgroundColor(disabled);
                            found.setText("");
                            result.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Log successfully deleted.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error deleting log.", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (SQLException e) {
                    showAlertDialog(v);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void UpdateData(String title, String child, String category, String comments) {
        boolean updated = db.updateData(etID.getText().toString(), title, child, category, comments);
        if (updated == true) {
            etSearch.setText("");
            etID.setText("");
            etID.setEnabled(false);
            update.setEnabled(false);
            update.setBackgroundColor(disabled);
            delete.setEnabled(false);
            delete.setBackgroundColor(disabled);
            found.setText("");
            result.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Log successfully updated.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Error updating log.", Toast.LENGTH_LONG).show();
        }
    }

    public void updateDialog(View v) {
        UpdateDialog updateDialog = new UpdateDialog();
        updateDialog.show(getSupportFragmentManager(), "Update Dialog");
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
