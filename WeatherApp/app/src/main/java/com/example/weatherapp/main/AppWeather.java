package com.example.weatherapp.main;

import android.app.Application;
import android.util.Log;

import com.example.weatherapp.Database.DBManager;

public class AppWeather extends Application {
    public DBManager dbManager;
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("WeatherApp", "Weather App Started");
        dbManager = new DBManager(this);
        Log.v("Address", "Database Created");

    }

}
