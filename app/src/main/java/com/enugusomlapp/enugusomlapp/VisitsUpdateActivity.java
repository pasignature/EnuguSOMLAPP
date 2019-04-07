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

import java.util.Calendar;

public class VisitsUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    //declare variables for the form
    private EditText visitHistoryIDhiddenEditText;
    private EditText facilityEditText;
    private EditText healthfacility_streetEditText;
    private Spinner citystateEditText;
    private EditText healthfacility_telEditText;
    private EditText visit_reasonEditText;
    private EditText doctorEditText;
    private EditText collecteditemsEditText;
    private EditText commentsEditText;
    private EditText next_appointmentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitsupdate);

        //get arralist from asynctast update form
        Bundle formData = this.getIntent().getExtras();
        if(formData != null) {
            //fetch the records from db and populate the form
            visitHistoryIDhiddenEditText = (EditText) findViewById(R.id.visitHistoryID_hidden);
            visitHistoryIDhiddenEditText.setText(formData.getString("historyID"));
            facilityEditText = (EditText) findViewById(R.id.healthfacility);
            facilityEditText.setText(formData.getString("health_facility"));
            //citystateEditText = (Spinner) findViewById(R.id.citystate);
            //citystateEditText.setS(arrlist[2]);
            healthfacility_streetEditText = (EditText) findViewById(R.id.healthfacility_street);
            healthfacility_streetEditText.setText(formData.getString("facility_street"));
            healthfacility_telEditText = (EditText) findViewById(R.id.healthfacility_tel);
            healthfacility_telEditText.setText(formData.getString("facility_tel"));
            visit_reasonEditText = (EditText) findViewById(R.id.visit_reason);
            visit_reasonEditText.setText(formData.getString("visitreason"));
            doctorEditText = (EditText) findViewById(R.id.consultant_doctor);
            doctorEditText.setText(formData.getString("consultantdoctor"));
            collecteditemsEditText = (EditText) findViewById(R.id.collected_items);
            collecteditemsEditText.setText(formData.getString("collected_items"));
            commentsEditText = (EditText) findViewById(R.id.comments);
            commentsEditText.setText(formData.getString("comments"));
            next_appointmentEditText = (EditText) findViewById(R.id.next_appointment);
            next_appointmentEditText.setText(formData.getString("next_appointment"));
            next_appointmentEditText.setFocusable(false);
            next_appointmentEditText.setTextColor(Color.GREEN);
        }else{
            Toast.makeText(this.getApplicationContext(), "ERROR: Update list is empty.", Toast.LENGTH_LONG).show();
            finish();
        }

        // Address the form fields
        visitHistoryIDhiddenEditText = (EditText) findViewById(R.id.visitHistoryID_hidden);
        facilityEditText = (EditText) findViewById(R.id.healthfacility);
        citystateEditText = (Spinner) findViewById(R.id.citystate);
        healthfacility_streetEditText = (EditText) findViewById(R.id.healthfacility_street);
        healthfacility_telEditText = (EditText) findViewById(R.id.healthfacility_tel);
        visit_reasonEditText = (EditText) findViewById(R.id.visit_reason);
        doctorEditText = (EditText) findViewById(R.id.consultant_doctor);
        collecteditemsEditText = (EditText) findViewById(R.id.collected_items);
        commentsEditText = (EditText) findViewById(R.id.comments);

        //Capture buttons from layout
        Button Processform = (Button) findViewById(R.id.Vsubmit_button);
        Button gotosearchForm = (Button) findViewById(R.id.Vcancel_button);
        //Register the onClick listener with the implementation above
        gotosearchForm.setOnClickListener(this);
        Processform.setOnClickListener(this);

        //get the strings from search success activity
        Bundle formData2 = this.getIntent().getExtras();
        String historyID = formData2.getString("historyID");
        TextView txtView = (TextView) findViewById(R.id.OP_code);
        txtView.setText(historyID);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Vcancel_button:
                Intent gotoDashboard = new Intent(this, SearchActivity.class);
                startActivity(gotoDashboard);
                break;
            case R.id.next_appointment:
                // Pega a data e hora corrente
                final Calendar c = Calendar.getInstance();

                Integer mYear = c.get(Calendar.YEAR);
                Integer mMonth = c.get(Calendar.MONTH);
                Integer mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(VisitsUpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        next_appointmentEditText.setText(dy+"-"+mt+"-"+year);
                       /* TimePickerDialog timePickerDialog = new TimePickerDialog(VisitsActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
            case R.id.Vsubmit_button:
                //validations for the reg form
                int errorChecker = 0;
                final String health_facility = facilityEditText.getText().toString();
                if(!validateUserinput(health_facility) || (health_facility.length() > 50)) {
                    errorChecker = 1;
                    facilityEditText.setError("Empty or over 50 chars");
                }
                final String facility_street = healthfacility_streetEditText.getText().toString();
                if(!validateUserinput(facility_street) || (facility_street.length() > 160)) {
                    errorChecker = 1;
                    healthfacility_streetEditText.setError("Empty or over 30 chars");
                }

                final String citystate = citystateEditText.getSelectedItem().toString();
                if(!validateUserinput(citystate) || (citystate.length() > 160) || citystate.equalsIgnoreCase("Facility City/State")){
                    errorChecker = 2;
                    TextView errorText = (TextView)citystateEditText.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(R.string.citystate_prompt);//changes the selected item text to this
                }

                final String historyID = visitHistoryIDhiddenEditText.getText().toString();
                final String facility_tel = healthfacility_telEditText.getText().toString();
                final String visitreason = visit_reasonEditText.getText().toString();
                final String consultantdoctor = doctorEditText.getText().toString();
                final String collected_items = collecteditemsEditText.getText().toString();
                final String comments = commentsEditText.getText().toString();
                final String next_appointment = next_appointmentEditText.getText().toString();
                Bundle formData3 = this.getIntent().getExtras();
                String visitdatetime = formData3.getString("visitdatetime");

                if(errorChecker <1){
                    //generate out patient unique code
                    //announce the code
                    Toast.makeText(getApplicationContext(), "Updating History with ID: "+historyID, Toast.LENGTH_LONG).show();

                    // form Validation Completed
                    // Inserting regForm data into the database
                    //save patient data to SQlite Database using AsyncTask //send to AsyncTask
                    AsycTsVisitsFormUpdate2 VisitFormtasks = new AsycTsVisitsFormUpdate2(this);
                    VisitFormtasks.execute("update_visitdata",historyID,health_facility,facility_street,citystate,facility_tel,visitreason,consultantdoctor,collected_items,comments,next_appointment,visitdatetime);
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
