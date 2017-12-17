package com.example.compucity.mweather.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;

import com.example.compucity.mweather.data.WeatherContract;
import com.example.compucity.mweather.data.WeatherPreferences;
import com.example.compucity.mweather.utilities.NetworkUtils;
import com.example.compucity.mweather.utilities.NotificationUtils;
import com.example.compucity.mweather.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by CompuCity on 12/17/2017.
 */
public class WeatherSyncTask {
    synchronized public static void syncWeather(Context context){

        try {
            URL weatherurl= NetworkUtils.getUrl(context);
            String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(weatherurl);
            ContentValues[] weatherContentValuesFromJson = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, responseFromHttpUrl);
            if(weatherContentValuesFromJson!=null&&weatherContentValuesFromJson.length!=0){
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI,null,null);
                contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI,weatherContentValuesFromJson);
                boolean notificationenabled= WeatherPreferences.areNotificationsEnabled(context);
                long timeSinceLastNotification = WeatherPreferences
                        .getEllapsedTimeSinceLastNotification(context);

                boolean oneDayPassedSinceLastNotification = false;
                if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
                    oneDayPassedSinceLastNotification = true;
                }
                if (notificationenabled && oneDayPassedSinceLastNotification) {
                    NotificationUtils.notifyUserOfNewWeather(context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
