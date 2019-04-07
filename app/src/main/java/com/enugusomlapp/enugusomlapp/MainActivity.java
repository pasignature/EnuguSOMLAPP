package com.enugusomlapp.enugusomlapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

      //String patientcode = "ES210391";
        //int patientcode = 0;
       /* ContentValues CnVs = new ContentValues();
        CnVs.put("syncStatus","unsynced");
        DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(this);
        SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();

        //String updateStatus = SQliteDB.update("registration_main", CnVs, "patientcode = ?", new String[]{patientcode});
        int updateStatus = SQliteDB.update("registration_main", CnVs,null,null);
        if(updateStatus >0){
            Toast.makeText(getApplicationContext(), "Congrats "+updateStatus+ "Rows updated successfully!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Unknown Error occurred!"+updateStatus, Toast.LENGTH_LONG).show();
        }*/

        //Capture button from layout
        Button regbutton = (Button)findViewById(R.id.reg_button);
        Button searchbutton = (Button)findViewById(R.id.search_button);
        Button buttonresendsms = (Button)findViewById(R.id.resendsmsBTN);
        Button uploadButton = (Button)findViewById(R.id.upload_Button);
        //Register the onClick listener with the implementation above
        regbutton.setOnClickListener(this);
        searchbutton.setOnClickListener(this);
        buttonresendsms.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.reg_button:
                Intent gotomainReg = new Intent(this, RegisterActivity.class);
                startActivity(gotomainReg);
                break;
            case R.id.search_button:
                Intent gotosearch = new Intent(this, SearchActivity.class);
                startActivity(gotosearch);
                break;
            case R.id.resendsmsBTN:
                Intent gotoResendsms = new Intent(this, ResendSMS.class);
                startActivity(gotoResendsms);
                break;
            case R.id.upload_Button:

                //DB Class to perform DB related operations
                final DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(this);
                if(androidOpenDbHelperObj.getAllRegMain().size() >0){
                    if(androidOpenDbHelperObj.dbSyncCountRegMain() >0){

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            // set title
                            alertDialogBuilder.setTitle("Upload Data to Central Server.");
                            // set dialog message
                            alertDialogBuilder
                                    .setMessage("Are you sure to proceed?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes, Upload",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            //must authenticate first
                                            Intent gotoAuthenticate = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(gotoAuthenticate);
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

                    }else{
                        Toast.makeText(getApplicationContext(), "APP and Remote Central DBs are in Sync!", Toast.LENGTH_LONG).show();
                        break;
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No data in APP DB to perform upload action.", Toast.LENGTH_LONG).show();
                    break;
                }

                break;
            case R.id.exitApp:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set title
                alertDialogBuilder.setTitle("Terminate and Exit EnuguSOMLAPP");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure to proceed?")
                        .setCancelable(false)
                        .setPositiveButton("Yes, Exit APP",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //terminate and exit app
                                finish();
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
                break;
            default:
                break;

        }
    }

}