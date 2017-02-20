package com.kt.iot.mobile.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.ImageView;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.TagStrmApi;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.gigaiot_sdk.data.TagStrmApiResponse;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.custom.CustomTabPageIndicator;
import com.kt.iot.mobile.ui.fragment.DeviceTabPageAdapter;
import com.kt.iot.mobile.ui.fragment.device.control.DeviceCtrlListFragment;
import com.kt.iot.mobile.ui.fragment.event.list.EventListFragment;
import com.kt.iot.mobile.ui.fragment.rawdata.RawdataGraphWebViewFragment;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.ModifyDeviceMgr;
import com.kt.iot.mobile.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 22..
 */
public class DeviceActivity extends ActionBarActivity {

    private final String TAG = DeviceActivity.class.getSimpleName();

    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_SVCTGT = "svctgt";
    /****************/
    public static  String strDevice="";
    /***************/
    private Device mDevice;
    private SvcTgt mSvcTgt;

    private ArrayList<Fragment> mArrayFragments = new ArrayList<>();
    private String[] mArrayPageTitles = {"Event", "Log", "Control"};

    //private DeviceDetailFragment mDeviceDetailFragment;
    private RawdataGraphWebViewFragment mRawdataGraphWebViewFragment;
    private EventListFragment mEventListFragment;
    private DeviceCtrlListFragment mDeviceCtrlListFragment;

    private DisplayImageOptions mOptions;
    private ImageView mIvDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);



        strDevice = getIntent().getStringExtra(EXTRA_DEVICE);
        String strSvcTgt = getIntent().getStringExtra(EXTRA_SVCTGT);

        Gson gson = new Gson();
        mDevice = gson.fromJson(strDevice, Device.class);
        mSvcTgt = gson.fromJson(strSvcTgt, SvcTgt.class);

        //getSupportActionBar().setTitle(mDevice.getDevNm());

        getSupportActionBar().setCustomView(Util.getCustomTitleView(this, mDevice.getDevNm()));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.pre);




        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.white_solid)
                .showImageForEmptyUri(R.drawable.bg_01)
                .showImageOnFail(R.drawable.bg_01)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        initDeviceDetail();
        initDeviceTab();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mDevice != null && ModifyDeviceMgr.getModifyDevice() != null){

            //최근 수정된 디바이스 정보와 spotDevSeq가 같은경우
            if(mDevice.getSpotDevSeq().equals(ModifyDeviceMgr.getModifyDevice().getSpotDevSeq())){

                mDevice.setDevNm(ModifyDeviceMgr.getModifyDevice().getDevNm());
                getSupportActionBar().setCustomView(Util.getCustomTitleView(this, mDevice.getDevNm()));

                //이미지 파일명이 다르면 다시 이미지 로드
                if(!mDevice.getAtcFileNm().equals(ModifyDeviceMgr.getModifyDevice().getAtcFileNm()) ){

                    mDevice.setAtcFileNm(ModifyDeviceMgr.getModifyDevice().getAtcFileNm());

                    deviceImgLoad();
                }
            }
        }

    }

    private void deviceImgLoad(){

        if(!TextUtils.isEmpty(mDevice.getAtcFileNm())){

            //http://220.90.216.73:8080/masterapi/v1/devices/1000000569/1000000511/downloadImgStream

            String uriTemplate = "http://220.90.216.73:8080/masterapi/v1/devices/%s/%s/image/%s";

            String imageUri = String.format(uriTemplate, mDevice.getSvcTgtSeq(), mDevice.getSpotDevSeq(), mDevice.getAtcFileNm());

            ImageLoader.getInstance().displayImage(imageUri, mIvDevice, mOptions);

        }else{
            ImageLoader.getInstance().displayImage("", mIvDevice, mOptions);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();
                return true;

            case R.id.action_ctrl: {
                Log.w(TAG, "onOptionsItemSelected!! id = R.id.action_ctrl");
                Gson gson = new Gson();
                Intent intent = new Intent(this, DeviceSettingActivity.class);
                String strDevice = gson.toJson(mDevice);
                String strSvcTgt = gson.toJson(mSvcTgt);
                intent.putExtra(DeviceSettingActivity.EXTRA_DEVICE, strDevice);
                intent.putExtra(DeviceSettingActivity.EXTRA_SVCTGT, strSvcTgt);
                startActivity(intent);
                return true;
            }

            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void initDeviceDetail(){

        mIvDevice = (ImageView) findViewById(R.id.iv_device);

        deviceImgLoad();

    }

    private void initDeviceTab(){
        mEventListFragment = new EventListFragment();
        mRawdataGraphWebViewFragment = new RawdataGraphWebViewFragment();
        mDeviceCtrlListFragment = new DeviceCtrlListFragment();

        mArrayFragments.add(mEventListFragment);
        mArrayFragments.add(mRawdataGraphWebViewFragment);
        mArrayFragments.add(mDeviceCtrlListFragment);

        DeviceTabPageAdapter adapter = new DeviceTabPageAdapter(getSupportFragmentManager(), mArrayPageTitles, mArrayFragments);

        ViewPager pager = (ViewPager) findViewById(R.id.device_pager2);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);

        CustomTabPageIndicator indicator = (CustomTabPageIndicator) findViewById(R.id.device_tab_indicator2);
        indicator.setViewPager(pager);

        pager.setCurrentItem(0);

        new GetTagStrmListTask().execute();
    }


    private class GetTagStrmListTask extends AsyncTask<Void, Void, Void>{  // tag stream 목록 가져오기 not log값

        @Override
        protected Void doInBackground(Void... params) {

            TagStrmApi tagStrmApi = new TagStrmApi(ApplicationPreference.getInstance().getPrefAccessToken());
            TagStrmApiResponse response = tagStrmApi.getTagStrmList(mDevice.getSvcTgtSeq(), mDevice.getSpotDevSeq());
            mDevice.setTagStrmList(response.getTagStrms());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mRawdataGraphWebViewFragment.setDevice(mDevice);
            mRawdataGraphWebViewFragment.refresh();

            mEventListFragment.setDevice(mDevice);
            mEventListFragment.refresh();

            mDeviceCtrlListFragment.setDevice(mDevice);
            mDeviceCtrlListFragment.refresh();

        }
    }

}
