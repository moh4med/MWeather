package com.example.compucity.mweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.compucity.mweather.data.SunshinePreferences;
import com.example.compucity.mweather.utilities.NetworkUtils;
import com.example.compucity.mweather.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tv_weatherData, tv_errorMessage;
    ProgressBar pb_weatherloading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_weatherData = (TextView) findViewById(R.id.tv_weather_data);
        pb_weatherloading = (ProgressBar) findViewById(R.id.pb_loading_data);
        tv_errorMessage = (TextView) findViewById(R.id.error_message_tv);
        loadWeatherData();

    }

    private void loadWeatherData() {
        new WeatherDataAsyncTask().execute(SunshinePreferences.getPreferredWeatherLocation(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            tv_weatherData.setText("");
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class WeatherDataAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_weatherloading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String[] strings) {
            pb_weatherloading.setVisibility(View.INVISIBLE);
            if (strings == null) {
                showerrormessage();
            } else {
                showweatherdata();
                for (String s : strings) {
                    tv_weatherData.append(s + "\n\n\n");
                }
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

    private void showerrormessage() {
        tv_errorMessage.setVisibility(View.VISIBLE);
        tv_weatherData.setVisibility(View.INVISIBLE);
    }

    private void showweatherdata() {
        tv_errorMessage.setVisibility(View.INVISIBLE);
        tv_weatherData.setVisibility(View.VISIBLE);
    }
}
