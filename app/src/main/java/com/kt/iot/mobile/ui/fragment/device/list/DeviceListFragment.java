package com.kt.iot.mobile.ui.fragment.device.list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.DeviceApi;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.DeviceApiResponse;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.activity.DeviceRegActivity;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.ModifyDeviceMgr;
import com.kt.iot.mobile.utils.Util;

import java.util.ArrayList;

/**
 * Created by dongminyoon on 2015. 2. 24..
 */

public class DeviceListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, AbsListView.OnScrollListener {

    private static final String TAG = DeviceListFragment.class.getSimpleName();

    public interface OnDeviceListSelectedListener {
        public void onDeviceSelected(int position, Device device);
    }

    private OnDeviceListSelectedListener mCallback;

    /**
     * Called when the activity is first created.
     */
    private ArrayList<Device> mDevices;
    private DeviceListAdapter mAdapter;

    private SvcTgt mSvcTgt;

    private final int ROW_CNT = 10;
    private int mPageNum = 1;
    private int mListMax = 0;
    private boolean mProtectDuplicate 	= false;

    private ListView mListView;
    private View mFootloadview;

    private LinearLayout mLinearAddDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDevices = new ArrayList<Device>();

        View view = inflater.inflate(R.layout.fragment_device_list, container, false);
        mListView = (ListView) view.findViewById(R.id.lv_device_list);

        mFootloadview = inflater.inflate(R.layout.itemview_footer_loading, null);
        mListView.addFooterView(mFootloadview);

        mAdapter = new DeviceListAdapter(getActivity(), android.R.layout.simple_list_item_1, mDevices);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        mLinearAddDevice = (LinearLayout) view.findViewById(R.id.layout_linear_device_add);
        mLinearAddDevice.setOnClickListener(this);

        refresh();

        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.layout_linear_device_add:

                Gson gson = new Gson();
                String strSvcTgt = gson.toJson(mSvcTgt);

                Intent intent = new Intent(getActivity(), DeviceRegActivity.class);
                intent.putExtra(DeviceRegActivity.EXTRA_SVCTGT, strSvcTgt);

                startActivity(intent);

                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(position < mDevices.size()) {
            if (mCallback != null) {
                mCallback.onDeviceSelected(position, mDevices.get(position));
            }
        }else{
            //Toast.makeText(getActivity(), "디바이스 추가가 선택되었습니다.", Toast.LENGTH_SHORT).show();

            /*if (mCallback != null) {
                mCallback.onDeviceSelected(position, null);
            }*/
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if(totalItemCount > 0 && (firstVisibleItem + visibleItemCount) == totalItemCount && !mProtectDuplicate){

            Log.i(TAG, "Bottom!!!!!!! - " + totalItemCount);

            if(mDevices.size() < mListMax)
                new GetDevListTask().execute();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnDeviceListSelectedListener) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallback = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((ActionBarActivity)getActivity()).getSupportActionBar()
                .setCustomView(Util.getCustomTitleView(getActivity(), mSvcTgt.getSvcTgtNm()));
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);

        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.pre);

        refreshModifyDevice();

    }

    private void refreshModifyDevice(){

        if(mDevices.size() > 0 && ModifyDeviceMgr.getModifyDevice() != null){

            for(Device tmp : mDevices){

                if(tmp.getSpotDevSeq().equals(ModifyDeviceMgr.getModifyDevice().getSpotDevSeq())){

                    tmp.setDevNm(ModifyDeviceMgr.getModifyDevice().getDevNm());

                    //이미지 파일명이 다르면 다시 이미지 로드
                    if(!tmp.getAtcFileNm().equals(ModifyDeviceMgr.getModifyDevice().getAtcFileNm()) ){

                        tmp.setAtcFileNm(ModifyDeviceMgr.getModifyDevice().getAtcFileNm());
                    }

                    mAdapter.notifyDataSetChanged();

                    break;
                }

            }

        }


    }

    public void setSvcTgt(SvcTgt svcTgt) {
        mSvcTgt = svcTgt;
    }

    public void refresh() {

        if (mSvcTgt != null) {

            if(mDevices != null && mDevices.size() > 0){
                mDevices.clear();
                mAdapter.notifyDataSetChanged();
            }

            new GetDevListTask().execute();


            /*new Thread(new Runnable() {
                @Override
                public void run() {

                    DeviceApi deviceApi = new DeviceApi("hello");
                    //giGaIotDev.getDeviceListOld(mSvcTgtSeq);
                    DeviceApiResponse response = deviceApi.getDeviceListOld(mSvcTgt.getSvcTgtSeq());
                    Log.i(TAG, "refresh() responseCode = " + response.getResponseCode() + " | message = "  + response.getMessage());


                    Log.i(TAG, "refresh() getDevices total = " + response.getTotal() + " | page = " + response.getPage() + " | rowNum = " + response.getRowNum());

                    ArrayList<Device> devices = response.getDevices();


                    if(devices != null && devices.size() > 0){

                        for(Device device : devices){

                            Log.i(TAG, "refresh() device toString = " + device.toString());
                            Log.i(TAG, "refresh() device.getDevPwd = " + device.getDevPwd());

                            device.setImgResId(R.drawable.bg_01);
                            device.setThumbImgResId(R.drawable.menu_livlight);
                            mDevices.add(device);

                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();

                                if (mDevices.size() < 0) {
                                    Util.showDialog(getActivity(), getString(R.string.msg_device_not_found), getString(R.string.msg_device_not_found));
                                }
                            }
                        });

                    }


                }
            }).start();*/



        }

    }

    private class GetDevListTask extends AsyncTask<Void, Void, DeviceApiResponse>{

        @Override
        protected void onPreExecute() {

            mProtectDuplicate = true;
        }

        @Override
        protected DeviceApiResponse doInBackground(Void... params) {

            DeviceApi deviceApi = new DeviceApi(ApplicationPreference.getInstance().getPrefAccessToken());
            DeviceApiResponse response = deviceApi.getDeviceList(mSvcTgt.getSvcTgtSeq(), mPageNum, ROW_CNT);

            return response;
        }

        @Override
        protected void onPostExecute(DeviceApiResponse result) {

            Log.i(TAG, "refresh() responseCode = " + result.getResponseCode() + " | message = "  + result.getMessage());

            if(result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)){

                Log.i(TAG, "refresh() getDevices total = " + result.getTotal() + " | page = " + result.getPage() + " | rowNum = " + result.getRowNum()
                        + " | devices.size() = " + result.getDevices().size());

                //mListView.setAdapter(null);

                mListMax = result.getTotal();

                if(result.getDevices() != null && result.getDevices().size() > 0){

                    for(Device device : result.getDevices()){

                        mDevices.add(device);
                    }

                }

                if(result.getTotal() > mDevices.size()){

                    //mListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    mPageNum++;

                }else{

                    mListView.removeFooterView(mFootloadview);
                }

                mProtectDuplicate = false;
            }


        }
    }


}