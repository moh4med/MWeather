package com.example.compucity.mweather.sync;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by CompuCity on 12/17/2017.
 */

public class WeatherFirebaseJobService extends JobService {
    private AsyncTask<Void, Void, Void> mFetchWeatherTask;
    @Override
    public boolean onStartJob(final JobParameters job) {
        mFetchWeatherTask=new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                WeatherSyncUtils.startImmediateSync(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job,false);
            }
        };
        mFetchWeatherTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mFetchWeatherTask!=null){
            mFetchWeatherTask.cancel(true);
        }
        return true;
    }
}
