package com.enugusomlapp.enugusomlapp;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SuccessChildFormActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successchildform);

        //get the strings from register activity OR from asynctasgetchildprofile
        Bundle bundle = getIntent().getExtras();
        String sex = bundle.getString("sex");
        final String patientcode = bundle.getString("patientcode");
        final String childID = bundle.getString("childID");
        String fullnames = bundle.getString("fullnames");
        String profiledata = bundle.getString("profiledata");

        //Then you can set the text in the TextView:
        ImageView userphotoM = (ImageView) findViewById(R.id.user_profile_photoM);
        ImageView userphotoF = (ImageView) findViewById(R.id.user_profile_photoF);
        ImageView userphotoN = (ImageView) findViewById(R.id.user_profile_photoN);
        if (sex != null && sex.equalsIgnoreCase("Male")) {
            userphotoM.setVisibility(View.VISIBLE);
        } else if (sex != null && sex.equalsIgnoreCase("Female")) {
            userphotoF.setVisibility(View.VISIBLE);
        } else {
            userphotoN.setVisibility(View.VISIBLE);
        }

        TextView txtView = (TextView) findViewById(R.id.patientcode);
        txtView.setText(patientcode);
        TextView txtViewnames = (TextView) findViewById(R.id.fullnames);
        txtViewnames.setText(fullnames);
        TextView txtViewprofiledata = (TextView) findViewById(R.id.profiledatum);
        txtViewprofiledata.setText(profiledata);

        //click listener for list child visits history
        TextView txtView_childvisit = (TextView) findViewById(R.id.View_childvisit);
        // add listener
        txtView_childvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 1. First we have to open our DbHelper class by creating a new object of that
                DbSQLiteHelper androidOpenDbHelperObj2 = new DbSQLiteHelper(SuccessChildFormActivity.this);
                // 2. get reference to writable DB
                SQLiteDatabase SQliteDB2 = androidOpenDbHelperObj2.getWritableDatabase();
                // 3. Get the newly inserted patientcode
                Cursor c2 = SQliteDB2.rawQuery("SELECT * FROM registration_visithistory WHERE regmainFK = '"+patientcode+"'", null);
                if(c2.moveToFirst()) {
                    c2.close();
                    SQliteDB2.close();

                    Toast.makeText(getApplicationContext(), "Listing children for patientcode: "+patientcode, Toast.LENGTH_LONG).show();
                    //redirect to open children visit list
                    Intent openVisitList = new Intent(SuccessChildFormActivity.this, ListVisits.class);
                    openVisitList.putExtra("patientcode", patientcode);
                    openVisitList.putExtra("childID", childID);
                    startActivity(openVisitList);
                }else{
                    c2.close();
                    SQliteDB2.close();
                    Toast.makeText(SuccessChildFormActivity.this, "Sorry, no visitation data found.", Toast.LENGTH_LONG).show();
                }
            }
        });



        //listener and alert dialogue for edit child
        TextView txtView_editchild = (TextView) findViewById(R.id.Editdata);
       // add listener
        txtView_editchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuccessChildFormActivity.this);
                // set title
                alertDialogBuilder.setTitle("Edit/Update OP Child Data");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure to proceed?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //redirect to open child form
                                Intent openEdichild = new Intent(SuccessChildFormActivity.this, ChildMainUpdate.class);
                                openEdichild.putExtra("patientcode", patientcode);
                                openEdichild.putExtra("childID", childID);
                                startActivity(openEdichild);
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
            public void onClick(View arg2) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuccessChildFormActivity.this);
                // set title
                alertDialogBuilder.setTitle("Delete Patient Data");
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
                                AsyncTasksDelete deleteTasts = new AsyncTasksDelete(SuccessChildFormActivity.this);
                                deleteTasts.execute("delete_Child_Formdata", patientcode);
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
        //get the strings from register activity
        Bundle bundle = getIntent().getExtras();
        String patientcode = bundle.getString("patientcode");
        String childID = bundle.getString("childID");
        String fullnames = bundle.getString("fullnames");
        switch (v.getId()) {
            case R.id.Add_childvisit:
                Toast.makeText(getApplicationContext(), "Child visit under patientcode: "+patientcode, Toast.LENGTH_LONG).show();
                //redirect to open child visit form
                Intent openaddchild = new Intent(SuccessChildFormActivity.this, VisitsActivity.class);
                openaddchild.putExtra("patientcode", patientcode);
                openaddchild.putExtra("childID", childID);
                openaddchild.putExtra("fullnames", fullnames);
                startActivity(openaddchild);
                break;

            case R.id.open_motherdata:
                //get patient data from SQlite Database using main reg AsyncTask
                BackgroundAsyncTasksSearch searchFormtasks = new BackgroundAsyncTasksSearch(this);
                searchFormtasks.execute("fetchdata",patientcode);
                break;

            default:
                break;

        }


    }

}