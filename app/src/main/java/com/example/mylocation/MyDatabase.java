package com.example.mylocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {

    static String DATABASE = "location_db";
    static  String TBL_LOC = "tbl_location";
    Context context;

    public MyDatabase(Context context){
        super(context, DATABASE, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TBL_LOC + " (id integer primary key autoincrement, loc_name varchar(50), latitude decimal(5,11),  longitude decimal(5,11) ) ";
        sqLiteDatabase.execSQL(sql);
    }

    public ArrayList<MyLocation> getAllContact(){
        ArrayList<MyLocation> list = new ArrayList<MyLocation>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TBL_LOC, null,null,null,null,null,"loc_name");

        c.moveToFirst();
        while(!c.isAfterLast()){
            int id = c.getInt(c.getColumnIndex("id"));
            String locName = c.getString(c.getColumnIndex("loc_name"));
            double lat = c.getDouble(c.getColumnIndex("latitude"));
            double lon = c.getDouble(c.getColumnIndex("longitude"));
            list.add(new MyLocation(id, locName, lat, lon));

            c.moveToNext();
        }

        db.close(); // close database connection
        return list;
    }

    public long addContact(MyLocation loc) {
        long result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("loc_name", loc.getN() );
        cv.put("latitude", loc.getLa() );
        cv.put("longitude", loc.getLo() );
        result = db.insert(TBL_LOC, null, cv);

        db.close();// close database connection

        return result;
    }

    public int deleteContact(int id){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        result = db.delete(TBL_LOC, "id=?", new String[]{id+""});
        db.close();
        return result;
    }

    public long updateContact(MyLocation loc, int locID) {
        long result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("loc_name", loc.getN() );
        cv.put("latitude", loc.getLa() );
        cv.put("longitude", loc.getLo() );

        result = db.update(TBL_LOC, cv,"id="+locID, null);

        db.close();// close database connection

        return result;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}