package com.example.compucity.mweather.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Switch;

import com.example.compucity.mweather.utilities.SunshineDateUtils;

/**
 * Created by CompuCity on 12/14/2017.
 */

public class WeatherProvider extends ContentProvider {
    private WeatherDpHelper mopenHelper;
    public static final int CODE_WEATHER = 100;
    public static final int CODE_WEATHER_WITH_DATE = 101;
    public static UriMatcher muriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        mopenHelper = new WeatherDpHelper(getContext());
        return true;
    }

    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String auth = WeatherContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(auth, WeatherContract.PATH_WEATHER, CODE_WEATHER);
        uriMatcher.addURI(auth, WeatherContract.PATH_WEATHER + "/#", CODE_WEATHER_WITH_DATE);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection
            , @Nullable String[] SelectionArgs, @Nullable String sortorder) {
        Cursor cursor;
        String norutcdate = uri.getLastPathSegment();
        String[] SelArg = new String[]{norutcdate};
        switch (muriMatcher.match(uri)) {
            case CODE_WEATHER_WITH_DATE: {
                cursor = mopenHelper
                        .getReadableDatabase().query(
                                WeatherContract.WeatherEntry.TABLE_NAME
                                , projection
                                , WeatherContract.WeatherEntry.COLUMN_DATE + " = ? "
                                , SelArg
                                , null
                                , null
                                , sortorder);
                break;
            }
            case CODE_WEATHER: {
                cursor = mopenHelper
                        .getReadableDatabase().query(
                                WeatherContract.WeatherEntry.TABLE_NAME
                                , projection
                                , selection
                                , SelectionArgs
                                , null
                                , null
                                , sortorder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mopenHelper.getWritableDatabase();

        switch (muriMatcher.match(uri)) {

            case CODE_WEATHER:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long weatherDate =
                                value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
                        if (!SunshineDateUtils.isDateNormalized(weatherDate)) {
                            throw new IllegalArgumentException("Date must be normalized to insert");
                        }

                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionarg) {
        int rowdeleted = 0;
        if (selection == null) selection = "1";
        switch (muriMatcher.match(uri)) {
            case CODE_WEATHER: {
                rowdeleted=mopenHelper.getWritableDatabase().delete(
                        WeatherContract.WeatherEntry.TABLE_NAME
                        ,selection
                        ,selectionarg);
                if (rowdeleted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowdeleted;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
