package com.enugusomlapp.enugusomlapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    //declare variables for the form
    private EditText search_boxEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        //Capture buttons from layout
        TextView cancelthesearch = (TextView) findViewById(R.id.cancel_button);
        TextView submit_searchbutton = (TextView) findViewById(R.id.submit_searchbutton);
        //Register the onClick listener with the implementation above
        submit_searchbutton.setOnClickListener(this);
        cancelthesearch.setOnClickListener(this);

        // Address the form fields
        search_boxEditText = (EditText) findViewById(R.id.search_box);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_searchbutton:
                //validations for the search form
                int errorChecker = 0;
                final String patientcode2search = search_boxEditText.getText().toString();
                if(!validateUserinput(patientcode2search) || patientcode2search.length() != 8 || !isalphaNumeric(patientcode2search)) {
                    errorChecker = 1;
                    search_boxEditText.setError("Empty, Invalid or over 8 chars");
                }

                if(errorChecker <1){
                    // form Validation Completed
                    //get patient data from SQlite Database using AsyncTask
                    // This starts the AsyncTask
                    BackgroundAsyncTasksSearch searchFormtasks = new BackgroundAsyncTasksSearch(this);
                    searchFormtasks.execute("fetchdata",patientcode2search.trim().toUpperCase());
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid Patient Code.", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.cancel_button:
                Intent returnto_choosesearch = new Intent(this, MainActivity.class);
                startActivity(returnto_choosesearch);
                break;
            case R.id.homeimage:
                Intent returnto_home = new Intent(this, MainActivity.class);
                startActivity(returnto_home);
                break;
            default:
                break;
        }
    }

    // validating empty inputs
    public boolean validateUserinput(String userInput){
        return (userInput != null) && (userInput.length() == 8);
    }

    // validating digits only
    public boolean isalphaNumeric(String userDigits) {
        String pcode = userDigits.trim().toUpperCase();
        String alpaOnly = pcode.substring(0,2); //ES
        String digitsOnly = pcode.substring(2); //123456
        return alpaOnly.equals("ES") && digitsOnly.length() == 6 && TextUtils.isDigitsOnly(digitsOnly);
    }


}
