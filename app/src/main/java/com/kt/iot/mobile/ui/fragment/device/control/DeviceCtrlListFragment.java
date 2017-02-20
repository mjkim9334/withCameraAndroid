package com.kt.iot.mobile.ui.fragment.device.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.TagStrm;
import com.kt.iot.mobile.android.R;

import java.util.ArrayList;

/**
 * Created by NP1014425901 on 2015-02-26.
 */
public class DeviceCtrlListFragment extends ListFragment {

    private Device mDevice;
    private DeviceCtrlListAdapter mAdapter;
    private ArrayList<TagStrm> mArrayCtrlTags;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(getActivity().getResources().getString(R.string.msg_ctrl_tag_none));
        getListView().setBackgroundResource(R.drawable.bg_gradient_right);
        refresh();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setDevice(Device device){
        mDevice = device;
    }

    public void refresh(){
        if(mDevice != null && mDevice.getTagStrmList() != null){

            mArrayCtrlTags = new ArrayList<>();

            for(TagStrm tag : mDevice.getTagStrmList()){

                if(tag.getTagStrmPrpsTypeCd().equals("0000020")){
                    mArrayCtrlTags.add(tag);
                }

            }

            if(mArrayCtrlTags.size()>0){
                //mAdapter = new DeviceCtrlListAdapter(getActivity().getApplicationContext(), mArrayCtrlTags);
                mAdapter = new DeviceCtrlListAdapter(getActivity(), mArrayCtrlTags, mDevice);
                setListAdapter(mAdapter);
            }else{
                setListAdapter(null);
            }
            
        }
    }
}
