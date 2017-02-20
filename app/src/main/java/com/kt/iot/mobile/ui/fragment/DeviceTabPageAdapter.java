package com.kt.iot.mobile.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 16..
 */
public class DeviceTabPageAdapter extends FragmentPagerAdapter {

    String[] mTitles;
    private ArrayList<Fragment> mArrayPageFragments;

    public DeviceTabPageAdapter(FragmentManager fm, String[] titles, ArrayList<Fragment> pages) {
        super(fm);

        mTitles = titles;
        mArrayPageFragments = pages;

    }

    @Override
    public Fragment getItem(int position) {

        return mArrayPageFragments.get(position);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return mTitles[position % mTitles.length].toUpperCase();
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

}

