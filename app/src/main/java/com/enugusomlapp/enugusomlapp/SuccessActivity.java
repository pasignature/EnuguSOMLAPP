package com.enugusomlapp.enugusomlapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);

        //Capture button from layout
        Button ok_button = (Button)findViewById(R.id.success_okbtn);
        //Register the onClick listener with the implementation above
        ok_button.setOnClickListener(this);

        //get the strings from register activity
        Bundle bundle = getIntent().getExtras();
        String method = bundle.getString("method");
        String patientid = bundle.getString("patientid");
        String patientcode = bundle.getString("patientcode");
        String patientsname = bundle.getString("patientsname");
        String patientfname = bundle.getString("patientfname");
        String patientphone = bundle.getString("patientphone");

        //forge/compose the success msg
        String successmsg;
        if(method != null && method.equals("updateQRY")){
            successmsg = "Congrats "+ patientfname +" "+ patientsname +",\nYour data was successfully updated using Patientcode "+ patientcode+".";
        }else{
            successmsg = "Congrats "+ patientfname +" "+ patientsname +",\nYour data assigned id:"+patientid+" was created successfully.\nYour Patientcode "+ patientcode +" will be sent to "+ patientphone +".";
        }
        //Then you can set the text in the TextView:
        TextView txtView = (TextView) findViewById(R.id.success_msg);
        txtView.setText(successmsg);
    }

    public void onClick(View v){
        Intent gotomainHome = new Intent(this, MainActivity.class);
        startActivity(gotomainHome);
    }
}
