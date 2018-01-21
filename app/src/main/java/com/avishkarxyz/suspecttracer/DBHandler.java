package com.avishkarxyz.suspecttracer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

/**
 * Created by Ishaan on 8/27/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE = "database.db";

    //for table 1
    private static final String TABLE_FRIENDS = "friends";
    private static final String COLUMN_ID = "_id";
    private static final String FRIEND_NAME = "Name";
    private static final String FRIEND_ID = "Android_id";

    //for table 2
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String TIME = "time";



    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query =  "CREATE TABLE IF NOT EXISTS " + TABLE_FRIENDS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FRIEND_NAME +
                " TEXT, " + FRIEND_ID + " TEXT); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //delete other tables to ---------
        int count  =(int) DatabaseUtils.queryNumEntries(db, FRIEND_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        for(int x=1;x<=count;x++)
        {
            db.execSQL("DROP TABLE IF EXISTS " + "friend_" + Integer.toString(x));
        }
        onCreate(db);
    }

    public void AddFriend(FriendContact friend)
    {
        ContentValues values = new ContentValues();
        values.put(FRIEND_NAME, friend.getName());
        values.put(FRIEND_ID,friend.getAndroid_id());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_FRIENDS,null,values);
        long count  = DatabaseUtils.queryNumEntries(db, TABLE_FRIENDS);
        db.close();
        NewTable((int)count);
    }

    public String Name(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + FRIEND_NAME + " FROM " + TABLE_FRIENDS + " WHERE " + COLUMN_ID + " = " + id ;
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        String ans = c.getString(0);
        c.close();
        db.close();
        return ans;
    }

    public void NewTable(int position)
    {
        String query = "CREATE TABLE IF NOT EXISTS friend_"  + position + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LATITUDE +
                " DOUBLE, " +  LONGITUDE + " DOUBLE, " + TIME + " LONG);";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void AddEntry(Location location, int position)
    {
        ContentValues values = new ContentValues();
        values.put(LATITUDE, location.getLatitude());
        values.put(LONGITUDE, location.getLongitude());
        values.put(TIME, location.getTime());
        SQLiteDatabase db = this.getWritableDatabase();
        String Table_name = "friend_" + Integer.toString(position);
        db.insert(Table_name,null,values);
        db.close();
    }

    public Location RetrieveLast(int position)
    {
        String query= "SELECT * "   + " FROM friend_" + position;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query,null);
        c.moveToLast();
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(c.getString(1)));
        location.setLongitude(Double.parseDouble(c.getString(2)));
        location.setTime(Long.parseLong(c.getString(3)));
        c.close();
        db.close();
        return location;
    }

    public int getRowID(String friend)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_FRIENDS + " WHERE " + FRIEND_NAME + " = \"" + friend + "\";";
        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        String ans = c.getString(1);
        c.close();
        return Integer.parseInt(ans);
    }



}
