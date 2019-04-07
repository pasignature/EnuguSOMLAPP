package com.enugusomlapp.enugusomlapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener{

    // UI references.
    private View mProgressView;
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mProgressView = findViewById(R.id.login_progress);
        mEmailView = (EditText) findViewById(R.id.signinEmail);
        mPasswordView = (EditText) findViewById(R.id.signinPassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signinBTN:
                // Store and validate values at the time of the login attempt.
                int errorChecker = 0;
                final String email = mEmailView.getText().toString();
                if (!isNotEmpty(email)) {
                    errorChecker = 1;
                    mEmailView.setError("Email cannot be empty.");
                }
                if (email.length()>1 && !isValidEmail(email)) {
                    errorChecker = 1;
                    mEmailView.setError("Email is invalid.");
                }
                if (email.length()>1 && !isValidLengthshort(email)) {
                    errorChecker = 1;
                    mEmailView.setError("Email is too short. Must be 8 chars or more.");
                }
                if (email.length()>1 && !isValidLengthlong(email)) {
                    errorChecker = 1;
                    mEmailView.setError("Email is too long. Must be 50 chars or less.");
                }

                final String password = mPasswordView.getText().toString();
                if (!isNotEmpty(password)) {
                    errorChecker = 1;
                    mPasswordView.setError("Password cannot be empty.");
                }
                if (password.length()>1 && !isValidLengthshortPW(password)) {
                    errorChecker = 1;
                    mPasswordView.setError("Password is too short. Must be 6 chars or more.");
                }

                if (errorChecker < 1) {
                    // form Validation Completed, authenticate and handover heavy tasks to AsyncTask
                    //UserLoginTask mAuthTask = new UserLoginTask(email, password);
                    //mAuthTask.execute();

                    AsyncTasksLogin backgoundAsyncTasks = new AsyncTasksLogin();
                    backgoundAsyncTasks.execute(email,password);
                } else {
                    Toast.makeText(getApplicationContext(), "You must correct the errors to proceed.", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.forgotpassword:

                break;
            default:
                break;
        }
    }

    // validating email pattern
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating length
    private boolean isValidLengthshort(String string) {
        return string.length() >=8;
    }
    private boolean isValidLengthshortPW(String string) {
        return string.length() >=6;
    }
    private boolean isValidLengthlong(String string) {
        return string.length() <=50;
    }
    // validating empty strings
    private boolean isNotEmpty(String string) {
        return string != null && string.length() >0;
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class AsyncTasksLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            // show progress dialog
            //disable submit button and enable progressbar
            Button btn = (Button) findViewById(R.id.signinBTN);
            btn.setEnabled(false);
            btn.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            switch (values[0]) {
                case "200":
                    Toast.makeText(getApplicationContext(), "HTTP Status-Code 200: Connection OK.", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Pls wait...while authenticating...", Toast.LENGTH_LONG).show();
                    break;
                case "11111":
                    Toast.makeText(getApplicationContext(), "Congrats! Authentication was successful.", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Starting Data Upload.....Pls wait...", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }

        }

        @Override
        protected String doInBackground(String... params){
            String email = params[0];
            String password = params[1];
            String apiEndPoint = "http://www.enugusomlapp.com/android/";
            // attempt authentication against a network service.
            // Simulate network access.
            try{
                String requestUrl  = apiEndPoint+"?action=authenticate&" +
                        "username=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");
                URL url = new URL(requestUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                //writer.write(getPostDataString(postDataParams));
                writer.write(requestUrl);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    publishProgress(Integer.toString(responseCode)); //alert onProgressUpdate to display toast
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sbs = new StringBuilder("");
                    String line;
                    while ((line = in.readLine()) != null) {
                        sbs.append(line);
                        break;
                    }
                    in.close();
                    //Log.e("Login Response:",sbs.toString());
                    //return sbs.toString();
                    String loginStatus = sbs.toString();
                    if(loginStatus.equals("11111")){
                        publishProgress(loginStatus); //login success --- alert onProgressUpdate to display toast
                        //return loginStatus;

                    // attempt UPLOADing against a network service.
                        try{
                            //DB Class to perform DB related operations
                            final DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(LoginActivity.this);
                            String requestUrl2 = apiEndPoint + "sync_regmain.php";
                            URL url2 = new URL(requestUrl2);
                            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                            conn2.setUseCaches(false);
                            conn2.setReadTimeout(15000 /* milliseconds */);
                            conn2.setConnectTimeout(15000 /* milliseconds */);
                            conn2.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                            conn2.setRequestMethod("POST");
                            conn2.setDoInput(true);
                            conn2.setDoOutput(true);

                            OutputStream os2 = conn2.getOutputStream();
                            BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(os2, "UTF-8"));
                            writer2.write(androidOpenDbHelperObj.composeJSONfromRegMain());
                            writer2.flush();
                            writer2.close();
                            os2.close();

                            int responseCode2 = conn2.getResponseCode();
                            if(responseCode2 == HttpURLConnection.HTTP_OK) {
                                BufferedReader in2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                                StringBuilder sbs2 = new StringBuilder("");
                                String line2;
                                while ((line2 = in2.readLine()) != null) {
                                    sbs2.append(line2);
                                    break;
                                }
                                in2.close();
                                conn.disconnect();
                                conn2.disconnect();
                                Log.e("Spacebar:", sbs2.toString());
                                //return sbs2.toString();

                                            if(sbs2.toString().length() <5){
                                                return "1"; //php server returned empty json, possibly duplicate
                                            }else{
                                                    //try{
                                                        //update sync status of main table for each uploaded rows
                                                        JSONArray arr = new JSONArray(sbs2.toString());
                                                        System.out.println(arr.length());
                                                        Log.e("Server Res json:", arr.toString());
                                                        for(int i=0; i<arr.length();i++){
                                                            JSONObject obj = (JSONObject)arr.get(i);
                                                            String pcode = obj.get("patientcode").toString();
                                                            String syncstatus = obj.get("syncStatus").toString();
                                                            System.out.println(pcode);
                                                            System.out.println(syncstatus);
                                                            androidOpenDbHelperObj.updateSyncStatusRegMain(pcode,syncstatus);
                                                            uploadchildrentbls(apiEndPoint, pcode);
                                                            uploadvisitstbls(apiEndPoint, pcode);

                                                            //send sms
                                                            String smsstatus = androidOpenDbHelperObj.sendsms(pcode,apiEndPoint);
                                                            //String smsstatus = sendsms(apiEndPoint, pcode, phone4sms);
                                                            //System.out.println("phone4sms:"+phone4sms);
                                                            System.out.println("smmstatus:"+smsstatus);
                                                        }

                                                androidOpenDbHelperObj.close();
                                                //}catch (JSONException e) {
                                                    //    return "Exception sync: " + e.getMessage();
                                                        //e.printStackTrace();
                                                    //}
                                            System.out.println("Everything Successful");
                                            return "1979";
                                            }
                                }else{
                                return Integer.toString(responseCode2);
                                }
                        } catch (Exception e) {
                            return "Exception up: " + e.getMessage();
                        }

                    }else{
                        return loginStatus;
                    }
                }else{
                    return Integer.toString(responseCode);
                }
            }catch(Exception e){
                return "Exception nwk: " + e.getMessage();
            }

        //return "Error: Something went wrong. Try again.";
        }



        //send sms
      /*  private String sendsms(String apiEndPoint, String pcode, String phone4sms){

            //send sms
            try{
                String requestUrl5 = apiEndPoint +
                        "smsengine.php?action=sendmessage&" +
                        "patientcode=" + URLEncoder.encode(pcode, "UTF-8") +
                        "&phone=" + URLEncoder.encode(phone4sms, "UTF-8");

                URL url = new URL(requestUrl5);
                HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                int responseCode5 = uc.getResponseCode();
                if(responseCode5 == HttpURLConnection.HTTP_OK){
                    System.out.println(responseCode5);
                    System.out.println("sms successful");
                    uc.disconnect();
                    return "sent";
                }else{
                    System.out.println(responseCode5);
                    System.out.println("sms unsuccessful");
                    uc.disconnect();
                    return "smsFailed";
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        return "Error while sending sms";
        }*/

        //upload children and visits tables
        String uploadchildrentbls(String apiEndPoint, String pcode){
            String requestUrl3 = apiEndPoint + "sync_regchildren.php";
           try{
               //DB Class to perform DB related operations
               final DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(LoginActivity.this);
               URL url3 = new URL(requestUrl3);
               HttpURLConnection conn3 = (HttpURLConnection) url3.openConnection();
               conn3.setUseCaches(false);
               conn3.setReadTimeout(15000 /* milliseconds */);
               conn3.setConnectTimeout(15000 /* milliseconds */);
               conn3.setRequestProperty("Content-Type", "application/json;charset=utf-8");
               conn3.setRequestMethod("POST");
               conn3.setDoInput(true);
               conn3.setDoOutput(true);

               //use the codes to upload child and visits table rows
               OutputStream os3 = conn3.getOutputStream();
               BufferedWriter writer3 = new BufferedWriter(new OutputStreamWriter(os3, "UTF-8"));
               String JSONfromChild =  androidOpenDbHelperObj.composeJSONfromChild(pcode);
               writer3.write(JSONfromChild);
               writer3.flush();
               writer3.close();
               os3.close();
               int responseCode3 = conn3.getResponseCode();
               if(responseCode3 == HttpURLConnection.HTTP_OK){
                   conn3.disconnect();
                   androidOpenDbHelperObj.close();
                   return "1979";
               }
               conn3.disconnect();
               androidOpenDbHelperObj.close();
           }catch (Exception e){
               return "Exception ch: " + e.getMessage();
           }

        return "Error uploading children and visits tbls";
        }

        //upload visits tables
        String uploadvisitstbls(String apiEndPoint, String pcode){
           String requestUrl4 = apiEndPoint + "sync_regvisits.php";
           try{
               //DB Class to perform DB related operations
               final DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(LoginActivity.this);
               URL url4 = new URL(requestUrl4);
               HttpURLConnection conn4 = (HttpURLConnection) url4.openConnection();
               conn4.setUseCaches(false);
               conn4.setReadTimeout(15000 /* milliseconds */);
               conn4.setConnectTimeout(15000 /* milliseconds */);
               conn4.setRequestProperty("Content-Type", "application/json;charset=utf-8");
               conn4.setRequestMethod("POST");
               conn4.setDoInput(true);
               conn4.setDoOutput(true);

               //use the codes to upload visits table rows
               OutputStream os4 = conn4.getOutputStream();
               BufferedWriter writer4 = new BufferedWriter(new OutputStreamWriter(os4, "UTF-8"));
               String JSONfromVisits =  androidOpenDbHelperObj.composeJSONfromVisits(pcode);
               writer4.write(JSONfromVisits);
               writer4.flush();
               writer4.close();
               os4.close();
               int responseCode4 = conn4.getResponseCode();
               if(responseCode4 == HttpURLConnection.HTTP_OK) {
                   conn4.disconnect();
                   androidOpenDbHelperObj.close();
                   return "1979";
               }
               conn4.disconnect();
               androidOpenDbHelperObj.close();
           }catch (Exception e){
               return "Exception vi: " + e.getMessage();
           }

               return "Error uploading children and visits tbls";
        }


        @Override
        protected void onPostExecute(final String responseCode){
            switch(responseCode){
                case "00000":
                    Toast.makeText(getApplicationContext(), "Login Failed: Wrong email or password.", Toast.LENGTH_LONG).show();
                    //connection was OK and upload was successful
                    break;
                case "10001":
                    Toast.makeText(getApplicationContext(), "Login Failed due to internal server error.", Toast.LENGTH_LONG).show();
                    //connection was OK and upload was successful
                    break;
                case "1":
                    Toast.makeText(getApplicationContext(), "Error: Possibly duplicate data. Pls try again later.", Toast.LENGTH_LONG).show();
                    //connection was OK and upload was successful
                    break;
                case "1979":
                    //connection was OK and upload was successful
                    Toast.makeText(getApplicationContext(), "Congrats! Data was Uploaded to Central Server Successfully.", Toast.LENGTH_LONG).show();
                    Intent gotoMainpage = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(gotoMainpage);
                    break;
                case "1986":
                    Toast.makeText(getApplicationContext(), "Data Upload Failed: Pls try again.", Toast.LENGTH_LONG).show();
                    break;
                case "1900":
                    Toast.makeText(getApplicationContext(), "OOPs! Something went wrong. Server/Network Problems.", Toast.LENGTH_LONG).show();
                    break;
                case "1911":
                    Toast.makeText(getApplicationContext(), "OOPsy! Error while trying to authenticate. Pls try again.", Toast.LENGTH_LONG).show();
                    break;
                case "" + HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                    Toast.makeText(getApplicationContext(), "Error: HTTP Status-Code 408: Request Time-Out.", Toast.LENGTH_LONG).show();
                    break;
                case "" + HttpURLConnection.HTTP_NOT_FOUND:
                    Toast.makeText(getApplicationContext(), "Error: HTTP Status-Code 404: Not Found.", Toast.LENGTH_LONG).show();
                    break;
                case "" + HttpURLConnection.HTTP_BAD_GATEWAY:
                    Toast.makeText(getApplicationContext(), "Error: HTTP Status-Code 502: Bad Gateway.", Toast.LENGTH_LONG).show();
                    break;
                case "" + HttpURLConnection.HTTP_INTERNAL_ERROR:
                    Toast.makeText(getApplicationContext(), "Error: HTTP Status-Code 500: Internal Server Error.", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), responseCode, Toast.LENGTH_LONG).show();
                    break;
            }

            //enable submit button and disable progressbar
            mProgressView.setVisibility(View.GONE);
            Button btn = (Button) findViewById(R.id.signinBTN);
            btn.setEnabled(false);
            btn.setVisibility(View.GONE);
            finish();
        }

    }








}