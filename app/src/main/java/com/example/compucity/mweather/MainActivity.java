package com.example.compucity.mweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.compucity.mweather.data.SunshinePreferences;
import com.example.compucity.mweather.utilities.NetworkUtils;
import com.example.compucity.mweather.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tvweatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvweatherData = (TextView) findViewById(R.id.tv_weather_data);
        loadWeatherData();

    }

    private void loadWeatherData() {
        new WeatherDataAsyncTask().execute(SunshinePreferences.getPreferredWeatherLocation(this));
    }

    public class WeatherDataAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPostExecute(String[] strings) {
            if (strings == null) return;
            for (String s : strings) {
                tvweatherData.append(s + "\n\n\n");
            }
        }

        @Override
        protected String[] doInBackground(String... strings) {
            URL url = NetworkUtils.buildUrl(strings[0]);
            String jsondata = null;
            String[] data = null;
            try {
                jsondata = NetworkUtils.getResponseFromHttpUrl(url);
                data = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsondata);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }
    }
}
