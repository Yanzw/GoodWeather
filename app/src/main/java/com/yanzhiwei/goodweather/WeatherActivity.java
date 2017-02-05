package com.yanzhiwei.goodweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yanzhiwei.goodweather.gson.Forecast;
import com.yanzhiwei.goodweather.gson.Weather;
import com.yanzhiwei.goodweather.service.AutoUpdateDataService;
import com.yanzhiwei.goodweather.util.Constant;
import com.yanzhiwei.goodweather.util.HttpUtil;
import com.yanzhiwei.goodweather.util.LogUtil;
import com.yanzhiwei.goodweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yanzhiwei.goodweather.util.Constant.PERFERENCE_BING_PIC;
import static com.yanzhiwei.goodweather.util.Constant.REQUEST_BING_PIC;


public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carwashText;
    private TextView sportText;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private String mTempWeatherId = ""; //保存从Fragment发送的weatherId
    private ImageView navPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //设置全屏和状态栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        initView();
        initData();
    }

    private void initData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString(Constant.PERFERENCE_WEATHER, "");
        String weatherId;
        if (!TextUtils.isEmpty(weatherString)) {
            //有缓存直接解析天气数据
            Weather weather = Utility.handlerWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            mTempWeatherId = weatherId;
            showWeatherInfo(weather);
        } else {
            //无缓存时候联网查询天气数据
            weatherId = getIntent().getStringExtra(Constant.WEATHER_ID);
            mTempWeatherId = weatherId;
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        String bingPicUrl = preferences.getString(PERFERENCE_BING_PIC, null);
        if (!TextUtils.isEmpty(bingPicUrl)) {
            Glide.with(this).load(bingPicUrl).into(bingPicImg);
            Glide.with(this).load(bingPicUrl).into(navPicImg);
        } else {
            loadBingPic();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //联网查询天气数据
        String  weatherId = getIntent().getStringExtra(Constant.WEATHER_ID);
        weatherLayout.setVisibility(View.INVISIBLE);
        mTempWeatherId = weatherId;
        requestWeather(weatherId);
        LogUtil.d(TAG, "onRestart: weatherId ="+weatherId);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //更新最新的intent
        setIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    /**
     * 初始化View
     */
    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayShowTitleEnabled(false);    //设置去除label
        }

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navView.inflateHeaderView(R.layout.nav_header);
        navPicImg = (ImageView) navHeaderView.findViewById(R.id.nav_imageview);
        navView.setCheckedItem(R.id.nav_city);
        navView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_city:
                        Intent intent = new Intent(WeatherActivity.this,ChooseAreaActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_setting:
                        break;
                    case R.id.nav_about:
                        break;

                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forcast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carwashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //下拉刷新数据
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout
                .OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.d(TAG, "mTempWeatherId = " + mTempWeatherId);
                requestWeather(mTempWeatherId);
            }
        });

    }

    /**
     * 根据天气Id请求天气数据
     *
     * @param weatherId
     */
    public void requestWeather(String weatherId) {

        String weatherUrl = Constant.REQUEST_WEATHER_HEAD
                + weatherId + Constant.HE_WEATHER_API_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                getString(R.string.get_weather_data_fail), Toast.LENGTH_SHORT)
                                .show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handlerWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && Constant.STATUS_OK.equals(weather.status)) {
                            SharedPreferences.Editor editor =
                                    PreferenceManager
                                            .getDefaultSharedPreferences(WeatherActivity.this)
                                            .edit();
                            editor.putString(Constant.PERFERENCE_WEATHER, responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                            Toast.makeText(WeatherActivity.this,
                                    getString(R.string.get_weather_data_success), Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(WeatherActivity.this,
                                    getString(R.string.get_weather_data_fail), Toast.LENGTH_SHORT)
                                    .show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });

        loadBingPic();
    }

    /**
     * 处理并展示Weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature;
        String weatherInfo = weather.now.condition.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(getString(R.string.update_time) +" "+ updateTime);
        degreeText.setText(degree + "℃");
        weatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.condition.info_day);
            minText.setText(forecast.temperature.min);
            maxText.setText(forecast.temperature.max);
            forecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = getString(R.string.comf_index) + weather.suggestion.comfort.info;
        String carWash = getString(R.string.carwash_index) + weather.suggestion.carWash.info;
        String sport = getString(R.string.sport_index) + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carwashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        //启动定时更新数据的Service
        Intent intent = new Intent(this, AutoUpdateDataService.class);
        startService(intent);
    }

    /**
     * 请求Bing的每日一图
     */
    private void loadBingPic() {
        HttpUtil.sendOkHttpRequest(REQUEST_BING_PIC, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString(PERFERENCE_BING_PIC, bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                        Glide.with(WeatherActivity.this).load(bingPic).into(navPicImg);
                    }
                });
            }
        });
    }


}
