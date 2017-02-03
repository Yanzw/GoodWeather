package com.yanzhiwei.goodweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yanzhiwei on 2017/2/3.
 */

public class Basic {
    //城市名称
    @SerializedName("city")
    public String cityName;
    //城市对应的ID
    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        //当地时间
        @SerializedName("loc")
        public String updateTime;
    }


}
