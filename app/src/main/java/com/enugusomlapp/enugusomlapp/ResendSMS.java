package com.enugusomlapp.enugusomlapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ResendSMS extends AppCompatActivity implements View.OnClickListener{
    // UI references.
    private EditText enterPhoneView;
    private ProgressBar progressBarcyclic;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resendsms);

        enterPhoneView = (EditText) findViewById(R.id.enterphpone);
        progressBarcyclic = (ProgressBar) findViewById(R.id.sendSMS_progress);
    }


    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.resendsmsBTN:
                // Store and validate values
                int errorChecker = 0;
                final String patientphone = enterPhoneView.getText().toString();
                if (!isNotEmpty(patientphone)) {
                    errorChecker = 1;
                    enterPhoneView.setError("Patient Phone cannot be empty.");
                }
                if (patientphone.length()>1 && !isValidLength(patientphone)) {
                    errorChecker = 1;
                    enterPhoneView.setError("Phone number must be 11 or 12 chars.");
                }

                if (errorChecker < 1) {
                    AsyncTasksSendSMS backgoundAsyncTasks = new AsyncTasksSendSMS();
                    backgoundAsyncTasks.execute(patientphone);
                } else {
                    Toast.makeText(getApplicationContext(), "You must correct the errors to proceed.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel_button:
                Intent returnto_home1 = new Intent(this, MainActivity.class);
                startActivity(returnto_home1);
                break;
            case R.id.homeimage:
                Intent returnto_home = new Intent(this, MainActivity.class);
                startActivity(returnto_home);
                break;
            default:
                break;
        }
    }

    
    // validating length
    private boolean isValidLength(String string){
        return string.length() >=11 && string.length() <=12;
    }

    // validating empty strings
    private boolean isNotEmpty(String string){
        return string != null && string.length() >0;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class AsyncTasksSendSMS extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            //super.onPreExecute();
            // show progress dialog
            progressBarcyclic.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
        
        @Override
        protected String doInBackground(String... params){
            //DB Class to perform DB related operations
            final DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(ResendSMS.this);

            String patientphone = params[0];
            String patientcode = androidOpenDbHelperObj.getPatientcode4sms(patientphone);
            if(patientcode.equalsIgnoreCase("NOTFOUND")){
                return patientcode;
            }else {
                String apiEndPoint = "http://www.enugusomlapp.com/android/";
                //send sms
                try{
                    String requestUrl5 = apiEndPoint +
                            "smsengine.php?action=sendmessage&" +
                            "patientcode=" + URLEncoder.encode(patientcode, "UTF-8") +
                            "&phone=" + URLEncoder.encode(patientphone, "UTF-8");

                    URL url = new URL(requestUrl5);
                    HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                    int responseCode4 = uc.getResponseCode();
                    if(responseCode4 == HttpURLConnection.HTTP_OK){
                        System.out.println(requestUrl5);
                        System.out.println(responseCode4);
                        System.out.println(uc.getResponseMessage());
                        uc.disconnect();
                        androidOpenDbHelperObj.close();
                        return "sent";
                    }else{
                        System.out.println(responseCode4);
                        System.out.println(uc.getResponseMessage());
                        uc.disconnect();
                        androidOpenDbHelperObj.close();
                        return "smsFailed";
                    }

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        return null;
        }

        @Override
        protected void onPostExecute(String result){
            if(result != null && result.equalsIgnoreCase("sent")){
                Toast.makeText(ResendSMS.this.getApplicationContext(), "Done, Patient Code was Sent successfully.", Toast.LENGTH_LONG).show();
            }else if(result != null && result.equalsIgnoreCase("NOTFOUND")){
                Toast.makeText(ResendSMS.this.getApplicationContext(), "No Patientcode match the phone", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ResendSMS.this.getApplicationContext(), "Send SMS Failed. Maybe Network Problems.", Toast.LENGTH_LONG).show();
            }

        progressBarcyclic.setVisibility(View.GONE);
        }
    }
}