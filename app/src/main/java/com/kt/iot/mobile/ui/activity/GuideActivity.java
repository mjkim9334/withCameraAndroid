package com.kt.iot.mobile.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.device.guide.GuideFragment;
import com.kt.iot.mobile.ui.fragment.device.guide.GuideViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 6. 16..
 */
public class GuideActivity extends FragmentActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);


        ArrayList<Fragment> arrayFragments = new ArrayList<>();

        ImageView ivClose = (ImageView) findViewById(R.id.iv_guide_close);
        ivClose.setOnClickListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_pager);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.guide_indicator);

        arrayFragments.add(GuideFragment.newInstance(0));
        arrayFragments.add(GuideFragment.newInstance(1));
        arrayFragments.add(GuideFragment.newInstance(2));
        arrayFragments.add(GuideFragment.newInstance(3));

        GuideViewPagerAdapter adapter = new GuideViewPagerAdapter(getSupportFragmentManager(), arrayFragments);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        indicator.setViewPager(viewPager);

        viewPager.setCurrentItem(0);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_guide_close:

                finish();
                break;
        }

    }
}
