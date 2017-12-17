package com.example.compucity.mweather;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.compucity.mweather.utilities.SunshineDateUtils;
import com.example.compucity.mweather.utilities.SunshineWeatherUtils;

/**
 * Created by CompuCity on 12/12/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder> {
    String TAG = WeatherAdapter.class.getSimpleName();
    private final Context mContext;
    private Cursor mCursor;
    WeatherAdapterOnClickHandler mWeatherAdapterOnClickHandler;

    interface WeatherAdapterOnClickHandler {
        void onclick(long date);
    }

    public WeatherAdapter(WeatherAdapterOnClickHandler w, @NonNull Context context) {
        mWeatherAdapterOnClickHandler = w;
        this.mContext = context;
    }

    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "creating view holder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new WeatherAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherAdapterViewHolder holder, int position) {
        //holder.tv_weatherdata.setText(weatherDataStrings[position]);
        mCursor.moveToPosition(position);
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);
        String highAndLowTemperature =
                SunshineWeatherUtils.formatHighLows(mContext, highInCelsius, lowInCelsius);
        String weatherSummary = dateString + " - " + description + " - " + highAndLowTemperature;
        holder.tv_weatherdata.setText(weatherSummary);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public class WeatherAdapterViewHolder
            extends ViewHolder
            implements View.OnClickListener {
        final TextView tv_weatherdata;

        public WeatherAdapterViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "In view holder");
            tv_weatherdata = (TextView) itemView.findViewById(R.id.tv_weather_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mWeatherAdapterOnClickHandler.onclick(dateInMillis);
        }
    }

}
