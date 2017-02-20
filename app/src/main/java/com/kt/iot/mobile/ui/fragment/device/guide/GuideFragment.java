package com.kt.iot.mobile.ui.fragment.device.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kt.iot.mobile.android.R;

/**
 * Created by ceoko on 15. 6. 16..
 */
public class GuideFragment extends Fragment {

    private int mIndex;

    private final int[] RSRC_GUIDE_SCREENSHOT = {R.drawable.guide_01, R.drawable.guide_02, R.drawable.guide_03, R.drawable.guide_04};
    private final int[] RSRC_GUIDE_TEXT = {R.string.guide_01, R.string.guide_02, R.string.guide_03, R.string.guide_04};

    public static GuideFragment newInstance(int index){

        GuideFragment fragment = new GuideFragment();
        fragment.mIndex = index;

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_guide, container, false);

        ImageView ivMain = (ImageView) v.findViewById(R.id.iv_guide_main);
        TextView tvGuide = (TextView) v.findViewById(R.id.tv_guide_desc);

        ivMain.setImageResource(RSRC_GUIDE_SCREENSHOT[mIndex]);
        tvGuide.setText(RSRC_GUIDE_TEXT[mIndex]);

        return v;
    }
}
