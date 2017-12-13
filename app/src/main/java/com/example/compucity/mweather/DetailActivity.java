package com.example.compucity.mweather;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView tv_detailData;
    String detailData;
    private String FORECAST_HASHTAG=" #MWeather";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tv_detailData=findViewById(R.id.tv_detailData);
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.hasExtra(Intent.EXTRA_TEXT)){
                detailData=intent.getStringExtra(Intent.EXTRA_TEXT);
                tv_detailData.setText(detailData);
            }
        }
    }
    Intent getShareIntent(){
        Intent intent=ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(detailData+FORECAST_HASHTAG)
                .getIntent();
        return intent;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        MenuItem item = menu.findItem(R.id.action_share);
        item.setIntent(getShareIntent());
        return true;
    }
}
