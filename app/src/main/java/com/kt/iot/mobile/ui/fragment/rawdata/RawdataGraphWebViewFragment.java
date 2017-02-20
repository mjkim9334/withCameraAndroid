package com.kt.iot.mobile.ui.fragment.rawdata;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.TagStrmApi;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.TagStrm;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.data.GraphHtml;
import com.kt.iot.mobile.data.LogStream;
import com.kt.iot.mobile.service.RawdataGraphListService;
import com.kt.iot.mobile.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NP1014425901 on 2015-03-11.
 */

public class RawdataGraphWebViewFragment extends Fragment implements RawdataGraphListService.UpdateListener{

    private static final String TAG = RawdataGraphWebViewFragment.class.getSimpleName();

    public static final String ACTION_RECIVER_RAWDATA = "com.kt.iot.mobile.action.RAWDATA";
    public static final String CATEGORY_RECIVER_RAWDATA = "com.kt.iot.mobile.category.RAWDATA";

    private Device mDevice;

    private View view;
    private WebView mWb;
    private TextView mTvNodata;

    private ArrayList<LogStream> items;

    private RawdataReceiver mRawdataReceiver;
    private RawdataGraphListService service;

    private long pushUpdateTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.w(TAG, "onCreateView()!!");

        items = new ArrayList<LogStream>();

        view = inflater.inflate(R.layout.fragment_rawdata_graph_web, container, false);

        mWb = (WebView) view.findViewById(R.id.wv_rawdata_graph);
        mWb.getSettings().setJavaScriptEnabled(true);
        mWb.getSettings().setUseWideViewPort(false);

        mTvNodata = (TextView) view.findViewById(R.id.tv_rawdata_graph_nodata);

        mRawdataReceiver = new RawdataReceiver();

        return view;
    }

    public void setDevice(Device device) {
        mDevice = device;
    }

    public void refresh() {

        if(items != null && items.size()>0){
            Log.w(TAG, "refresh() items != null && items.size()>0!!");
           items.clear();
        }

        if(mDevice != null){

            if(mDevice.getTagStrmList() == null || mDevice.getTagStrmList().size() == 0){

                mWb.setVisibility(View.INVISIBLE);
                mTvNodata.setVisibility(View.VISIBLE);
                return;

            }else if(mDevice.getTagStrmList().size() > 0){

                int cnt = 0;

                for(TagStrm tag : mDevice.getTagStrmList()){
                    if(tag.getTagStrmPrpsTypeCd().equals(TagStrmApi.TAGSTRM_DATA)){
                        cnt++;
                    }
                }

                if(cnt == 0){

                    mWb.setVisibility(View.INVISIBLE);
                    mTvNodata.setVisibility(View.VISIBLE);
                    return;
                }

            }

        }

        if (service != null) {
            Log.w(TAG, "refresh() service != null so service.startPolling()!!");
            service.startPolling(mDevice);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (service == null) {
            service = new RawdataGraphListService(this);
        }
        service.addListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (service != null) {
            refresh();
        }

        IntentFilter filter = new IntentFilter(ACTION_RECIVER_RAWDATA);
        filter.addCategory(CATEGORY_RECIVER_RAWDATA);
        getActivity().registerReceiver(mRawdataReceiver, filter);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (service != null) {
            Log.w(TAG, "onPause()!! service != null so service.stopPolling!!");
            service.stopPolling();
        }

        getActivity().unregisterReceiver(mRawdataReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (service != null) {
            service.removeListener(this);
        }
    }

    @Override
    public void onUpdate(final List<LogStream> list) {

        if (list != null && list.size() > 0) {
            items.clear();
            for (LogStream logStream : list) {
                items.add(logStream);
            }

            if (view != null) {

                String strHtml = GraphHtml.getGraphHtmlString(items);

                if(strHtml == null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mWb.setVisibility(View.INVISIBLE);
                            mTvNodata.setVisibility(View.VISIBLE);
                        }
                    });

                    return;

                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mWb.setVisibility(View.VISIBLE);
                            mTvNodata.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                final File htmlFile = writeToGraphHtml(strHtml);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mWb.loadUrl("file:///" + htmlFile.getAbsolutePath());

                    }
                });
            }
        }

    }

    private File writeToGraphHtml(String data){

        File htmlFile = new File(Util.getChartHomeDir() +  "/RawDataChart.html");
        try {
            FileOutputStream fos = new FileOutputStream(htmlFile);

            fos.write(data.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlFile;

    }

    private class RawdataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "RawdataReceiver.onReceive!");

            if(intent != null){

                String strPushLog = intent.getStringExtra("msg");

                com.kt.gigaiot_sdk.data.Log log = new Gson().fromJson(strPushLog, com.kt.gigaiot_sdk.data.Log.class);

                if(items.size() > 0){

                    for(LogStream tmp : items){

                        if(tmp.getLogList().size() >= 5){
                            tmp.getLogList().clear();
                        }

                        tmp.getLogList().add(0, log);

                    }

                    long passTime = System.currentTimeMillis() - pushUpdateTime;

                    if(items.get(0).getLogList().size() == 5 && passTime >= 5000){

                        pushUpdateTime = System.currentTimeMillis();

                        if (view != null) {

                            String strHtml = GraphHtml.getGraphHtmlString(items);

                            if(strHtml == null){

                                mWb.setVisibility(View.INVISIBLE);
                                mTvNodata.setVisibility(View.VISIBLE);

                                return;

                            }else{

                                mWb.setVisibility(View.VISIBLE);
                                mTvNodata.setVisibility(View.INVISIBLE);
                            }

                            File htmlFile = writeToGraphHtml(strHtml);

                            mWb.loadUrl("file:///" + htmlFile.getAbsolutePath());

                        }

                    }



                }


            }

        }
    }

}
