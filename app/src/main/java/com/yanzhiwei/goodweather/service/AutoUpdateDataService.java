package com.yanzhiwei.goodweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.yanzhiwei.goodweather.gson.Weather;
import com.yanzhiwei.goodweather.util.Constant;
import com.yanzhiwei.goodweather.util.HttpUtil;
import com.yanzhiwei.goodweather.util.LogUtil;
import com.yanzhiwei.goodweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yanzhiwei.goodweather.util.Constant.HE_WEATHER_API_KEY;
import static com.yanzhiwei.goodweather.util.Constant.PERFERENCE_BING_PIC;
import static com.yanzhiwei.goodweather.util.Constant.PERFERENCE_WEATHER;
import static com.yanzhiwei.goodweather.util.Constant.REQUEST_WEATHER_HEAD;
import static com.yanzhiwei.goodweather.util.Constant.STATUS_OK;

public class AutoUpdateDataService extends Service {
    private static final String TAG = "AutoUpdateDataService";

    public AutoUpdateDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        repeatStartService();
        LogUtil.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 重复执行启动service，间隔8小时
     */
    private void repeatStartService() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;//8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateDataService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
    }

    /**
     * 更新天气数据
     */
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString(PERFERENCE_WEATHER, "");
        if (!TextUtils.isEmpty(weatherString)) {
            //有缓存时候直接解析天气数据
            Weather weather = Utility.handlerWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = REQUEST_WEATHER_HEAD + weatherId + HE_WEATHER_API_KEY;
//            LogUtil.d(TAG,"weatherId = "+weatherId + " ,weatherUrl = "+weatherUrl);
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.e(TAG,"更新天气数据失败！");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.handlerWeatherResponse(responseText);
                    if (weather != null && STATUS_OK.equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(AutoUpdateDataService.this).edit();
                        editor.putString(PERFERENCE_WEATHER, responseText);
                        editor.apply();
//                        LogUtil.d(TAG,"更新天气数据成功，已保存到SP");
                    }
                }
            });

        }
    }

    /**
     * 更新Bing每日一图
     */
    private void updateBingPic() {
        String requestBingUrl = Constant.REQUEST_BING_PIC;
        HttpUtil.sendOkHttpRequest(requestBingUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG,"更新服务器返回的每日一图失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    SharedPreferences.Editor editor =
                            PreferenceManager
                                    .getDefaultSharedPreferences(AutoUpdateDataService.this)
                                    .edit();
                    editor.putString(PERFERENCE_BING_PIC, responseText);
                    editor.apply();
//                    LogUtil.d(TAG,"更新服务器返回的每日一图成功，已保存到SP");
                }
            }
        });
    }
}
