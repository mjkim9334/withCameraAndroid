package com.kt.iot.mobile.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.device.DeviceRegFragment;
import com.kt.iot.mobile.utils.Util;

/**
 * Created by ceoko on 15. 5. 4..
 */
public class DeviceRegActivity extends ActionBarActivity {

    public final static String EXTRA_SVCTGT = "svctgt";

    private Gson mGson;
    private SvcTgt mSvcTgt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_common);


        mGson = new Gson();

     /*   String strDevice = getIntent().getStringExtra(EXTRA_DEVICE);

        Gson gson = new Gson();
        Device device = gson.fromJson(strDevice, Device.class);*/

        mSvcTgt = mGson.fromJson(getIntent().getStringExtra(EXTRA_SVCTGT), SvcTgt.class);


        getSupportActionBar().setCustomView(Util.getCustomTitleView(this, "디바이스 등록"));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.pre);

        DeviceRegFragment fragment = new DeviceRegFragment();
        fragment.setSvcTgt(mSvcTgt);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, fragment).commit();

        /*DeviceSettingFragment fragment = new DeviceSettingFragment();
        fragment.setDevice(device);

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, fragment).commit();*/

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
