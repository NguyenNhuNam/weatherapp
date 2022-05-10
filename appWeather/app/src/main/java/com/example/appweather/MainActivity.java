package com.example.appweather;

import android.location.Address;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.squareup.picasso.Picasso;
public class MainActivity extends AppCompatActivity {
    String CITY1;
    TextView VisionT,DewPointT, UVT, humidityT;
    EditText CITY;
    Address address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.valueHumidity).setVisibility(View.GONE);
        findViewById(R.id.valueDewpoint).setVisibility(View.GONE);
        findViewById(R.id.valueUV).setVisibility(View.GONE);
        findViewById(R.id.valueVision).setVisibility(View.GONE);
        GeoLocation geoLocation = new GeoLocation();
        address = geoLocation.getAddress("Ha Noi",getApplicationContext());
        new weatherTask().execute();
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String args[]) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/onecall?lat="+address.getLatitude()+"&lon="+address.getLongitude()+"&exclude=minutely&units=metric&appid=4ee00d17de477a0bf0d5a1193bb90d51");
            if(response == null) {Log.e("response", "không có giá trị");}
            else{
                Log.e("response",response);
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            humidityT = findViewById(R.id.valueHumidity);
            DewPointT = findViewById(R.id.valueDewpoint);
            UVT = findViewById(R.id.valueUV);
            VisionT = findViewById(R.id.valueVision);
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject current = jsonObj.getJSONObject("current");
                JSONArray weather = current.getJSONArray("weather");
                JSONArray hourly = jsonObj.getJSONArray("hourly");
                //Long updatedAt = jsonObj.getLong("dt");
                //String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));*/
                String humidity = current.getString("humidity");
                String dewPoint = current.getString("dew_point");
                String UV = current.getString("uvi");
                String visibility = current.getInt("visibility")/1000 + " km";
                humidityT.setText(humidity);
                DewPointT.setText(dewPoint);
                UVT.setText(UV);
                VisionT.setText(visibility);
                LinearLayoutCompat Daily = findViewById(R.id.daily);
                int dem = 0;
                for(int i = 0; i < hourly.length()/2; i++){
                    /*new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);*/
                    LinearLayoutCompat a = new LinearLayoutCompat(getApplicationContext());
                    a.setOrientation(LinearLayoutCompat.VERTICAL);
                    a.setGravity(Gravity.CENTER);
                    LinearLayoutCompat.LayoutParams linearLayoutCompat = new LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                    linearLayoutCompat.setMargins(20,0,20,0);
                    //thêm textview hiển thị thời gian
                    JSONObject hour = hourly.getJSONObject(i);
                    TextView time = new TextView(getApplicationContext());
                    time.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    Long updatedAt = hour.getLong("dt");
                    String updatedAtText = new SimpleDateFormat(
                            "hh a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                    time.setText(updatedAtText);
                    //thêm textView icon thời tiết cho mỗi giờ
                    /*TextView Icon = new TextView(getApplicationContext());
                    Icon.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    Icon.setText("icon");*/
                    String iconurl = "http://openweathermap.org/img/w/" +
                            hour.getJSONArray("weather").getJSONObject(0).getString("icon") + ".png";
                    Log.e("ảnh"+i,iconurl);
                    //ImageView Icon = new ImageView(getApplicationContext());
                    ImageView Icon = findViewById(R.id.test);
                    Picasso.get().load("http://openweathermap.org/img/wn/10d@2x.png").into(Icon);
                    //thêm textView hiển thị khả năng mưa cho mỗi giờ
                    TextView pop = new TextView(getApplicationContext());
                    pop.setText(hour.getString("pop")+"%");
                    pop.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    //thêm textView hiển thị nhiệt độ theo từng giờ
                    TextView Temp = new TextView(getApplicationContext());
                    Temp.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    Temp.setText(hour.getInt("temp")+"");
                    a.addView(time,linearLayoutCompat);
                    //a.addView(Icon);
                    a.addView(pop);
                    a.addView(Temp);
                    Daily.addView(a);
                }
                findViewById(R.id.valueHumidity).setVisibility(View.VISIBLE);
                findViewById(R.id.valueDewpoint).setVisibility(View.VISIBLE);
                findViewById(R.id.valueUV).setVisibility(View.VISIBLE);
                findViewById(R.id.valueVision).setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                findViewById(R.id.valueHumidity).setVisibility(View.VISIBLE);
            }
        }
    }
}