package com.piercesmith.babystation;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity implements ChildDialog.ChildDialogListener {

    TextView home, sort;
    DatabaseHelper db;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    ListView logs;
    String category;
    private Spinner ddlSort;
    private static final String[] paths = {"Most Recent", "Child", "Feeding", "Changing", "Milestone", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHelper(this);

        listItem = new ArrayList<>();

        home = (TextView) findViewById(R.id.tvHome);
        logs = (ListView) findViewById(R.id.lvLogDisplay);

        // Drop down list
        sort = (TextView) findViewById(R.id.tvSort);
        ddlSort = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,paths);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlSort.setAdapter(spinAdapter);

        ddlSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                try {
                    int viewBy = ddlSort.getSelectedItemPosition();
                    switch (viewBy) {
                        case 0:
                            listItem.clear();
                            displayRecent();
                            break;
                        case 1:
                            listItem.clear();
                            childDialog(v);
                            break;
                        case 2:
                            listItem.clear();
                            category = "Feeding";
                            displaySort(category);
                            break;
                        case 3:
                            listItem.clear();
                            category = "Changing";
                            displaySort(category);
                            break;
                        case 4:
                            listItem.clear();
                            category = "Milestone";
                            displaySort(category);
                            break;
                        case 5:
                            listItem.clear();
                            category = "Other";
                            displaySort(category);
                            break;
                    }
                } catch (Exception e) {
                    showAlertDialog(v);
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing
            }
        });
    }

    public void displayRecent() {
        Cursor c = db.recentLogs();
        if (c.getCount() == 0) {
            sort.setVisibility(View.GONE);
            ddlSort.setVisibility(View.GONE);
            home.setText("You have not entered any logs.\n\nTry adding one from the menu.");
            logs.setVisibility(View.GONE);
        }
        else {
            sort.setVisibility(View.VISIBLE);
            ddlSort.setVisibility(View.VISIBLE);
            home.setText("Most Recent");
            logs.setVisibility(View.VISIBLE);
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                listItem.add("\nTitle: " + c.getString(1) + "\n"
                        + "Child: " + c.getString(2) + "\n"
                        + "Category: " + c.getString(3) + "\n"
                        + "Comments: " + c.getString(4) + "\n"
                        + c.getString(5) + "\n");
            }
            c.close();
            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listItem);
            logs.setAdapter(adapter);
        }
    }

    public void displaySort(String category) {
        Cursor c = db.sortCat(category);
        if (c.getCount() == 0) {
            home.setText("No logs in this category.");
            logs.setVisibility(View.GONE);
        }
        else {
            home.setText(category);
            logs.setVisibility(View.VISIBLE);
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                listItem.add("\nTitle: " + c.getString(1) + "\n"
                        + "Child: " + c.getString(2) + "\n"
                        + "Comments: " + c.getString(4) + "\n"
                        + c.getString(5) + "\n");
            }
            c.close();
            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listItem);
            logs.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listItem.clear();
        displayRecent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivity(new Intent(ScrollingActivity.this, AddLog.class));
        }
        else if (id == R.id.action_search) {
            startActivity(new Intent(ScrollingActivity.this, Search.class));
        }
        else if (id == R.id.action_about) {
            startActivity(new Intent(ScrollingActivity.this, About.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayChild(String name) {
        Cursor c = db.sortChild(name);
        if (c.getCount() == 0) {
            home.setText("No logs found for this child.");
            logs.setVisibility(View.GONE);
        }
        else {
            home.setText(name);
            logs.setVisibility(View.VISIBLE);
            while (c.moveToNext()) {
                listItem.add("\nTitle: " + c.getString(1) + "\n"
                        + "Category: " + c.getString(3) + "\n"
                        + "Comments: " + c.getString(4) + "\n"
                        + c.getString(5) + "\n");
            }
            c.close();
        }
    }

    public void childDialog(View v) {
        ChildDialog childDialog = new ChildDialog();
        childDialog.show(getSupportFragmentManager(), "Child Dialog");
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