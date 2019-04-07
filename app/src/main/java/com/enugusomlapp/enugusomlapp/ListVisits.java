package com.enugusomlapp.enugusomlapp;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListVisits extends ListActivity {
    //initialize arraylist
    private ArrayList<String> results = new ArrayList<>();
    private ArrayList<String> desc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listvisits);

        //get the strings from success activity
        Bundle bundle = getIntent().getExtras();
        final String patientcode = bundle.getString("patientcode");
        final String childID = bundle.getString("childID");

        openAndQueryDatabase(patientcode,childID);
    }

    public void openAndQueryDatabase(String patientcode, String childID) {
        // 1. First we have to open our DbHelper class by creating a new object of that
        DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(this);
        // 2. get reference to writable DB
        SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();
        //decide which query to run
        String querypcode;
        //fetch child visits
         //querypcode = "SELECT * FROM registration_visithistory";
         querypcode = "SELECT historyID, health_facility, visitdatetime FROM registration_visithistory WHERE regmainFK = '" + patientcode + "' AND childFK = '" + childID + "'";
        if (childID.length()<=8 || childID.equals("1979")){
            //fetch main patient visits
            querypcode = "SELECT historyID, health_facility, visitdatetime FROM registration_visithistory WHERE regmainFK = '" + patientcode + "' AND childFK = 'NA'";
        }

        //System.out.println(querypcode+" spacebar "+patientcode+" spacebar "+childID);
        // 3. Get the newly inserted patientcode
        Cursor c = SQliteDB.rawQuery(querypcode, null);
        if (c.moveToFirst()) {
            do {
                String historyID = c.getString(c.getColumnIndex("historyID"));
                String health_facility = c.getString(c.getColumnIndex("health_facility"));
                String visitdatetime = c.getString(c.getColumnIndex("visitdatetime"));
                results.add(historyID);
                desc.add("Health Facility:\n" + health_facility + ", " + visitdatetime);
            } while (c.moveToNext());

            c.close();
            SQliteDB.close();

            MyAdapter myAdapter = new MyAdapter(this, results, desc);
            setListAdapter(myAdapter);
        } else {
            results.add("Empty");
            desc.add("NO VISITATION DATA FOUND.");
            MyAdapter myAdapter = new MyAdapter(this, results, desc);
            setListAdapter(myAdapter);

            SQliteDB.close();
            Toast.makeText(this.getApplicationContext(), "No visitation data found. Add a visit first.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String visitid = l.getItemAtPosition(position).toString();
        Toast.makeText(this, "Redirecting to "+visitid, Toast.LENGTH_LONG).show();


        AsyncGetVisitHistory visittasks = new AsyncGetVisitHistory(this);
        visittasks.execute("fetch_visitHistory",visitid);
    }




}