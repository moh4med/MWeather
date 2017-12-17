package com.example.compucity.mweather.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by CompuCity on 12/17/2017.
 */

public class WeatherIntentServic extends IntentService {

    public WeatherIntentServic() {
        super(WeatherIntentServic.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        WeatherSyncTask.syncWeather(this);
    }
}
