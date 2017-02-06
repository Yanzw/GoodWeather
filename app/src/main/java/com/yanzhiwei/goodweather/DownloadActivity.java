package com.yanzhiwei.goodweather;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhiwei.goodweather.service.DownloadService;
import com.yanzhiwei.goodweather.util.LogUtil;

import static com.yanzhiwei.goodweather.util.Constant.PERFERENCE_BING_PIC;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DownloadActivity";
    public static final int REQUESTCODE_WRITE_EXTERNAL_STORAGE = 0x11;
    public static final int PROGRESS_UPDATE = 0x12;
    private DownloadService.DownloadBinder mDownloadBinder;
    private String downloadUrlStr;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadBinder = (DownloadService.DownloadBinder) service;
            DownloadService downloadService  = mDownloadBinder.getService();
            downloadService.setCallBack(new DownloadService.ProgressCallBack() {
                @Override
                public void onProgressChange(int progress) {
                    Message message = new Message();
                    message.arg1 = progress;
                    message.what = PROGRESS_UPDATE;
                    handler.sendMessage(message);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS_UPDATE:
                    int progress = msg.arg1;
                    LogUtil.d(TAG,"progress = "+progress);
                    progressBar.setProgress(progress);
                    progressText.setText(progress+"%");
                    break;
            }

        }
    };
    private ProgressBar progressBar;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        downloadUrlStr = prefs.getString(PERFERENCE_BING_PIC, "");

        EditText downloadUrlEdit =  (EditText) findViewById(R.id.editDownloadUrl);
        if (!TextUtils.isEmpty(downloadUrlStr)) {
            downloadUrlEdit.setText(downloadUrlStr);
        }
        Button startDownload = (Button) findViewById(R.id.start_download);
        Button pauseDownload = (Button) findViewById(R.id.pause_download);
        Button cancelDownload = (Button) findViewById(R.id.cancel_download);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressText = (TextView) findViewById(R.id.textProgress);

        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        cancelDownload.setOnClickListener(this);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);

        if (ContextCompat.checkSelfPermission(DownloadActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DownloadActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUESTCODE_WRITE_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onClick(View v) {

        if (mDownloadBinder == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.start_download:
                if (!TextUtils.isEmpty(downloadUrlStr)) {
                    mDownloadBinder.startDownload(downloadUrlStr);
                } else {
                    Toast.makeText(this, getString(R.string.download_url_null), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.pause_download:
                mDownloadBinder.pauseDownload();
                break;

            case R.id.cancel_download:
                mDownloadBinder.cancelDownload();
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.can_not_download), Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }




}
