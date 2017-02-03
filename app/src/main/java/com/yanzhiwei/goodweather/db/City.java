package com.yanzhiwei.goodweather.db;

import org.litepal.crud.DataSupport;

/**
 * 市的实体类
 * Created by yanzhiwei on 2017/2/3.
 */

public class City extends DataSupport {
    /**建表id字段**/
    private int id;
    /**市的名称**/
    private String cityName;
    /**市的代号**/
    private int cityCode;
    /**该市所属的省的代号**/
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
