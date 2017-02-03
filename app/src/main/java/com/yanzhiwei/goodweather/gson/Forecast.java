package com.yanzhiwei.goodweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yanzhiwei on 2017/2/3.
 */

public class Forecast {
    //预报日期
    public String date;
    //天气状况
    @SerializedName("cond")
    public ForecastCondition condition;
    //温度
    @SerializedName("tmp")
    public Temperature temperature;

    public class ForecastCondition{
        //白天天气状况描述
        @SerializedName("txt_d")
        public String info_day;
        //夜间天气状况描述
        @SerializedName("txt_n")
        public String info_night;
    }

    public class Temperature{
        //最高温度
        public String max;
        //最低温度
        public String min;
    }

}
