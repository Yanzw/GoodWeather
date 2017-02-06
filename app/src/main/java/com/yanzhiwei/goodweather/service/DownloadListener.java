package com.yanzhiwei.goodweather.service;

/**
 * 下载监听器的回调方法
 * Created by yanzhiwei on 2017/1/31.
 */

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();
}
