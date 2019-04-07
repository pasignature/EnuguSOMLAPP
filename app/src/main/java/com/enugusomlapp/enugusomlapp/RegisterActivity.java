package com.enugusomlapp.enugusomlapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements OnClickListener{
    //declare variables for the form
    private EditText snameEditText;
    private EditText fnameEditText;
    private EditText onamesEditText;
    private EditText d_obEditText;
    private Spinner sex_genderEditText;
    private EditText telEditText;
    private Spinner maritalstatusEditText;
    private EditText occupationEditText;
    private EditText bloodgroupEditText;
    private EditText genotypeEditText;
    private Spinner HIVstatusEditText;
    private EditText ailmentEditText;
    private Spinner firstpregnancyEditText;
    private EditText childrenEditText;
    private EditText normal_birthEditText;
    private EditText cesarean_sectionEditText;
    private EditText tb_attendanceEditText;
    private EditText tb_contactEditText;
    private Spinner anyfamplanningEditText;
    private Spinner needfamplanningEditText;
    private EditText nok_fullnameEditText;
    private EditText nok_telEditText;
    private Spinner nokrelationshipEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Address the form fields
        snameEditText = (EditText) findViewById(R.id.sname);
        fnameEditText = (EditText) findViewById(R.id.fname);
        onamesEditText = (EditText) findViewById(R.id.onames);

        d_obEditText = (EditText) findViewById(R.id.d_ob);
        d_obEditText.setFocusable(false);
        d_obEditText.setTextColor(Color.GREEN);

        sex_genderEditText = (Spinner) findViewById(R.id.sex_gender);
        telEditText = (EditText) findViewById(R.id.tel);
        maritalstatusEditText = (Spinner) findViewById(R.id.maritalstatus);
        occupationEditText = (EditText) findViewById(R.id.occupation);
        bloodgroupEditText = (EditText) findViewById(R.id.bloodgroup);
        genotypeEditText = (EditText) findViewById(R.id.genotype);
        HIVstatusEditText = (Spinner) findViewById(R.id.HIVstatus);
        ailmentEditText = (EditText) findViewById(R.id.ailment);
        firstpregnancyEditText = (Spinner) findViewById(R.id.firstpregnancy);
        childrenEditText = (EditText) findViewById(R.id.children);
        normal_birthEditText = (EditText) findViewById(R.id.normal_birth);
        cesarean_sectionEditText = (EditText) findViewById(R.id.cesarean_section);
        tb_attendanceEditText = (EditText) findViewById(R.id.tb_attendance);
        tb_contactEditText = (EditText) findViewById(R.id.tb_contact);
        anyfamplanningEditText = (Spinner) findViewById(R.id.anyfamplanning);
        needfamplanningEditText = (Spinner) findViewById(R.id.needfamplanning);
        nok_fullnameEditText = (EditText) findViewById(R.id.nok_fullname);
        nok_telEditText = (EditText) findViewById(R.id.nok_tel);
        nokrelationshipEditText = (Spinner) findViewById(R.id.nok_relationship);

        //Capture buttons from layout
        Button Processform = (Button) findViewById(R.id.submit_button);
        Button gotodashboard = (Button) findViewById(R.id.cancel_button);
        //Register the onClick listener with the implementation above
        gotodashboard.setOnClickListener(this);
        Processform.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_button:
                Intent gotoDashboard = new Intent(this, MainActivity.class);
                startActivity(gotoDashboard);
                break;
            case R.id.d_ob:
                // Pega a data e hora corrente
                final Calendar c = Calendar.getInstance();

                Integer mYear = c.get(Calendar.YEAR);
                Integer mMonth = c.get(Calendar.MONTH);
                Integer mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
            case R.id.submit_button:
                //validations for the reg form
                int errorChecker = 0;
                final String fsname = snameEditText.getText().toString();
                if(!validateUserinput(fsname) || (fsname.length() > 20)) {
                    errorChecker = 1;
                    snameEditText.setError("Empty or over 20 chars");
                }
                final String ffname = fnameEditText.getText().toString();
                if(!validateUserinput(ffname) || (ffname.length() > 20)) {
                    errorChecker = 2;
                    fnameEditText.setError("Empty or over 20 chars");
                }
                final String fonames = onamesEditText.getText().toString();

                final String dob = d_obEditText.getText().toString();
                if(!validateUserinput(dob) || dob.length() >10) {
                    errorChecker = 4;
                    d_obEditText.setError("Empty or over 10 chars");
                }
                final String sex_gender = sex_genderEditText.getSelectedItem().toString();
                if(!validateUserinput(sex_gender) || (sex_gender.length() > 7) || sex_gender.equalsIgnoreCase("Choose Gender")){
                    errorChecker = 5;
                    TextView errorText = (TextView)sex_genderEditText.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(R.string.sex_prompt);//changes the selected item text to this
                }
                final String tel = telEditText.getText().toString();
                if(!validateUserinput(tel) || (tel.length() > 12) || !isNumeric(tel)) {
                    errorChecker = 6;
                    telEditText.setError("Empty or over 12 chars");
                }
                final String maritalstatus = maritalstatusEditText.getSelectedItem().toString();
                if(!validateUserinput(maritalstatus) || (maritalstatus.length() > 15) || maritalstatus.equalsIgnoreCase("Choose Marital Status")){
                    errorChecker = 7;
                    TextView errorText = (TextView)maritalstatusEditText.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(R.string.maritalstatus_prompt);//changes the selected item text to this
                }
                final String occupation = occupationEditText.getText().toString();
                if(!validateUserinput(occupation) || (occupation.length() > 30)) {
                    errorChecker = 8;
                    occupationEditText.setError("Empty or over 30 chars");
                }
                final String bloodgroup = bloodgroupEditText.getText().toString();
                final String genotype = genotypeEditText.getText().toString();
                String HIVstatus = HIVstatusEditText.getSelectedItem().toString();
                if(HIVstatus.equalsIgnoreCase("Choose HIV Status")){
                    HIVstatus = "unknown";
                }

                final String ailment = ailmentEditText.getText().toString();
                if(!validateUserinput(ailment) || (ailment.length() > 30)) {
                    errorChecker = 12;
                    ailmentEditText.setError("Empty or over 30 chars");
                }
                String firstpregnancy = firstpregnancyEditText.getSelectedItem().toString();
                if(firstpregnancy.equalsIgnoreCase("Is this your first pregnancy?")){
                    firstpregnancy = "NA";
                }
                final String children = childrenEditText.getText().toString();
                final String normal_birth = normal_birthEditText.getText().toString();
                final String cesarean_section = cesarean_sectionEditText.getText().toString();
                final String tb_attendance = tb_attendanceEditText.getText().toString();
                final String tb_contact = tb_contactEditText.getText().toString();

                String anyfamplanning = anyfamplanningEditText.getSelectedItem().toString();
                if(anyfamplanning.equalsIgnoreCase("Have you done family planning before?")){
                    anyfamplanning = "NA";
                }
                String needfamplanning = needfamplanningEditText.getSelectedItem().toString();
                if(needfamplanning.equalsIgnoreCase("Do you need family planning?")){
                    needfamplanning = "NA";
                }
                final String nok_fullname = nok_fullnameEditText.getText().toString();
                final String nok_tel = nok_telEditText.getText().toString();
                String nokrelationship = nokrelationshipEditText.getSelectedItem().toString();
                if(nokrelationship.equalsIgnoreCase("Relationship to Next-of-kin")){
                    nokrelationship = "NA";
                }

                if(errorChecker <1){
                    //generate out patient unique code
                    final String patientcode;
                    final int rand3digits1;
                    final int rand3digits2;
                    SecureRandom r = new SecureRandom();
                    rand3digits1 = ((1 + r.nextInt(2)) * 100000 + r.nextInt(100000));
                    rand3digits2 = ((1 + r.nextInt(2)) * 999999 + r.nextInt(999999));
                    String mergepcode = "ES" + rand3digits1 + rand3digits2; //e.g. ES123456789012
                    patientcode = mergepcode.substring(0,8); //e.g. ES123456

                    //generate datatime
                    SimpleDateFormat dateFormatter;
                    Calendar newDatetime = Calendar.getInstance();
                    dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    final String todayDatetime = dateFormatter.format(newDatetime.getTime());//"2016-11-16 12:08:43";
                    final String lastupdateby = "SOMLAPP";
                    //initial sync status
                    final String syncstatus = "unsynced";

                    // form Validation Completed
                    // Inserting regForm data into the database
                    //save patient data to SQlite Database using AsyncTask //send to AsyncTask
                    BackgroundAsyncTasks backgoundAsyncTasks = new BackgroundAsyncTasks(this);
                    backgoundAsyncTasks.execute("savedata",patientcode,fsname,ffname,fonames,dob,sex_gender,tel,maritalstatus,occupation,bloodgroup,genotype,HIVstatus,ailment,firstpregnancy,children,normal_birth,cesarean_section,tb_attendance,tb_contact,anyfamplanning,needfamplanning,nok_fullname,nok_tel,nokrelationship,lastupdateby,todayDatetime,todayDatetime,lastupdateby,syncstatus);
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

    // validating digits only
    public boolean isNumeric(String userDigits) {
        return TextUtils.isDigitsOnly(userDigits);
    }


}