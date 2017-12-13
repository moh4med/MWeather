package com.example.compucity.mweather;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compucity.mweather.data.SunshinePreferences;
import com.example.compucity.mweather.utilities.NetworkUtils;
import com.example.compucity.mweather.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements WeatherAdapter.WeatherAdapterOnClickHandler {
    TextView tv_errorMessage;
    ProgressBar pb_weatherloading;
    RecyclerView mRecycleview;
    WeatherAdapter mWeatherAdapter;
    String TAG=MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb_weatherloading = (ProgressBar) findViewById(R.id.pb_loading_data);
        tv_errorMessage = (TextView) findViewById(R.id.error_message_tv);
        mRecycleview = findViewById(R.id.rv_forecast);
        mWeatherAdapter = new WeatherAdapter(this);
        mRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setAdapter(mWeatherAdapter);
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
            mWeatherAdapter.setWeatherDataStrings(null);
            loadWeatherData();
            return true;
        }else if(id==R.id.show_map){
            showmap();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showmap() {
        String addressString = "1600 Ampitheatre Parkway, CA";
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + geoLocation.toString()
                    + ", no receiving apps installed!");
        }
    }

    @Override
    public void onclick(String s) {
      //  Toast.makeText(this,s,Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,s);
        startActivity(intent);
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
               /* String []s=new String[20];
                for (int i=0;i<20;i++){
                    s[i]=Integer.toString(i);
                }*/
                mWeatherAdapter.setWeatherDataStrings(strings);
               // mWeatherAdapter.setWeatherDataStrings(s);
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
        mRecycleview.setVisibility(View.INVISIBLE);
    }

    private void showweatherdata() {
        tv_errorMessage.setVisibility(View.INVISIBLE);
        mRecycleview.setVisibility(View.VISIBLE);
    }
}
