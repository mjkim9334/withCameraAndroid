package com.kt.iot.mobile.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.device.DeviceSettingFragment;
import com.kt.iot.mobile.utils.Util;

/**
 * Created by ceoko on 15. 5. 4..
 */
public class DeviceSettingActivity extends ActionBarActivity {

    private final String TAG = DeviceSettingActivity.class.getSimpleName();

    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_SVCTGT = "svctgt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_overlay);


        String strDevice = getIntent().getStringExtra(EXTRA_DEVICE);
        String strSvcTgt = getIntent().getStringExtra(EXTRA_SVCTGT);

        Gson gson = new Gson();
        Device device = gson.fromJson(strDevice, Device.class);
        SvcTgt svcTgt = gson.fromJson(strSvcTgt, SvcTgt.class);

        Log.d(TAG, "strSvcTgt = " + strSvcTgt);

        getSupportActionBar().setCustomView(Util.getCustomTitleView(this, device.getDevNm()));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.pre);


        DeviceSettingFragment fragment = new DeviceSettingFragment();
        fragment.setDevice(device);
        fragment.setSvcTgt(svcTgt);

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
