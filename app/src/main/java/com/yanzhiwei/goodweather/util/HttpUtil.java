package com.yanzhiwei.goodweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 网络访问工具
 * Created by yanzhiwei on 2017/2/3.
 */

public class HttpUtil {

    /**
     * 发送Http请求
     * @param address 请求地址
     * @param callback 请求后的回调
     */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }


}
