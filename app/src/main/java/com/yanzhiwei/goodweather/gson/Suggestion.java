package com.yanzhiwei.goodweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yanzhiwei on 2017/2/3.
 */

public class Suggestion {
    //舒适度指数
    @SerializedName("comf")
    public Comfort comfort;
    //洗车指数
    @SerializedName("cw")
    public CarWash carWash;
    //运动指数
    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }

}
