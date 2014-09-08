package org.rix1.phishilight2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rikard Eide on 21/08/14.
 * Description:
 */
public class DBhelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "phishing.db";
    private static final int SCHEMA_VERSION = 1;

    public DBhelper(Context context){
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ip_address (_id INTEGER PRIMARY KEY AUTOINCREMENT, ip TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public Cursor getAll() {
        return(getReadableDatabase().rawQuery("SELECT _id, ip FROM ip_address ORDER BY ip", null));
    }

    //Put IP address into a ContentValues and tell SQLiteDatabase to insert
    //it into the database
    public void insert(String ip) {
        ContentValues contV = new ContentValues();

        contV.put("ip", ip);

        getWritableDatabase().insert("ip_address", "ip", contV);
    }

    //Get IP address out of the cursor
    public String getIP(Cursor c){
        return(c.getString(1));
    }
}

