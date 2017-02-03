package com.yanzhiwei.goodweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yanzhiwei on 2017/2/3.
 */

public class Now {
    /**温度**/
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public Condition condition;

    public class Condition{
        //天气状况代码
        @SerializedName("code")
        public String conditionCode;
        //天气状况描述
        @SerializedName("txt")
        public String info;
    }

}
