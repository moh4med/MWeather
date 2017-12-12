package com.example.compucity.mweather;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by CompuCity on 12/12/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder>{
    String TAG=WeatherAdapter.class.getSimpleName();
    private String []weatherDataStrings;
    WeatherAdapterOnClickHandler mWeatherAdapterOnClickHandler;
    interface WeatherAdapterOnClickHandler{
        void onclick(String s);
    }
    public void setWeatherDataStrings(String[]s){
        weatherDataStrings=s;
        notifyDataSetChanged();
    }
    public WeatherAdapter(WeatherAdapterOnClickHandler w){
        mWeatherAdapterOnClickHandler=w;
    }
    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"creating view holder");
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.forecast_list_item,parent,false);
        return new WeatherAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherAdapterViewHolder holder, int position) {
        Log.d(TAG,"binding view holder number :"+position);
        Log.d(TAG,"previous message is:"+holder.tv_weatherdata.getText().toString());
        Log.d(TAG,"now message is:"+weatherDataStrings[position]);
        holder.tv_weatherdata.setText(weatherDataStrings[position]);
    }

    @Override
    public int getItemCount() {
        if(weatherDataStrings==null)return 0;
        return weatherDataStrings.length;
    }

    public class WeatherAdapterViewHolder extends ViewHolder implements View.OnClickListener{
        TextView tv_weatherdata;
        public WeatherAdapterViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG,"In view holder");
            tv_weatherdata=(TextView)itemView.findViewById(R.id.tv_weather_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mWeatherAdapterOnClickHandler.onclick(weatherDataStrings[getAdapterPosition()]);
        }
    }

}
