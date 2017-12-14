package com.example.compucity.mweather.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.compucity.mweather.WeatherAdapter;
import com.example.compucity.mweather.data.WeatherContract.WeatherEntry;

/**
 * Created by CompuCity on 12/14/2017.
 */

public class WeatherDpHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 3;

    public WeatherDpHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE =

                "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +

                        WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        WeatherEntry.COLUMN_DATE + " INTEGER  NOT NULL, " +

                        WeatherEntry.COLUMN_WEATHER_ID + " INTEGER  NOT NULL, " +

                        WeatherEntry.COLUMN_MIN_TEMP + " REAL  NOT NULL, " +
                        WeatherEntry.COLUMN_MAX_TEMP + " REAL  NOT NULL, " +

                        WeatherEntry.COLUMN_HUMIDITY + " REAL  NOT NULL, " +
                        WeatherEntry.COLUMN_PRESSURE + " REAL  NOT NULL, " +

                        WeatherEntry.COLUMN_WIND_SPEED + " REAL  NOT NULL, " +
                        WeatherEntry.COLUMN_DEGREES + " REAL  NOT NULL" +
                        " UNIQUE (" + WeatherEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
