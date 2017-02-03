package com.yanzhiwei.goodweather.gson;

/**
 * 空气质量，仅限国内城市
 * Created by yanzhiwei on 2017/2/3.
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        //空气质量指数
        public String aqi;
        //PM2.5 1小时平均值(ug/m³)
        public String pm25;

    }

}
