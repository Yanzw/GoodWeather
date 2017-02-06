package com.yanzhiwei.goodweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import static com.yanzhiwei.goodweather.util.Constant.PERFERENCE_AUTO_UPDATE_DATA;
import static com.yanzhiwei.goodweather.util.Constant.PERFERENCE_AUTO_UPDATE_PIC;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    private CheckBox checkBoxUdatePic;
    private CheckBox checkBoxUdateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        checkBoxUdatePic = (CheckBox) findViewById(R.id.cb_update_pic);
        checkBoxUdateData = (CheckBox) findViewById(R.id.cb_update_data);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean autoUpdatePic = pref.getBoolean(PERFERENCE_AUTO_UPDATE_PIC, true);
        boolean autoUpdateData = pref.getBoolean(PERFERENCE_AUTO_UPDATE_DATA, true);
        checkBoxUdatePic.setChecked(autoUpdatePic);
        checkBoxUdateData.setChecked(autoUpdateData);
        checkBoxUdatePic.setOnCheckedChangeListener(checkedChangeListener);
        checkBoxUdateData.setOnCheckedChangeListener(checkedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    switch (buttonView.getId()) {
                        case R.id.cb_update_pic:
                            if (checkBoxUdatePic.isChecked()) {
                                setPreference(PERFERENCE_AUTO_UPDATE_PIC, true);
                            } else {
                                setPreference(PERFERENCE_AUTO_UPDATE_PIC, false);
                            }
                            break;

                        case R.id.cb_update_data:
                            if (checkBoxUdateData.isChecked()) {
                                setPreference(PERFERENCE_AUTO_UPDATE_DATA, true);
                            } else {
                                setPreference(PERFERENCE_AUTO_UPDATE_DATA, false);
                            }
                            break;
                    }

                }
            };

    private void setPreference(String perfStr, boolean isChecked) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean(perfStr, isChecked);
        editor.apply();
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
}
