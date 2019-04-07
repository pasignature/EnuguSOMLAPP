package com.enugusomlapp.enugusomlapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;


class BackgroundAsyncTasks extends AsyncTask<String, String, String> {
    private Context ctx;
    BackgroundAsyncTasks(Context ctx) {
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
    protected String doInBackground(String... params) {
        publishProgress("Saving Patient Data..."); // Calls onProgressUpdate()
        // 1. download data from the server
        String method = params[0];

        String patientcode = params[1];
        String sname = params[2];
        String fname = params[3];
        String onames = params[4];
        String dob = params[5];
        String sex = params[6];
        String phone = params[7];
        String maritalstatus = params[8];
        String occupation = params[9];
        String bloodgroup = params[10];
        String genotype = params[11];
        String HIVstatus = params[12];
        String anyailment = params[13];
        String firstpregnancy = params[14];
        String noofchildren = params[15];
        String normalbirths = params[16];
        String cesareansections = params[17];
        String tbattendances = params[18];
        String tba_contact = params[19];
        String anyfamplanning = params[20];
        String needfamplanning = params[21];
        String nok_fullname = params[22];
        String nok_phone = params[23];
        String nok_relationship = params[24];
        String createdby = params[25];
        String datetime = params[26];
        String lastupdate = params[27];
        String lastupdateby = params[28];
        String syncStatus = params[29];

        // 2. First we have to open our DbHelper class by creating a new object of that
        DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(ctx);

        if(method.equals("savedata")){
            // Then we need to get a writable SQLite database, because we are going to insert some values
            // SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
            // 3. get reference to writable DB
            SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();

            // Check before adding item if item already exist
            Cursor cursor;
            String checkQuery = "SELECT * FROM registration_main WHERE phone='"+phone+"' LIMIT 1";
            cursor= SQliteDB.rawQuery(checkQuery,null);
            if(cursor.moveToFirst()){
                // record exists, abort data insert
                return "patientexists";
            }else{
                // record does not exist, insert data
                DbSQLiteHelper.save_patientdata(SQliteDB, patientcode,sname,fname,onames,dob,sex,phone,maritalstatus,occupation,bloodgroup,genotype,HIVstatus,anyailment,firstpregnancy,noofchildren,normalbirths,cesareansections,tbattendances,tba_contact,anyfamplanning,needfamplanning,nok_fullname,nok_phone,nok_relationship,createdby,datetime,lastupdate,lastupdateby,syncStatus);
                //return msg after insert succesful

                // Get the newly inserted patientcode
                String getpatientcodeQuery = "SELECT id,patientcode,sname,fname,phone FROM registration_main WHERE phone='"+phone+"' LIMIT 1";
                cursor= SQliteDB.rawQuery(getpatientcodeQuery,null);
                if(cursor.moveToFirst()){
                    String patientcodedb = cursor.getString(cursor.getColumnIndex("patientcode"));
                    // 4. close
                    cursor.close();
                    SQliteDB.close();
                    return patientcodedb;
                }else{
                    // 4. close
                    cursor.close();
                    SQliteDB.close();

                    return "insertfailed";
                }

            }
        }

        if(method.equals("update_mainOPdata")){
            // Then we need to get a writable SQLite database, because we are going to insert some values
            // SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
            // 3. get reference to writable DB
            SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();
            ContentValues CnVs = new ContentValues();
            CnVs.put("sname",sname);
            CnVs.put("fname",fname);
            CnVs.put("onames",onames);
            CnVs.put("dob",dob);
            CnVs.put("sex",sex);
            CnVs.put("phone",phone);
            CnVs.put("maritalstatus",maritalstatus);
            CnVs.put("occupation",occupation);
            CnVs.put("bloodgroup",bloodgroup);
            CnVs.put("genotype",genotype);
            CnVs.put("HIVstatus",HIVstatus);
            CnVs.put("anyailment",anyailment);
            CnVs.put("firstpregnancy",firstpregnancy);
            CnVs.put("noofchildren",noofchildren);
            CnVs.put("normalbirths",normalbirths);
            CnVs.put("cesareansections",cesareansections);
            CnVs.put("tbattendances",tbattendances);
            CnVs.put("tba_contact",tba_contact);
            CnVs.put("anyfamplanning",anyfamplanning);
            CnVs.put("needfamplanning",needfamplanning);
            CnVs.put("nok_fullname",nok_fullname);
            CnVs.put("nok_phone",nok_phone);
            CnVs.put("nok_relationship",nok_relationship);
            CnVs.put("nok_relationship",nok_relationship);
            CnVs.put("lastupdate",lastupdate);
            CnVs.put("lastupdateby",lastupdateby);
            CnVs.put("syncStatus",syncStatus);
            int qryStatus = SQliteDB.update("registration_main", CnVs, "patientcode = ?", new String[]{patientcode});
            if(qryStatus>0){
                //append a string to indicate method used
                return "updateQRY"+patientcode;
            }else if(qryStatus <=0){
                return "insertfailed";
            }else{
                return null;
            }
        }

        return null;
    }


    @Override
    protected void onPostExecute(String result){
        //super.onPostExecute(result);
    if(result != null) {
        switch (result) {
            case "patientexists":
                Toast.makeText(ctx.getApplicationContext(), "A patient with that phone already exists.", Toast.LENGTH_LONG).show();
                break;
            case "insertfailed":
                Toast.makeText(ctx.getApplicationContext(), "Data Save Failed. Try Again.", Toast.LENGTH_LONG).show();
                break;
            default:
                //extract the method
                String method = "savedata2DB"; //default
                String pcode = result; //ES123456
                if (result.length() > 12) { //i.e. if e.g. updateQRYES123456
                    method = result.substring(0, 9); //updateQRY
                    pcode = result.substring(9); //ES123456
                }

                // 1. First we have to open our DbHelper class by creating a new object of that
                DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(ctx);
                // 2. get reference to writable DB
                SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();
                // 3. Get the newly inserted patientcode
                Cursor cursor;
                String getpatientcodeQuery = "SELECT id,patientcode,sname,fname,phone FROM registration_main WHERE patientcode='" + pcode + "' LIMIT 1";
                cursor = SQliteDB.rawQuery(getpatientcodeQuery, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String patientidmsg = cursor.getString(cursor.getColumnIndex("id"));
                    String patientcodemsg = cursor.getString(cursor.getColumnIndex("patientcode"));
                    String snamemsg = cursor.getString(cursor.getColumnIndex("sname"));
                    String fnamemsg = cursor.getString(cursor.getColumnIndex("fname"));
                    String phonemsg = cursor.getString(cursor.getColumnIndex("phone"));
                    // 4. close
                    cursor.close();
                    SQliteDB.close();

                    Toast.makeText(ctx.getApplicationContext(), "Patient Data Saved Successfully.", Toast.LENGTH_LONG).show();

                    // 5. redirect to success page
                    Intent intent = new Intent(ctx, SuccessActivity.class);
                    intent.putExtra("method", method);
                    intent.putExtra("patientid", patientidmsg);
                    intent.putExtra("patientcode", patientcodemsg);
                    intent.putExtra("patientsname", snamemsg);
                    intent.putExtra("patientfname", fnamemsg);
                    intent.putExtra("patientphone", phonemsg);
                    ctx.startActivity(intent);
                } else {
                    // 6. close
                    SQliteDB.close();
                    Toast.makeText(ctx.getApplicationContext(), "Save status not confirmed. Assigned ID:" + result + ".", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }else{
        Toast.makeText(ctx.getApplicationContext(), "An error occurred. Try Again.", Toast.LENGTH_LONG).show();
    }

        // 6. dismiss dialog if available

    }

}