package com.example.weatherapp;

public class Address {
    String name;
    double lat;
    double lon;
    public Address(String _name, double _lat, double _lon){
        name = _name;
        lat = _lat;
        lon = _lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setName(String name) {
        this.name = name;
    }
}
