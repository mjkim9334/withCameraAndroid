package com.kt.iot.mobile.ui.fragment.device;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kt.gigaiot_sdk.data.Device;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.DeviceTabPageAdapter;
import com.kt.iot.mobile.ui.fragment.event.list.EventListFragment;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 16..
 */
public class DeviceTabFragment extends Fragment {

    private final String TAG = DeviceTabFragment.class.getSimpleName();

    private ArrayList<Fragment> mArrayFragments = new ArrayList<>();
    private String[] mArrayPageTitles = {"Event", "Log"};

    private EventListFragment mEventListFragment;

    private Device mDevice;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_tab, null);

        mEventListFragment = new EventListFragment();
        mArrayFragments.add(mEventListFragment);

        DeviceTabPageAdapter adapter = new DeviceTabPageAdapter(getFragmentManager(), mArrayPageTitles, mArrayFragments);

        ViewPager pager = (ViewPager) view.findViewById(R.id.device_pager);
        //pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.device_tab_indicator);
        indicator.setViewPager(pager);

        pager.setCurrentItem(0);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy()!!");

        getActivity().getSupportFragmentManager().beginTransaction().remove(mEventListFragment).commit();
    }

    public void setDevice(final Device device) {
        if (device != null) {
            mDevice = device;

            if(mEventListFragment != null){
                mEventListFragment.setDevice(mDevice);
            }

            //refresh();
        }
    }

    public void refresh() {

        if(mEventListFragment != null) {
            mEventListFragment.refresh();
        }
    }
}
