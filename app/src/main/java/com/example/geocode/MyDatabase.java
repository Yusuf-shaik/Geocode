package com.example.geocode;

import java.net.ConnectException;
import java.util.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//database class
public class MyDatabase extends SQLiteOpenHelper {
    public static final String name = "GEO";
    public static final String table_name = "GeoLocation";
    public static final String column_id = "id";
    public static final String column_address = "address";
    public static final String column_latitude = "latitude";
    public static final String column_longitude = "longitude";


    public MyDatabase(@Nullable Context context){
        super(context, name, null, 1);
    }

    //create database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + table_name+ "(id integer primary key, address text, latitude text, longitude text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }

    //insert new record into the database
    @SuppressLint("NewApi")
    public boolean insertAddress(String address, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_address, address);
        contentValues.put(column_latitude, latitude);
        contentValues.put(column_longitude, longitude);
        long inserted = db.insert(table_name, null, contentValues);
        return inserted > 0;

    }
    //get all information in database
    public Cursor getRecords(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + table_name;

        Cursor cursor = null;
        if(db != null ){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    //search database based on a specific address. Uses wildcard to allow any keyword search within the address string
    public Cursor search(String searchable){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + table_name + " where address like '%" + searchable +"%'";


        Cursor cursor = null;
        if(db != null ){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    //update the record in the database
    @SuppressLint("NewApi")
    public boolean updateAddress(Integer id, String address, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_address, address);
        contentValues.put(column_latitude, latitude);
        contentValues.put(column_longitude, longitude);
        long result = db.update(table_name, contentValues, "id =?", new String[] {Integer.toString(id)});
        return result > 0;
    }

    //delete entire record in database
    public Integer deleteRecord(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_name, "id = ? ", new String[] {Integer.toString(id)});
    }

}
