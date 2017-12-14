package com.example.compucity.mweather.data;

import android.provider.BaseColumns;

/**
 * Created by CompuCity on 12/14/2017.
 */

public class WeatherContract {
    //  TODO (1) Within WeatherContract, create a public static final class called WeatherEntry that implements BaseColumns

    //      Do steps 2 through 10 within the WeatherEntry class
    public static final class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_DATE = "data";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_DEGREES = "degrees";

    }
}
