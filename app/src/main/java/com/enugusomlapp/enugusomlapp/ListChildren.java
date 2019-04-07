package com.enugusomlapp.enugusomlapp;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListChildren extends ListActivity {
    //initialize arraylist
    private ArrayList<String> results = new ArrayList<>();
    private ArrayList<String> desc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listchildren);

        //get the strings from success activity
        Bundle bundle = getIntent().getExtras();
        final String patientcode = bundle.getString("patientcode");

        openAndQueryDatabase(patientcode);
        //displayResultList();
       /* String[] dados = {"Item 1", "Item 2", "Item 3",
                "Item 4", "Item 5", "Item 6", "Item 7"};

        String[] dados2 = {"desc 1", "desc 2", "desc 3",
                "desc 4", "desc 5", "desc 6", "desc 7"};*/
    }

    private void openAndQueryDatabase(String patientcode) {
            // 1. First we have to open our DbHelper class by creating a new object of that
            DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(this);
            // 2. get reference to writable DB
            SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();
            // 3. Get the newly inserted patientcode
            Cursor c = SQliteDB.rawQuery("SELECT childID, fullname, sex FROM registration_children WHERE regmainFK = '"+patientcode+"'", null);
            if(c.moveToFirst()) {
                    do {
                        String childID = c.getString(c.getColumnIndex("childID"));
                        String fullname = c.getString(c.getColumnIndex("fullname"));
                        String sex = c.getString(c.getColumnIndex("sex"));
                        results.add(childID);
                        desc.add("Name: " + fullname + "- Sex: " + sex);
                    }while (c.moveToNext());

                c.close();
                SQliteDB.close();

                MyAdapter myAdapter = new MyAdapter(this, results, desc);
                setListAdapter(myAdapter);
            }else{
                results.add("Empty");
                desc.add("NO CHILD DATA FOUND.");
                MyAdapter myAdapter = new MyAdapter(this, results, desc);
                setListAdapter(myAdapter);

                SQliteDB.close();
                Toast.makeText(this.getApplicationContext(), "No child data found. Add a child first.", Toast.LENGTH_LONG).show();
            }
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String childid = l.getItemAtPosition(position).toString();
        Toast.makeText(this, "Redirecting to "+childid, Toast.LENGTH_LONG).show();


        AsyncGetChildProfile ChildFormtasks = new AsyncGetChildProfile(this);
        ChildFormtasks.execute("fetch_childprofile",childid);
    }


}