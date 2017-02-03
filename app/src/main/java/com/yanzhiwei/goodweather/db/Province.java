package com.yanzhiwei.goodweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省实体类
 * Created by yanzhiwei on 2017/2/3.
 */

public class Province extends DataSupport {
    /**建表id字段**/
    private int id;
    /**省的名称**/
    private String provinceName;
    /**省的代号**/
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
