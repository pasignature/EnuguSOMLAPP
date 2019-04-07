package com.enugusomlapp.enugusomlapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

class AsycTsVisitsFormUpdate2 extends AsyncTask<String, String, ContentValues> {
    private Context ctx;
    AsycTsVisitsFormUpdate2(Context ctx) {
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
    protected ContentValues doInBackground(String... params) {
        //publishProgress("Saving Patient Data..."); // Calls onProgressUpdate()
        String method = params[0];
        if(method.equals("update_visitdata")){
            // 1. download data from the server
            String historyID = params[1];

            ContentValues CnVs = new ContentValues();
            CnVs.put("historyID",historyID);
            CnVs.put("health_facility",params[2]);
            CnVs.put("facility_street",params[3]);
            CnVs.put("citystate",params[4]);
            CnVs.put("facility_tel",params[5]);
            CnVs.put("visitreason",params[6]);
            CnVs.put("consultantdoctor",params[7]);
            CnVs.put("collected_items",params[8]);
            CnVs.put("comments",params[9]);
            CnVs.put("next_appointment",params[10]);
            CnVs.put("visitdatetime",params[11]);

            // 2. First we have to open our DbHelper class by creating a new object of that
            DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(ctx);
            // Then we need to get a writable SQLite database, because we are going to insert some values
            // SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
            // 3. get reference to writable DB
            SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();
            int qryStatus = SQliteDB.update("registration_visithistory", CnVs, "historyID = ?", new String[]{historyID});
            if(qryStatus>0){
                return CnVs;
            }else{
                return null;
            }
        }

        return null;
    }


    @Override
    protected void onPostExecute(ContentValues result){
        //super.onPostExecute(result);
        if(result != null){
            // extract data from cursor
                String   historyID = result.get("historyID").toString();
                String   health_facility = result.get("health_facility").toString();
                String   facility_street = result.get("facility_street").toString();
                String   citystate = result.get("citystate").toString();
                String   facility_tel = result.get("facility_tel").toString();
                String   visitreason = result.get("visitreason").toString();
                String   consultantdoctor = result.get("consultantdoctor").toString();
                String   collected_items = result.get("collected_items").toString();
                String   comments = result.get("comments").toString();
                String   next_appointment = result.get("next_appointment").toString();
                String   visitdatetime = result.get("visitdatetime").toString();

                //concatenate strings
                String profiledata = "Street: "+facility_street+"\n" +
                        "State: "+citystate+"\n" +
                        "Telephone: " + facility_tel + "\n" +
                        "Reason for visit:\n" + visitreason + "\n\n" +
                        "Consultant Doctor: " + consultantdoctor + "\n" +
                        "Items Collected:\n" + collected_items + "\n\n" +
                        "Remarks/Comments:\n" + comments + "\n\n" +
                        "Date of Next Appointment: " + next_appointment + "\n" +
                        "Visitation Date/Time: " + visitdatetime + "\n" +
                        "DB ID: " + historyID + "\n";

                Toast.makeText(ctx.getApplicationContext(), "Congrats, Data was updated successfully.", Toast.LENGTH_LONG).show();

                // 5. redirect to visits success page
                Intent intent = new Intent(ctx, SuccessVisitFormActivity.class);
                intent.putExtra("historyID", historyID);
                intent.putExtra("health_facility", health_facility);
                intent.putExtra("facility_street", facility_street);
                intent.putExtra("citystate", citystate);
                intent.putExtra("profiledata", profiledata);
                ctx.startActivity(intent);
            }else{
                Toast.makeText(ctx.getApplicationContext(), "ERROR: Data Update Failed. Pls try again.", Toast.LENGTH_LONG).show();
            }

        // 6. dismiss dialog if available

    }

}