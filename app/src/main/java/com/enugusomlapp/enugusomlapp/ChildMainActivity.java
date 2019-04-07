package com.enugusomlapp.enugusomlapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChildMainActivity extends AppCompatActivity implements View.OnClickListener {
    //declare variables for the form
    private EditText childfullnameEditText;
    private Spinner sexEditText;
    private EditText d_obEditText;
    private EditText bloodgroupEditText;
    private EditText genotypeEditText;
    private Spinner HIVstatusEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.childmain);

        d_obEditText = (EditText) findViewById(R.id.child_dob);
        d_obEditText.setFocusable(false);
        d_obEditText.setTextColor(Color.GREEN);

        // Address the form fields
        childfullnameEditText = (EditText) findViewById(R.id.childfullname);
        sexEditText = (Spinner) findViewById(R.id.child_sex);
        bloodgroupEditText = (EditText) findViewById(R.id.child_bloodgroup);
        genotypeEditText = (EditText) findViewById(R.id.child_genotype);
        HIVstatusEditText = (Spinner) findViewById(R.id.child_HIVstatus);

        //Capture buttons from layout
        Button Processform = (Button) findViewById(R.id.childsubmit_button);
        Button gotosearchForm = (Button) findViewById(R.id.childcancel_button);
        //Register the onClick listener with the implementation above
        gotosearchForm.setOnClickListener(this);
        Processform.setOnClickListener(this);

        //get the strings from search success activity
        Bundle bundle = getIntent().getExtras();
        String patientcode = bundle.getString("patientcode");
        TextView txtView = (TextView) findViewById(R.id.OP_code);
        txtView.setText(patientcode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.childcancel_button:
                Intent gotoDashboard = new Intent(this, SearchActivity.class);
                startActivity(gotoDashboard);
                break;
            case R.id.child_dob:
                // Pega a data e hora corrente
                final Calendar c = Calendar.getInstance();

                Integer mYear = c.get(Calendar.YEAR);
                Integer mMonth = c.get(Calendar.MONTH);
                Integer mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ChildMainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear+=1;
                        String mt,dy;   //local variable
                        if(monthOfYear<10)
                            mt="0"+monthOfYear; //if month less than 10 then ad 0 before month
                        else mt=String.valueOf(monthOfYear);

                        if(dayOfMonth<10)
                            dy = "0"+dayOfMonth;
                        else dy = String.valueOf(dayOfMonth);
                        d_obEditText.setText(year+"-"+mt+"-"+dy);
                       /* TimePickerDialog timePickerDialog = new TimePickerDialog(ChildMainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String _dataHora = timeTextfield.getText().toString() + " " + hourOfDay + ":" + minute;
                                timeTextfield.setText(_dataHora);
                            }
                        }, 0, 0, false);
                        timePickerDialog.show();*/
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.childsubmit_button:
                //validations for the reg form
                int errorChecker = 0;
                final String childfullname = childfullnameEditText.getText().toString();
                if(!validateUserinput(childfullname) || (childfullname.length() > 60)) {
                    errorChecker = 1;
                    childfullnameEditText.setError("Empty or over 60 chars");
                }

                final String sex_gender = sexEditText.getSelectedItem().toString();
                if(!validateUserinput(sex_gender) || (sex_gender.length() > 7) || sex_gender.equalsIgnoreCase("Choose Gender")){
                    errorChecker = 2;
                    TextView errorText = (TextView)sexEditText.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(R.string.sex_prompt);//changes the selected item text to this
                }

                final String dob = d_obEditText.getText().toString();
                if(!validateUserinput(dob) || dob.length() >10) {
                    errorChecker = 3;
                    d_obEditText.setError("Empty or over 10 chars");
                }

                final String bloodgroup = bloodgroupEditText.getText().toString();
                final String genotype = genotypeEditText.getText().toString();
                String HIVstatus = HIVstatusEditText.getSelectedItem().toString();
                if(HIVstatus.equalsIgnoreCase("Choose HIV Status")){
                    HIVstatus = "unknown";
                }

                if(errorChecker <1){
                    //generate out patient unique code
                    //instead get the strings from search success activity
                    Bundle bundle = getIntent().getExtras();
                    String patientcode = bundle.getString("patientcode");
                    //announce the code
                    Toast.makeText(getApplicationContext(), "Registering child with patientcode: "+patientcode, Toast.LENGTH_LONG).show();

                    //generate datatime
                    SimpleDateFormat dateFormatterDP;
                    Calendar newDatetime = Calendar.getInstance();
                    dateFormatterDP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    final String todayDatetime = dateFormatterDP.format(newDatetime.getTime());//"2016-11-16 12:08:43";
                    final String lastupdateby = "SOMLAPP";

                    // form Validation Completed
                    // Inserting regForm data into the database
                    //save patient data to SQlite Database using AsyncTask //send to AsyncTask
                    AsyncTasksChildForm ChildFormtasks = new AsyncTasksChildForm(this);
                    ChildFormtasks.execute("save_childdata",patientcode,childfullname,sex_gender,dob,bloodgroup,genotype,HIVstatus,lastupdateby,todayDatetime,todayDatetime,lastupdateby);
                }else{
                    Toast.makeText(getApplicationContext(), "Correct the errors to proceed.", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }


    // validating empty inputs
    public boolean validateUserinput(String userInput){
        return (userInput != null) && (userInput.length() >= 1);
    }



}
