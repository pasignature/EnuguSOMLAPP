package com.enugusomlapp.enugusomlapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;


class AsyncTasksDelete extends AsyncTask<String, String, String> {
    private Context ctx;
    AsyncTasksDelete(Context ctx) {
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
        String pcode = params[1];

        // 2. First we have to open our DbHelper class by creating a new object of that
        DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(ctx);
        // Then we need to get a writable SQLite database, because we are going to insert some values
        // SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
        // 3. get reference to writable DB
        SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();

        if(method.equals("delete_MainForm_data")){
            SQliteDB.beginTransactionNonExclusive();
            //Query db and delete
            int qryStatus1 = SQliteDB.delete("registration_main", "patientcode = ?", new String[]{pcode});
            int qryStatus2 = SQliteDB.delete("registration_children", "patientcode = ?", new String[]{pcode});
            int qryStatus3 = SQliteDB.delete("registration_visithistory", "patientcode = ?", new String[]{pcode});
            // //Query db and return cursor
           /* Cursor cursor;
            String checkQuery = "SELECT * FROM registration_main WHERE patientcode='"+pcode+"' LIMIT 1";
            cursor= SQliteDB.rawQuery(checkQuery,null);*/
            if(qryStatus1 >0 && qryStatus2 >0 && qryStatus3 >0){
                SQliteDB.setTransactionSuccessful();
                SQliteDB.endTransaction();    // 4. close
                SQliteDB.close();
                return "deleted";
            }else{
                SQliteDB.endTransaction();
                SQliteDB.close();
                return "notdeleted";
            }
        }

        if(method.equals("delete_Child_Formdata")){
            //Query db and delete
            int qryStatus = SQliteDB.delete("registration_children", "childID = ?", new String[]{pcode});

            // //Query db and return cursor
            Cursor cursor;
            String checkQuery = "SELECT * FROM registration_children WHERE childID='"+pcode+"' LIMIT 1";
            cursor= SQliteDB.rawQuery(checkQuery,null);
            if(cursor.moveToFirst()){
                cursor.close();    // 4. close
                SQliteDB.close();
                return "notdeleted";
            }else{
                cursor.close();    // 4. close
                SQliteDB.close();
                return "deleted";
            }
        }

        if(method.equals("delete_VISIT_data")){
            //Query db and delete
            int qryStatus = SQliteDB.delete("registration_visithistory", "historyID = ?", new String[]{pcode});

            // //Query db and return cursor
            Cursor cursor;
            String checkQuery = "SELECT * FROM registration_visithistory WHERE historyID='"+pcode+"' LIMIT 1";
            cursor= SQliteDB.rawQuery(checkQuery,null);
            if(cursor.moveToFirst()){
                cursor.close();    // 4. close
                SQliteDB.close();
                return "notdeleted";
            }else{
                cursor.close();    // 4. close
                SQliteDB.close();
                return "deleted";
            }
        }



        return null;
    }


    @Override
    protected void onPostExecute(String result){
        if(result != null && result.equalsIgnoreCase("deleted")){
            Toast.makeText(ctx.getApplicationContext(), "Done, record was DELETED successfully.", Toast.LENGTH_LONG).show();

            // 5. redirect to success page
            Intent intent = new Intent(ctx, SearchActivity.class);
            ctx.startActivity(intent);
        }else{
            Toast.makeText(ctx.getApplicationContext(), "Delete Failed. Pls try again.", Toast.LENGTH_LONG).show();
        }
    }

}