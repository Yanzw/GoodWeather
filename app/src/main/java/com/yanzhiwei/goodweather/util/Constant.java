package com.yanzhiwei.goodweather.util;

import static com.yanzhiwei.goodweather.BuildConfig.MY_WEATHER_MAP_API_KEY;

/**
 * 常量类
 * Created by yanzhiwei on 2017/2/3.
 */

public class Constant {
    /**JSON字段**/
    public static final String PROVINCE_CODE = "id";
    public static final String PROVINCE_NAME = "name";

    public static final String CITY_NAME = "name";
    public static final String CITY_CODE = "id";

    public static final String COUNTY_NAME = "name";
    public static final String COUNTY_WEATHER_ID = "weather_id";
    public static final String STATUS_OK = "ok";
    public static final String FROM_ACTIVITY = "FromActivity";

    /**URL列表***/
    /**中国全部省的列表***/
    public static final String CHINA_PROVINCES_URL = "http://guolin.tech/api/china";

    public static final String PERFERENCE_WEATHER = "weather";
    public static final String PERFERENCE_BING_PIC = "bing_pic";
    public static final String PERFERENCE_AUTO_UPDATE_PIC = "perference_auto_update_pic";
    public static final String PERFERENCE_AUTO_UPDATE_DATA = "perference_auto_update_data";
    public static final String HE_WEATHER = "HeWeather";
    public static final String WEATHER_ID = "weather_id";

    public static final String REQUEST_WEATHER_HEAD  = "http://guolin.tech/api/weather?cityid=";
    public static final String HE_WEATHER_API_KEY = "&key="+ MY_WEATHER_MAP_API_KEY;
    public static final String REQUEST_BING_PIC = "http://guolin.tech/api/bing_pic";

}
