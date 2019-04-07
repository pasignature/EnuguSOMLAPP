package com.enugusomlapp.enugusomlapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SuccessVisitFormActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successvisitform);

        //get the strings from register activity
        Bundle bundle = getIntent().getExtras();
        final String historyID = bundle.getString("historyID");
        String health_facility = bundle.getString("health_facility");
        String profiledata = bundle.getString("profiledata");

        TextView txtViewnames = (TextView) findViewById(R.id.facilityname);
        txtViewnames.setText(health_facility);
        TextView txtViewprofiledata = (TextView) findViewById(R.id.profiledatum);
        txtViewprofiledata.setText(profiledata);

        //listener and alert dialogue for edit visit
        TextView txtView_visit = (TextView) findViewById(R.id.Editdata);
        txtView_visit.setOnClickListener(this);
       // add listener
        txtView_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuccessVisitFormActivity.this);
                // set title
                alertDialogBuilder.setTitle("Edit/Update Visit History");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //send to asynctask
                                AsycTsVisitsFormUpdate backgoundAsyncTasks = new AsycTsVisitsFormUpdate(SuccessVisitFormActivity.this);
                                backgoundAsyncTasks.execute("populate_visitupdateForm",historyID);
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });


        //listener and alert dialogue for delete
        TextView txtViewDelete = (TextView) findViewById(R.id.deldata);
        // add listener
        txtViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuccessVisitFormActivity.this);
                // set title
                alertDialogBuilder.setTitle("Delete Patient Visit Data");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Unreversible Action. Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                // MainActivity.this.finish();
                                // This starts the AsyncTask
                                AsyncTasksDelete deleteTasts = new AsyncTasksDelete(SuccessVisitFormActivity.this);
                                deleteTasts.execute("delete_VISIT_data", historyID);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });


    }


    @Override
    public void onClick(View v) {
        //get the strings from asynctaskvisit register activity
        Bundle bundle = getIntent().getExtras();
        String historyID = bundle.getString("historyID");
        String health_facility = bundle.getString("health_facility");
        String facility_street = bundle.getString("facility_street");
        String citystate = bundle.getString("citystate");

        switch (v.getId()){
            case R.id.open_OnGooglemap:                                                     //TODO configure google street map
                //open health facility on google map
                Intent intent = new Intent(this, HFGMapActivity.class);
                intent.putExtra("historyID", historyID);
                intent.putExtra("health_facility", health_facility);
                intent.putExtra("facility_street", facility_street);
                intent.putExtra("citystate", citystate);
                this.startActivity(intent);
                break;
            case R.id.homeimage:
                Intent returnto_home = new Intent(this, MainActivity.class);
                startActivity(returnto_home);
                break;
            default:
                break;
        }


    }

}