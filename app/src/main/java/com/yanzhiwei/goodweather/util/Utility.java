package com.yanzhiwei.goodweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yanzhiwei.goodweather.db.City;
import com.yanzhiwei.goodweather.db.County;
import com.yanzhiwei.goodweather.db.Province;
import com.yanzhiwei.goodweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.yanzhiwei.goodweather.util.Constant.HE_WEATHER;

/**
 *实用工具类
 * Created by yanzhiwei on 2017/2/3.
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     * @param response 服务器返回的请求数据
     * @return true表示保存成功
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinceJsonArray = new JSONArray(response);
                for (int i = 0; i < allProvinceJsonArray.length(); i++) {
                    JSONObject provinceObject = allProvinceJsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString(Constant.PROVINCE_NAME));
                    province.setProvinceCode(provinceObject.getInt(Constant.PROVINCE_CODE));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCityArray = new JSONArray(response);
                for (int i = 0; i < allCityArray.length(); i++) {
                    JSONObject cityObject = allCityArray.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString(Constant.CITY_NAME));
                    city.setCityCode(cityObject.getInt(Constant.CITY_CODE));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCountyArray = new JSONArray(response);
                for (int i = 0; i < allCountyArray.length(); i++) {
                    JSONObject countyObject = allCountyArray.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString(Constant.COUNTY_NAME));
                    county.setWeatherId(countyObject.getString(Constant.COUNTY_WEATHER_ID));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather对象
     * @param response
     * @return
     */
    public static Weather handlerWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(HE_WEATHER);
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Weather weather = new Gson().fromJson(weatherContent,Weather.class);
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
