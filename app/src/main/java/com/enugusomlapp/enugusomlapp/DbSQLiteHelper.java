package com.enugusomlapp.enugusomlapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

class DbSQLiteHelper extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "enugu_somlappdb";

    DbSQLiteHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //db.execSQL("DROP TABLE IF EXISTS registration_main");
        // 1. SQL statement to create tables
        db.execSQL("CREATE TABLE IF NOT EXISTS registration_main (id INTEGER PRIMARY KEY AUTOINCREMENT, patientcode varchar(11) NOT NULL UNIQUE, sname varchar(20) NOT NULL, fname varchar(20) NOT NULL, onames varchar(20) NOT NULL DEFAULT 'NA', dob varchar(10) NOT NULL, sex varchar(7) NOT NULL, phone varchar(15) NOT NULL UNIQUE, maritalstatus varchar(15) NOT NULL, occupation varchar(30) NOT NULL, bloodgroup varchar(3) NOT NULL DEFAULT 'NA', genotype varchar(3) NOT NULL DEFAULT 'NA', HIVstatus varchar(8) DEFAULT 'unknown', anyailment varchar(30) NOT NULL, firstpregnancy varchar(3) NOT NULL DEFAULT 'NA', noofchildren int(3) NOT NULL DEFAULT 0, normalbirths int(2) NOT NULL DEFAULT 0, cesareansections int(2) NOT NULL DEFAULT 0, tbattendances int(2) NOT NULL DEFAULT 0, tba_contact varchar(200) NOT NULL DEFAULT 'NA', anyfamplanning varchar(3) NOT NULL DEFAULT 'NA', needfamplanning varchar(3) NOT NULL DEFAULT 'NA', nok_fullname varchar(60) NOT NULL DEFAULT 'NA', nok_phone varchar(15) NOT NULL DEFAULT 'NA', nok_relationship varchar(20) NOT NULL DEFAULT 'NA', createdby varchar(20) NOT NULL, datetime varchar(22) NOT NULL, lastupdate varchar(22) NOT NULL, lastupdateby varchar(20) NOT NULL, syncStatus varchar(8) NOT NULL DEFAULT 'unsynced', CONSTRAINT uniquefields1 UNIQUE (patientcode, phone))");
        db.execSQL("CREATE TABLE IF NOT EXISTS registration_children (id INTEGER PRIMARY KEY AUTOINCREMENT, childID varchar(20) NOT NULL, regmainFK varchar(11) NOT NULL, fullname varchar(60) NOT NULL, sex varchar(8) NOT NULL, dob varchar(10) NOT NULL, bloodgroup varchar(3) NOT NULL, genotype varchar(3) NOT NULL, HIVstatus varchar(8) NOT NULL, createdby varchar(20) NOT NULL, datetime varchar(22) NOT NULL, lastupdate varchar(22) NOT NULL, lastupdateby varchar(20) NOT NULL, CONSTRAINT uniquefields2 UNIQUE (childID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS registration_visithistory (id INTEGER PRIMARY KEY AUTOINCREMENT, historyID varchar(20) NOT NULL, regmainFK varchar(12) NOT NULL, childFK int(25) NOT NULL, health_facility varchar(50) NOT NULL, facility_street varchar(160) NOT NULL, citystate varchar(50) NOT NULL, facility_tel varchar(12) NOT NULL, visitreason varchar(100) NOT NULL, consultantdoctor varchar(60) NOT NULL, collected_items varchar(255) DEFAULT 'NA', comments varchar(255) DEFAULT 'NA', next_appointment varchar(22) NOT NULL, visitdatetime varchar(22) NOT NULL, CONSTRAINT uniquefields3 UNIQUE (historyID))");
    }

    static void save_patientdata(SQLiteDatabase SQliteDB, String patientcode, String sname, String fname, String onames, String dob, String sex, String phone, String maritalstatus, String occupation, String bloodgroup, String genotype, String HIVstatus, String anyailment, String firstpregnancy, String noofchildren, String normalbirths, String cesareansections, String tbattendances, String tba_contact, String anyfamplanning, String needfamplanning, String nok_fullname, String nok_phone, String nok_relationship, String createdby, String datetime, String lastupdate, String lastupdateby, String syncStatus){
        // 2. create ContentValues to add key // key/value -> keys = column names/ values = column values
        ContentValues values = new ContentValues();
        values.put("patientcode", patientcode);
        values.put("sname", sname);
        values.put("fname", fname);
        values.put("onames", onames);
        values.put("dob", dob);
        values.put("sex", sex);
        values.put("phone", phone);
        values.put("maritalstatus", maritalstatus);
        values.put("occupation", occupation);
        values.put("bloodgroup", bloodgroup);
        values.put("genotype", genotype);
        values.put("HIVstatus", HIVstatus);
        values.put("anyailment", anyailment);
        values.put("firstpregnancy", firstpregnancy);
        values.put("noofchildren", noofchildren);
        values.put("normalbirths", normalbirths);
        values.put("cesareansections", cesareansections);
        values.put("tbattendances", tbattendances);
        values.put("tba_contact", tba_contact);
        values.put("anyfamplanning", anyfamplanning);
        values.put("needfamplanning", needfamplanning);
        values.put("nok_fullname", nok_fullname);
        values.put("nok_phone", nok_phone);
        values.put("nok_relationship", nok_relationship);
        values.put("createdby", createdby);
        values.put("datetime", datetime);
        values.put("lastupdate", lastupdate);
        values.put("lastupdateby", lastupdateby);
        values.put("syncStatus", syncStatus);

        // 3. insert
        SQliteDB.insert("registration_main", null, values);
    }


    static void save_child_data(SQLiteDatabase SQliteDB, String childID,String patientcode,String fullname,String sex,String dob,String bloodgroup,String genotype,String HIVstatus,String createdby,String datetime,String lastupdate,String lastupdateby) {
        // 2. create ContentValues to add key // key/value -> keys = column names/ values = column values
        ContentValues values = new ContentValues();
        values.put("childID", childID);
        values.put("regmainFK", patientcode);
        values.put("fullname", fullname);
        values.put("sex", sex);
        values.put("dob", dob);
        values.put("bloodgroup", bloodgroup);
        values.put("genotype", genotype);
        values.put("HIVstatus", HIVstatus);
        values.put("createdby", createdby);
        values.put("datetime", datetime);
        values.put("lastupdate", lastupdate);
        values.put("lastupdateby", lastupdateby);

        // 3. insert
        SQliteDB.insert("registration_children", null, values);
    }


    static void save_visit_data(SQLiteDatabase SQliteDB, String historyID,String regmainFK,String childFK,String health_facility,String facility_street,String citystate,String facility_tel,String visitreason,String consultantdoctor,String collected_items,String comments,String next_appointment,String visitdatetime){
        // 2. create ContentValues to add key // key/value -> keys = column names/ values = column values
        ContentValues values = new ContentValues();
        values.put("historyID", historyID);
        values.put("regmainFK", regmainFK);
        values.put("childFK", childFK);
        values.put("health_facility", health_facility);
        values.put("facility_street", facility_street);
        values.put("citystate", citystate);
        values.put("facility_tel", facility_tel);
        values.put("visitreason", visitreason);
        values.put("consultantdoctor", consultantdoctor);
        values.put("collected_items", collected_items);
        values.put("comments", comments);
        values.put("next_appointment", next_appointment);
        values.put("visitdatetime", visitdatetime);

        // 3. insert
        SQliteDB.insert("registration_visithistory", null, values);
    }


    /**
     * Get list of data from SQLite DB as Array List for table registration_main
     //* @return
     */
    ArrayList<HashMap<String, String>> getAllRegMain() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM registration_main";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("patientcode", cursor.getString(1));
                map.put("sname", cursor.getString(2));
                map.put("fname", cursor.getString(3));
                map.put("onames", cursor.getString(4));
                map.put("dob", cursor.getString(5));
                map.put("sex", cursor.getString(6));
                map.put("phone", cursor.getString(7));
                map.put("maritalstatus", cursor.getString(8));
                map.put("occupation", cursor.getString(9));
                map.put("bloodgroup", cursor.getString(10));
                map.put("genotype", cursor.getString(11));
                map.put("HIVstatus", cursor.getString(12));
                map.put("anyailment", cursor.getString(13));
                map.put("firstpregnancy", cursor.getString(14));
                map.put("noofchildren", cursor.getString(15));
                map.put("normalbirths", cursor.getString(16));
                map.put("cesareansections", cursor.getString(17));
                map.put("tbattendances", cursor.getString(18));
                map.put("tba_contact", cursor.getString(19));
                map.put("anyfamplanning", cursor.getString(20));
                map.put("needfamplanning", cursor.getString(21));
                map.put("nok_fullname", cursor.getString(22));
                map.put("nok_phone", cursor.getString(23));
                map.put("nok_relationship", cursor.getString(24));
                map.put("createdby", cursor.getString(25));
                map.put("datetime", cursor.getString(26));
                map.put("lastupdate", cursor.getString(27));
                map.put("lastupdateby", cursor.getString(28));
                map.put("syncStatus", cursor.getString(29));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return wordList;
    }


    /**
     * Compose JSON out of SQLite records for registration_main
     //* @return
     */
    String composeJSONfromRegMain(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM registration_main where syncStatus = 'unsynced'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("patientcode", cursor.getString(1));
                map.put("sname", cursor.getString(2));
                map.put("fname", cursor.getString(3));
                map.put("onames", cursor.getString(4));
                map.put("dob", cursor.getString(5));
                map.put("sex", cursor.getString(6));
                map.put("phone", cursor.getString(7));
                map.put("maritalstatus", cursor.getString(8));
                map.put("occupation", cursor.getString(9));
                map.put("bloodgroup", cursor.getString(10));
                map.put("genotype", cursor.getString(11));
                map.put("HIVstatus", cursor.getString(12));
                map.put("anyailment", cursor.getString(13));
                map.put("firstpregnancy", cursor.getString(14));
                map.put("noofchildren", cursor.getString(15));
                map.put("normalbirths", cursor.getString(16));
                map.put("cesareansections", cursor.getString(17));
                map.put("tbattendances", cursor.getString(18));
                map.put("tba_contact", cursor.getString(19));
                map.put("anyfamplanning", cursor.getString(20));
                map.put("needfamplanning", cursor.getString(21));
                map.put("nok_fullname", cursor.getString(22));
                map.put("nok_phone", cursor.getString(23));
                map.put("nok_relationship", cursor.getString(24));
                map.put("createdby", cursor.getString(25));
                map.put("datetime", cursor.getString(26));
                map.put("lastupdate", cursor.getString(27));
                map.put("lastupdateby", cursor.getString(28));
                map.put("syncStatus", cursor.getString(29));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }


    /**
     * Compose JSON out of SQLite records for children table
     //* @return
     */
    String composeJSONfromChild(String patientcode){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM registration_children where regmainFK = '"+patientcode+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("childID", cursor.getString(1));
                map.put("regmainFK", cursor.getString(2));
                map.put("fullname", cursor.getString(3));
                map.put("sex", cursor.getString(4));
                map.put("dob", cursor.getString(5));
                map.put("bloodgroup", cursor.getString(6));
                map.put("genotype", cursor.getString(7));
                map.put("HIVstatus", cursor.getString(8));
                map.put("createdby", cursor.getString(9));
                map.put("datetime", cursor.getString(10));
                map.put("lastupdate", cursor.getString(11));
                map.put("lastupdateby", cursor.getString(12));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    /**
     * Compose JSON out of SQLite records for visits table
     //* @return
     */
    String composeJSONfromVisits(String patientcode){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM registration_visithistory where regmainFK = '"+patientcode+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("historyID", cursor.getString(1));
                map.put("regmainFK", cursor.getString(2));
                map.put("childFK", cursor.getString(3));
                map.put("health_facility", cursor.getString(4));
                map.put("facility_street", cursor.getString(5));
                map.put("citystate", cursor.getString(6));
                map.put("facility_tel", cursor.getString(7));
                map.put("visitreason", cursor.getString(8));
                map.put("consultantdoctor", cursor.getString(9));
                map.put("collected_items", cursor.getString(10));
                map.put("comments", cursor.getString(11));
                map.put("next_appointment", cursor.getString(12));
                map.put("visitdatetime", cursor.getString(13));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    /**
     * Get phone and send sms
     //* @return
     */
    String sendsms(String pcode, String apiEndPoint){
        String selectQuery = "SELECT phone FROM registration_main WHERE patientcode = '" + pcode + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
                if(cursor != null && cursor.moveToFirst()){
                    String phone4sms = cursor.getString(cursor.getColumnIndex("phone"));
                    cursor.close();
                    database.close();

                        //send sms
                        try {
                            String requestUrl5 = apiEndPoint +
                                    "smsengine.php?action=sendmessage&" +
                                    "patientcode=" + URLEncoder.encode(pcode, "UTF-8") +
                                    "&phone=" + URLEncoder.encode(phone4sms, "UTF-8");

                            URL url = new URL(requestUrl5);
                            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                            int responseCode5 = uc.getResponseCode();
                            if (responseCode5 == HttpURLConnection.HTTP_OK) {
                                System.out.println(responseCode5);
                                System.out.println("sms successful");
                                uc.disconnect();
                                return "sent";
                            } else {
                                System.out.println(responseCode5);
                                System.out.println("sms unsuccessful");
                                uc.disconnect();
                                return "smsFailed";
                            }

                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }

                }else{
                    database.close();
                    return "Empty phone cursor.";
                }

        return "Error sending sms";
        }

    /**
     * Get Patientcode
     //* @return
     */
    String getPatientcode4sms(String pphone){
        String selectQuery = "SELECT patientcode FROM registration_main where phone = '"+pphone+"' LIMIT 1";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            String Patientcode = cursor.getString(0);
            cursor.close();
            database.close();
            return Patientcode;
        }

    cursor.close();
    database.close();
    return "NOTFOUND";
    }

    /**
     * Get SQLite records that are yet to be Synced
     //* @return
     */
    int dbSyncCountRegMain(){
        int count;
        String selectQuery = "SELECT  * FROM registration_main where syncStatus = 'unsynced'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }

    /**
     * Update Sync status against each User ID
     //* @param serialnumber
     //* @param status
     */
    void updateSyncStatusRegMain(String patientcode, String syncStatus){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues CnVs = new ContentValues();
        CnVs.put("syncStatus",syncStatus);
        database.update("registration_main", CnVs, "patientcode = ?", new String[]{patientcode});
        database.close();
    }


    @Override
    public void onUpgrade (final SQLiteDatabase db, final int oldVersion, final int newVersion){
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS registration_main");
        db.execSQL("DROP TABLE IF EXISTS registration_children");
        db.execSQL("DROP TABLE IF EXISTS registration_visithistory");

        // recreate fresh tables
        this.onCreate(db);
    }

}