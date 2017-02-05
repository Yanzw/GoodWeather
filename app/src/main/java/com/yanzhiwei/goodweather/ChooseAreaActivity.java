package com.yanzhiwei.goodweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import static com.yanzhiwei.goodweather.util.Constant.FROM_ACTIVITY;
import static com.yanzhiwei.goodweather.util.Constant.PERFERENCE_WEATHER;

public class ChooseAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getStringExtra(FROM_ACTIVITY)==null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String prefWeatherStr = prefs.getString(PERFERENCE_WEATHER, "");
            if (!TextUtils.isEmpty(prefWeatherStr)) {
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
