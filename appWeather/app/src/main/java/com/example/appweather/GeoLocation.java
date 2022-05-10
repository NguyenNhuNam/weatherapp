package com.example.appweather;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

public class GeoLocation {
    public static Address getAddress(final String locationAddress, final Context context){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Address address = null;
        try{
            List addressList = geocoder.getFromLocationName(locationAddress,1);
            if(addressList != null && addressList.size() > 0){
                address = (Address) addressList.get(0);
                Log.e("Tọa độ",address.getLatitude()+" "+address.getLongitude());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return address;
    }
}
