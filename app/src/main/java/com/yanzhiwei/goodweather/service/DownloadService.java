package com.yanzhiwei.goodweather.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.yanzhiwei.goodweather.DownloadActivity;
import com.yanzhiwei.goodweather.R;
import com.yanzhiwei.goodweather.util.LogUtil;

import java.io.File;

/**
 * Created by yanzhiwei on 2017/1/31.
 */

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    private DownloadTask downloadTask;
    private String downloadUrl;

    private ProgressCallBack callBack;
    public interface ProgressCallBack{
        void onProgressChange(int progress);
    }

    public void setCallBack(ProgressCallBack callBack){
        this.callBack = callBack;
    }

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager()
                    .notify(1, getNotification(getString(R.string.downloading), progress));
            callBack.onProgressChange(progress);
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager()
                    .notify(1, getNotification(getString(R.string.download_success), -1));
            Toast.makeText(DownloadService.this,
                    getString(R.string.download_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification(getString(R.string.download_failed), -1));
            Toast.makeText(DownloadService.this,
                    getString(R.string.download_failed), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this,
                    getString(R.string.download_paused), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,
                    getString(R.string.download_canceled), Toast.LENGTH_SHORT).show();
        }
    };

    public DownloadService() {
    }

    public DownloadBinder mBinder = new DownloadBinder();

    public class DownloadBinder extends Binder {

        public DownloadService getService(){
            return DownloadService.this;
        }

        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification(getString(R.string.downloading), 0));
                Toast.makeText(DownloadService.this, getString(R.string.downloading)
                        , Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            } else {
                LogUtil.d(TAG, "cancelDownload: downloadTask = null ");
                if (downloadUrl != null) {
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    Toast.makeText(DownloadService.this, getString(R.string.download_canceled),
                            Toast.LENGTH_SHORT).show();
                    stopForeground(true);
                }
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, DownloadActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        builder.setAutoCancel(true);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
