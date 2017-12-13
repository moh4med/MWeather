package com.example.compucity.mweather;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
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

import com.example.compucity.mweather.data.SunshinePreferences;
import com.example.compucity.mweather.utilities.NetworkUtils;
import com.example.compucity.mweather.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements WeatherAdapter.WeatherAdapterOnClickHandler
        , LoaderCallbacks<String[]> {
    TextView tv_errorMessage;
    ProgressBar pb_weatherloading;
    RecyclerView mRecycleview;
    WeatherAdapter mWeatherAdapter;
    String TAG=MainActivity.class.getSimpleName();
    private static int WEATHER_LOADER_ID=0;
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
        getSupportLoaderManager().initLoader(WEATHER_LOADER_ID,null,MainActivity.this);
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
            getSupportLoaderManager().restartLoader(WEATHER_LOADER_ID,null,  this);
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


    @Override
    public android.support.v4.content.Loader<String[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            String[] weatherData=null;
            @Override
            protected void onStartLoading() {
                if(weatherData==null){
                    pb_weatherloading.setVisibility(View.VISIBLE);
                    forceLoad();
                }else{
                    deliverResult(weatherData);
                }
            }

            @Override
            public String[] loadInBackground() {
                String loc=SunshinePreferences.getPreferredWeatherLocation(MainActivity.this);
                URL url = NetworkUtils.buildUrl(loc);
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

            @Override
            public void deliverResult(String[] data) {
                weatherData=data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String[]> loader, String[] data) {
        pb_weatherloading.setVisibility(View.INVISIBLE);
        mWeatherAdapter.setWeatherDataStrings(data);
        if (data == null) {
            showerrormessage();
        } else {
            showweatherdata();
            mWeatherAdapter.setWeatherDataStrings(data);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String[]> loader) {

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
