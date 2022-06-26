package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class QualityAir extends AppCompatActivity {

    String API = "b2e0f0cec21b094b6dceec04403dedd8";
    String token = "b00a263076msh94d2b12735473b1p16ccdfjsnc58bf1895c3e";

    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    TextView txt_progress, PM25, PM10, SO2, NO2, O3, CO, description, caution, status,
            item1, item2, item3, item4, item5, item6;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_air);

        relativeLayout = findViewById(R.id.air);
        progressBar = findViewById(R.id.progress_bar);
        txt_progress = findViewById(R.id.text_view_progress);
        PM25 = findViewById(R.id.PM25);
        PM10 = findViewById(R.id.PM10);
        SO2 = findViewById(R.id.SO2);
        NO2 = findViewById(R.id.NO2);
        O3 = findViewById(R.id.O3);
        CO = findViewById(R.id.CO);
        item1 = findViewById(R.id.it1);
        item2 = findViewById(R.id.it2);
        item3 = findViewById(R.id.it3);
        item4 = findViewById(R.id.it4);
        item5 = findViewById(R.id.it5);
        item6 = findViewById(R.id.it6);
        description = findViewById(R.id.description);
        caution = findViewById(R.id.caution);
        status = findViewById(R.id.status);

        progressBar.setProgress(40);

        new AirTask().execute();
    }

    public void changeColor() {
        PM25.setTextColor(getResources().getColor(R.color.black));
        PM10.setTextColor(getResources().getColor(R.color.black));
        CO.setTextColor(getResources().getColor(R.color.black));
        NO2.setTextColor(getResources().getColor(R.color.black));
        txt_progress.setTextColor(getResources().getColor(R.color.black));
        SO2.setTextColor(getResources().getColor(R.color.black));
        O3.setTextColor(getResources().getColor(R.color.black));
        description.setTextColor(getResources().getColor(R.color.black));
        status.setTextColor(getResources().getColor(R.color.black));
        item1.setTextColor(getResources().getColor(R.color.black));
        item2.setTextColor(getResources().getColor(R.color.black));
        item3.setTextColor(getResources().getColor(R.color.black));
        item4.setTextColor(getResources().getColor(R.color.black));
        item5.setTextColor(getResources().getColor(R.color.black));
        item6.setTextColor(getResources().getColor(R.color.black));
    }

    class AirTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://air-quality.p.rapidapi.com/current/airquality?lon=" + 105.8412 + "&lat=" + 21.0245, token);
            if(response == null) {
                Log.e("response", "không có giá trị");}
            else{
                Log.e("response",response);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray data = jsonObject.getJSONArray("data");
                int intAqi = data.getJSONObject(0).getInt("aqi");
                int pm25 = data.getJSONObject(0).getInt("pm25");
                int pm10 = data.getJSONObject(0).getInt("pm10");
                int co = data.getJSONObject(0).getInt("co");
                int o3 = data.getJSONObject(0).getInt("o3");
                int so2 = data.getJSONObject(0).getInt("so2");
                int no2 = data.getJSONObject(0).getInt("no2");

                progressBar.setProgress(intAqi);
                txt_progress.setText("" + intAqi);
                PM10.setText("" + pm10 + " μg/m3");
                PM25.setText("" + pm25 + " μg/m3");
                CO.setText("" + co + " μg/m3");
                O3.setText("" + o3 + " μg/m3");
                SO2.setText("" + so2 + " μg/m3");
                NO2.setText("" + no2 + " μg/m3");

                if(0 <= intAqi && intAqi <= 50){
                    relativeLayout.setBackgroundResource(R.color.green);
                    status.setText("Tốt");
                    description.setText("Chất lượng không khí được xem là đạt tiêu chuẩn và ô nhiễm không khí coi như không hoặc ít gây hại.");
                } else if( 51 <= intAqi && intAqi <= 100 ) {
                    relativeLayout.setBackgroundResource(R.color.yellow);
                    status.setText("Trung bình");
                    description.setText("Chất lượng không khí ở mức chấp nhận được; tuy nhiên một số chất gây ô nhiễm có thể ảnh hưởng đến sức khỏe của số ít những người nhạy cảm.");
                    changeColor();
                } else if ( 101 <= intAqi && intAqi <= 150 ){
                    relativeLayout.setBackgroundResource(R.color.orange);
                    status.setText("Kém");
                    description.setText("Nhóm người nhạy cảm có thể bị ảnh đến sức khỏe.Đa số mọi người ít bị ảnh hưởng đến sức khỏe");
//                    changeColor();
                } else if ( 151 <= intAqi && intAqi <=200 ){
                    relativeLayout.setBackgroundResource(R.color.red);
                    status.setText("Xấu");
                    description.setText("Mọi người đều có thể bị ảnh hưởng đến sức khỏe; nhóm người nhạy cảm có thể bị ảnh hưởng nghiêm trọng hơn.");
                } else if ( 201 <= intAqi && intAqi <= 300){
                    relativeLayout.setBackgroundResource(R.color.purple);
                    status.setText("Rất xấu");
                    description.setText("Cảnh báo nguy hại đến sức khỏe nghiêm trọng. Đa số mọi người đều bị ảnh hưởng.");
                } else if ( intAqi > 300) {
                    relativeLayout.setBackgroundResource(R.color.brown);
                    status.setText("Nguy hại");
                    description.setText("Cảnh báo sức khỏe: tất cả mọi người có thể chịu tác động nghiêm trọng đến sức khỏe.");
                }


            } catch (Exception e){
                System.out.println("Exception while parsing response");
                e.printStackTrace();
            }
        }
    }
}
