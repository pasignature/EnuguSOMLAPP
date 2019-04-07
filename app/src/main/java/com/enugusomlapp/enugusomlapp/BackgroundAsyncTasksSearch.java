package com.enugusomlapp.enugusomlapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

class BackgroundAsyncTasksSearch extends AsyncTask<String, String, Cursor> {
    private Context ctx;
    BackgroundAsyncTasksSearch(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        // show progress dialog
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Cursor doInBackground(String... params) {
        publishProgress("Saving Patient Data..."); // Calls onProgressUpdate()
        // 1. download data from the server
        String method = params[0];
        String pcode = params[1];

        // 2. First we have to open our DbHelper class by creating a new object of that
        DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(ctx);

        if(method.equals("fetchdata")){
            // Then we need to get a writable SQLite database, because we are going to insert some values
            // SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
            // 3. get reference to writable DB
            SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();

            // //Query db and rturn cursor
            Cursor cursor;
            String checkQuery = "SELECT * FROM registration_main WHERE patientcode='"+pcode+"' LIMIT 1";
            cursor= SQliteDB.rawQuery(checkQuery,null);
            if(cursor.moveToFirst()){
                return cursor;
            }else{
                return cursor;
            }
        }

        return null;
    }


    @Override
    protected void onPostExecute(Cursor result){
        if(result != null && result.moveToFirst()){
            // extract data from cursor
            String   patientid = result.getString(result.getColumnIndex("id"));
            String   patientcode = result.getString(result.getColumnIndex("patientcode"));
            String   sname = result.getString(result.getColumnIndex("sname"));
            String   fname = result.getString(result.getColumnIndex("fname"));
            String   onames = result.getString(result.getColumnIndex("onames"));
            String   dob = result.getString(result.getColumnIndex("dob"));
            String   sex = result.getString(result.getColumnIndex("sex"));
            String   phone = result.getString(result.getColumnIndex("phone"));
            String   maritalstatus = result.getString(result.getColumnIndex("maritalstatus"));
            String   occupation = result.getString(result.getColumnIndex("occupation"));
            String   bloodgroup = result.getString(result.getColumnIndex("bloodgroup"));
            String   genotype = result.getString(result.getColumnIndex("genotype"));
            String   HIVstatus = result.getString(result.getColumnIndex("HIVstatus"));
            String   anyailment = result.getString(result.getColumnIndex("anyailment"));
            String   firstpregnancy = result.getString(result.getColumnIndex("firstpregnancy"));
            String   noofchildren = result.getString(result.getColumnIndex("noofchildren"));
            String   normalbirths = result.getString(result.getColumnIndex("normalbirths"));
            String   cesareansections = result.getString(result.getColumnIndex("cesareansections"));
            String   tbattendances = result.getString(result.getColumnIndex("tbattendances"));
            String   tba_contact = result.getString(result.getColumnIndex("tba_contact"));
            String   anyfamplanning = result.getString(result.getColumnIndex("anyfamplanning"));
            String   needfamplanning = result.getString(result.getColumnIndex("needfamplanning"));
            String   nok_fullname = result.getString(result.getColumnIndex("nok_fullname"));
            String   nok_phone = result.getString(result.getColumnIndex("nok_phone"));
            String   nok_relationship = result.getString(result.getColumnIndex("nok_relationship"));
            String   createdby = result.getString(result.getColumnIndex("createdby"));
            String   datetime = result.getString(result.getColumnIndex("datetime"));
            String   lastupdate = result.getString(result.getColumnIndex("lastupdate"));
            String   lastupdateby = result.getString(result.getColumnIndex("lastupdateby"));
            String   syncStatus = result.getString(result.getColumnIndex("syncStatus"));

            //concatenate strings
            String fullnames = sname+", "+fname+" "+onames;
            String profiledata = "Gender: "+sex+"\n" +
                    "Date of birth: "+dob+"\n" +
                    "Phone: "+phone+"\n" +
                    "Marital Status: "+maritalstatus+"\n" +
                    "Occupation: "+occupation+"\n" +
                    "Blood Group: "+bloodgroup+"\n" +
                    "GenoType: " + genotype + "\n" +
                    "HIV Status: " + HIVstatus + "\n" +
                    "Initial Ailment: " + anyailment + "\n" +
                    "First Pregnancy?: " + firstpregnancy + "\n" +
                    "No. of Children: " + noofchildren + "\n" +
                    "Normal Births: " + normalbirths + "\n" +
                    "Cesarean Sections: " + cesareansections + "\n" +
                    "Traditional Births: " + tbattendances + "\n" +
                    "Traditional Birth Attendant Contact:\n" +
                    tba_contact + "\n" +
                    "Any Family Planning?: " + anyfamplanning + "\n" +
                    "Need Family Planning?: " + needfamplanning + "\n" +
                    "Next-of-kin Fullname: " + nok_fullname + "\n" +
                    "Next-of-kin Phone: " + nok_phone + "\n" +
                    "Next-of-kin Relationship: " + nok_relationship + "\n" +
                    "Created By: " + createdby + "\n" +
                    "Date / Time: " + datetime + "\n" +
                    "Last Update: " + lastupdate + "\n" +
                    "Last Updateby: " + lastupdateby + "\n" +
                    "Status: " + syncStatus + "\n" +
                    "DB ID: " + patientid + "\n";

            Toast.makeText(ctx.getApplicationContext(), "Congrats, Patientcode"+patientcode+" was FOUND.", Toast.LENGTH_LONG).show();

            // 5. redirect to success page i.e. profile page
            Intent intent = new Intent(ctx, SuccessSearchActivity.class);
            intent.putExtra("sex", sex);
            intent.putExtra("patientcode", patientcode);
            intent.putExtra("fullnames", fullnames);
            intent.putExtra("profiledata", profiledata);
            ctx.startActivity(intent);
            // 4. close
            result.close();

        }else{
            Toast.makeText(ctx.getApplicationContext(), "Patient NOT FOUND. Pls check the patientcode and try again.",Toast.LENGTH_LONG).show();
        }


        // 6. dismiss dialog if available

    }

}