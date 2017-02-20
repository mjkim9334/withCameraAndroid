package com.kt.iot.mobile.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.device.list.DeviceListFragment;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.BitmapUtil;

/**
 * Created by ceoko on 15. 4. 30..
 */
public class DeviceListActivity extends ActionBarActivity implements DeviceListFragment.OnDeviceListSelectedListener{

    private final String TAG = DeviceListActivity.class.getSimpleName();

    public final static String EXTRA_SVCTGT = "svctgt";

    Gson mGson;
    SvcTgt mSvcTgt;

    private ImageView mIvBg;
    private String mBgImgPath;
    private int mBgRsrcId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_common);

        mGson = new Gson();
        mSvcTgt = mGson.fromJson(getIntent().getStringExtra(EXTRA_SVCTGT), SvcTgt.class);

        Log.w(TAG, mSvcTgt.toString());

        DeviceListFragment fragment = new DeviceListFragment();
        fragment.setSvcTgt(mSvcTgt);

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, fragment).commit();

        getSupportActionBar().setElevation(0);

        mIvBg = (ImageView) findViewById(R.id.iv_common_bg);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case android.R.id.home:

                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeviceSelected(int position, Device device) {

        if(device != null) {
            String strDevice = mGson.toJson(device);
            String strSvcTgt = mGson.toJson(mSvcTgt);

            Intent intent = new Intent(DeviceListActivity.this, DeviceActivity.class);
            intent.putExtra(DeviceActivity.EXTRA_DEVICE, strDevice);
            intent.putExtra(DeviceActivity.EXTRA_SVCTGT, strSvcTgt);

            startActivity(intent);

        }else{

            String strSvcTgt = mGson.toJson(mSvcTgt);

            Intent intent = new Intent(DeviceListActivity.this, DeviceRegActivity.class);
            intent.putExtra(DeviceRegActivity.EXTRA_SVCTGT, strSvcTgt);

            startActivity(intent);
        }
    }

    private void setBackground(){

        String bgImgPath = ApplicationPreference.getInstance().getPrefBgUserCustom();
        int bgRsrcId = ApplicationPreference.getInstance().getPrefBgDefaultId();

        if(mBgImgPath != null && mBgImgPath.equals(bgImgPath) && mBgRsrcId == bgRsrcId){
            return;
        }

        if(bgImgPath != null && bgImgPath.equals("") == false){

            mIvBg.setImageBitmap(BitmapFactory.decodeFile(bgImgPath));
            mBgImgPath = bgImgPath;
            mBgRsrcId = bgRsrcId;
            return;

        }

        mIvBg.setImageBitmap(BitmapUtil.getBitmapFromResource(getResources(), bgRsrcId, 2048, 2048));
        mBgImgPath = bgImgPath;
        mBgRsrcId = bgRsrcId;

    }
}
