package com.yanzhiwei.goodweather.db;

import org.litepal.crud.DataSupport;

/**
 * 县的实体类
 * Created by yanzhiwei on 2017/2/3.
 */

public class County extends DataSupport {
    /**建表的id字段**/
    private int id;
    /**县的名称**/
    private String countyName;
    /**县的天气ID**/
    private String weatherId;
    /**该县所属的城市ID**/
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
