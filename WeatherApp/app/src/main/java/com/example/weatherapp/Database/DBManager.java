package com.example.weatherapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.weatherapp.Address;


public class DBManager {

    private SQLiteDatabase database;
    private DBDesigner dbHelper;

    public DBManager(Context context) {
        dbHelper = new DBDesigner(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public void add(Address d) {
        ContentValues values = new ContentValues();
        values.put("name", d.getName());
        values.put("lat", d.getLat());
        values.put("lon",d.getLon());

        database.insert("address", null, values);
    }

    public void delete(String name){
        database.delete("address","name = \""+ name +"\"", null);
    }

    public List<Address> getAll() {
        List<Address> listAddress = new ArrayList<Address>();
        Cursor cursor = database.rawQuery("SELECT * FROM address", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Address d = toAddress(cursor);
            listAddress.add(d);
            cursor.moveToNext();
        }
        cursor.close();
        return listAddress;
    }

    private Address toAddress(Cursor cursor) {
        Address pojo = new Address(cursor.getString(1), cursor.getDouble(2),cursor.getDouble(3));
        return pojo;
    }

    /*public void setTotalDonated(Base base) {
        Cursor c = database.rawQuery("SELECT SUM(amount) FROM donations", null);
        c.moveToFirst();
        if (!c.isAfterLast())
            base.app.totalDonated = c.getInt(0);
    }*/

    public void reset() {
        database.delete("donations", null, null);
    }
}
