package com.kt.iot.mobile.ui.fragment.device.guide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 6. 16..
 */
public class GuideViewPagerAdapter extends FragmentPagerAdapter {


    private ArrayList<Fragment> mArrayPageFragments;

    public GuideViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> pages) {
        super(fm);

        mArrayPageFragments = pages;
    }

    @Override
    public Fragment getItem(int position) {

        return mArrayPageFragments.get(position);
    }

    @Override
    public int getCount() {
        return mArrayPageFragments.size();
    }


}
