package com.example.compucity.mweather.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.compucity.mweather.data.WeatherContract;

/**
 * Created by CompuCity on 12/17/2017.
 */

public class WeatherSyncUtils {
    private static boolean sInitialized;

    synchronized public static void Initialize(@NonNull final Context context) {
        if (sInitialized) return;
        sInitialized = true;
        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... voids) {
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                String[] projectionColumns = {WeatherContract.WeatherEntry._ID};
                String selectionStatement = WeatherContract.WeatherEntry
                        .getSqlSelectForTodayOnwards();
                Cursor cursor = context.getContentResolver().query(
                        forecastQueryUri,
                        projectionColumns,
                        selectionStatement,
                        null,
                        null);
                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }
                cursor.close();
                return null;
            }
        }.execute();
    }

    public static void startImmediateSync(@NonNull Context context) {
        Intent intent = new Intent(context, WeatherIntentServic.class);
        context.startService(intent);
    }
}
