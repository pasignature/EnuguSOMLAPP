package com.enugusomlapp.enugusomlapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SuccessSearchActivity extends AppCompatActivity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successsearch);

        //get the strings from register activity
        Bundle bundle = getIntent().getExtras();
        String sex = bundle.getString("sex");
        final String patientcode = bundle.getString("patientcode");
        String fullnames = bundle.getString("fullnames");
        String profiledata = bundle.getString("profiledata");

        //Then you can set the text in the TextView:
        ImageView userphotoM = (ImageView) findViewById(R.id.user_profile_photoM);
        ImageView userphotoF = (ImageView) findViewById(R.id.user_profile_photoF);
        ImageView userphotoN = (ImageView) findViewById(R.id.user_profile_photoN);
        if(sex != null && sex.equalsIgnoreCase("Male")){
            userphotoM.setVisibility(View.VISIBLE);
        }else if(sex != null && sex.equalsIgnoreCase("Female")){
            userphotoF.setVisibility(View.VISIBLE);
        }else{
            userphotoN.setVisibility(View.VISIBLE);
        }

        TextView txtView = (TextView) findViewById(R.id.patientcode);
        txtView.setText(patientcode);
        TextView txtViewnames = (TextView) findViewById(R.id.names);
        txtViewnames.setText(fullnames);
        TextView txtViewprofiledata = (TextView) findViewById(R.id.profiledatum);
        txtViewprofiledata.setText(profiledata);

        TextView txtView_open_childdata = (TextView) findViewById(R.id.open_childdata);
        txtView_open_childdata.setOnClickListener(this);

        TextView txtView_addchild = (TextView) findViewById(R.id.Addchild);
        txtView_addchild.setOnClickListener(this);


        //listener and alert dialogue for add main visit
        TextView txtView_editmain = (TextView) findViewById(R.id.Editdata);
       // add listener
        txtView_editmain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuccessSearchActivity.this);
                // set title
                alertDialogBuilder.setTitle("Edit/Update Out Patient Data");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure to proceed?")
                        .setCancelable(false)
                        .setPositiveButton("Yes, Proceed",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //redirect to open child form
                                Intent openaddchild = new Intent(SuccessSearchActivity.this, RegisterUpdateActivity.class);
                                openaddchild.putExtra("patientcode", patientcode);
                                startActivity(openaddchild);
                            }
                        })
                        .setNegativeButton("No, Cancel",new DialogInterface.OnClickListener() {
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
        txtViewDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuccessSearchActivity.this);
                // set title
                alertDialogBuilder.setTitle("Delete Patient Data");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Unreversible Action. Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes, Proceed",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                               // if this button is clicked, close
                               // current activity
                               // MainActivity.this.finish();
                               // This starts the AsyncTask
                                AsyncTasksDelete delete = new AsyncTasksDelete(SuccessSearchActivity.this);
                                delete.execute("delete_MainForm_data",patientcode);
                            }
                        })
                        .setNegativeButton("No, Cancel",new DialogInterface.OnClickListener() {
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





    }



    @Override
    public void onClick(View v){
        //get the strings from search activity
        Bundle bundle = getIntent().getExtras();
        String patientcode = bundle.getString("patientcode");
        String fullnames = bundle.getString("fullnames");

        switch (v.getId()) {
            case R.id.Addchild:
                //redirect to open child form
                Intent openaddchild = new Intent(SuccessSearchActivity.this, ChildMainActivity.class);
                openaddchild.putExtra("patientcode", patientcode);
                startActivity(openaddchild);
                break;

            case R.id.open_childdata:
                // 1. First we have to open our DbHelper class by creating a new object of that
                DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(this);
                // 2. get reference to writable DB
                SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();
                // 3. Get the newly inserted patientcode
                Cursor c = SQliteDB.rawQuery("SELECT * FROM registration_children WHERE regmainFK = '"+patientcode+"'", null);
                if(c.moveToFirst()) {
                    c.close();
                    SQliteDB.close();

                    //redirect to open children list
                    Intent openChildrenList = new Intent(SuccessSearchActivity.this, ListChildren.class);
                    openChildrenList.putExtra("patientcode", patientcode);
                    startActivity(openChildrenList);
                }else{
                    c.close();
                    SQliteDB.close();
                    Toast.makeText(this.getApplicationContext(), "Sorry: no child data found.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.Add_visit:
                //redirect to open child visit form
                Intent openVisitform = new Intent(SuccessSearchActivity.this, VisitsActivity.class);
                openVisitform.putExtra("patientcode", patientcode);
                openVisitform.putExtra("fullnames", fullnames);
                openVisitform.putExtra("childID", "1979");
                startActivity(openVisitform);
                break;

            case R.id.View_visit:
                // 1. First we have to open our DbHelper class by creating a new object of that
                DbSQLiteHelper androidOpenDbHelperObj2 = new DbSQLiteHelper(this);
                // 2. get reference to writable DB
                SQLiteDatabase SQliteDB2 = androidOpenDbHelperObj2.getWritableDatabase();
                // 3. Get the newly inserted patientcode
                Cursor c2 = SQliteDB2.rawQuery("SELECT * FROM registration_visithistory WHERE regmainFK = '"+patientcode+"'", null);
                if(c2.moveToFirst()) {
                    c2.close();
                    SQliteDB2.close();

                    //redirect to open main patient visit list
                    Intent openVisitList = new Intent(SuccessSearchActivity.this, ListVisits.class);
                    openVisitList.putExtra("patientcode", patientcode);
                    openVisitList.putExtra("childID", "1979");
                    startActivity(openVisitList);
                }else{
                    c2.close();
                    SQliteDB2.close();
                    Toast.makeText(this.getApplicationContext(), "Sorry, no visitation data found.", Toast.LENGTH_LONG).show();
                }
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