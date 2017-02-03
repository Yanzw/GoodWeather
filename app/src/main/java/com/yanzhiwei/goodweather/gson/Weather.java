package com.yanzhiwei.goodweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 天气类
 * Created by yanzhiwei on 2017/2/3.
 */

public class Weather {
    /**接口状态**/
    public String status;
    /**基本信息**/
    public Basic basic;
    /**AQI指数**/
    public AQI aqi;
    /**实况天气**/
    public Now now;
    /**生活指数，仅限国内城市**/
    public Suggestion suggestion;
    /**7-10天天气预报**/
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
