package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.example.weatherapp.main.AppWeather;

public class MainActivity extends AppCompatActivity implements LocationListener{

    String CITY = "Hanoi";
    String API = "b2e0f0cec21b094b6dceec04403dedd8";
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    Double lat, lon;

    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, visibilityTxt;

    TextView[] forecast = new TextView[5];
    TextView[] forecastTemp = new TextView[5];
    ImageView[] forecastIcons = new ImageView[5];
    public static AppWeather app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (AppWeather) getApplication();

        app.dbManager.open();
        List<Address> list = app.dbManager.getAll();
        for(int i = 0; i < list.size(); i++){
            Log.e("Thanh phố" + i, list.get(i).getName() + " " + list.get(i).getLon()+ " " +list.get(i).getLat());
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawLayout);
        findViewById(R.id.imageMenu).setOnClickListener((view) -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        NavigationView navigationView = findViewById(R.id.NavigationView);
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        addressTxt = findViewById(R.id.address);
        //updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        visibilityTxt = findViewById(R.id.visibility);
        IdAssign(forecast, forecastTemp, forecastIcons);

        new weatherTask().execute();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.dbManager.close();
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        Log.e("adasd", lat.toString());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    private void IdAssign(TextView[] forecast,TextView[] forecastTemp,ImageView[] forecastIcons){
        forecast[0]=findViewById(R.id.id_forecastDay1);
        forecast[1]=findViewById(R.id.id_forecastDay2);
        forecast[2]=findViewById(R.id.id_forecastDay3);
        forecast[3]=findViewById(R.id.id_forecastDay4);
        forecast[4]=findViewById(R.id.id_forecastDay5);
        forecastTemp[0]=findViewById(R.id.id_forecastTemp1);
        forecastTemp[1]=findViewById(R.id.id_forecastTemp2);
        forecastTemp[2]=findViewById(R.id.id_forecastTemp3);
        forecastTemp[3]=findViewById(R.id.id_forecastTemp4);
        forecastTemp[4]=findViewById(R.id.id_forecastTemp5);
        forecastIcons[0]=findViewById(R.id.id_forecastIcon1);
        forecastIcons[1]=findViewById(R.id.id_forecastIcon2);
        forecastIcons[2]=findViewById(R.id.id_forecastIcon3);
        forecastIcons[3]=findViewById(R.id.id_forecastIcon4);
        forecastIcons[4]=findViewById(R.id.id_forecastIcon5);

    }
    private void setDataImage(final ImageView ImageView, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (value){
                    case "01d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w01d)); break;
                    case "01n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w01d)); break;
                    case "02d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w02d)); break;
                    case "02n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w02d)); break;
                    case "03d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w03d)); break;
                    case "03n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w03d)); break;
                    case "04d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w04d)); break;
                    case "04n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w04d)); break;
                    case "09d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w09d)); break;
                    case "09n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w09d)); break;
                    case "10d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w10d)); break;
                    case "10n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w10d)); break;
                    case "11d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w11d)); break;
                    case "11n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w11d)); break;
                    case "13d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w13d)); break;
                    case "13n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w13d)); break;
                    default:ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w03d));

                }
            }
        });
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/onecall?lat=" + 21.0245 + "&lon=" + 105.8412 + "&units=metric&appid=" + API);
            if(response == null) {Log.e("response", "không có giá trị");}
            else{
                Log.e("response",response);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject current = jsonObj.getJSONObject("current");

//                JSONArray hourly = jsonObj.getJSONArray("hourly");
                JSONArray daily = jsonObj.getJSONArray("daily");
                JSONObject tempp = daily.getJSONObject(0).getJSONObject("temp");
                JSONObject weather = daily.getJSONObject(0).getJSONArray("weather").getJSONObject(0);
                //Long updatedAt = jsonObj.getLong("dt");
                //String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));*/
                String humidity = current.getString("humidity");
                String dewPoint = current.getString("dew_point");
                String UV = current.getString("uvi");
                String visibility = current.getInt("visibility") / 1000 + "km";

//                Long updatedAt = jsonObj.getLong("dt");
//                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = current.getString("temp") + "°C";
                String tempMin = "Min Temp: " + tempp.getString("min") + "°C";
                String tempMax = "Max Temp: " + tempp.getString("max") + "°C";
                String pressure = current.getString("pressure");

                Long sunrise = current.getLong("sunrise");
                Long sunset = current.getLong("sunset");
                String windSpeed = current.getString("wind_speed");
                String weatherDescription = weather.getString("description");

//                String address = jsonObj.getString("name") + ", " + .getString("country");


//                addressTxt.setText(address);
//                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxTxt.setText(tempMax);
                sunriseTxt.setText(new SimpleDateFormat("hh:mm a").format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("hh:mm a").format(new Date(sunset * 1000)));
                windTxt.setText(windSpeed);
                pressureTxt.setText(pressure);
                humidityTxt.setText(humidity);
                visibilityTxt.setText(visibility);

                for (int i = 0; i < 5; i++) {
                    Long dt_text = daily.getJSONObject(i+1).getLong("dt");
                    String dt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date(dt_text * 1000));
                    String[] dateSplit = dt.split("-");
                    Calendar calendar=new GregorianCalendar(Integer.parseInt(dateSplit[0]),Integer.parseInt(dateSplit[1])-1,Integer.parseInt(dateSplit[2]));
                    Date forecastDate=calendar.getTime();
                    String dateString=forecastDate.toString();
                    String[] forecastDateSplit=dateString.split(" ");
                    String date=forecastDateSplit[0]+", "+forecastDateSplit[1] +" "+forecastDateSplit[2];
                    forecast[i].setText(date);

                    JSONObject tem = daily.getJSONObject(i+1).getJSONObject("temp");
                    String temm = tem.getString("min") + "° - " + tem.getString("max") + "°";
                    forecastTemp[i].setText(temm);

                    JSONObject weatherr = daily.getJSONObject(i+1).getJSONArray("weather").getJSONObject(0);
                    String icon = weatherr.getString("icon");
                    setDataImage(forecastIcons[i], icon);
                }

                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }

}