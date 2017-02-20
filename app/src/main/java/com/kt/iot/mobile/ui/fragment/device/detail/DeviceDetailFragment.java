package com.kt.iot.mobile.ui.fragment.device.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kt.gigaiot_sdk.DeviceApi;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.DeviceApiResponse;
import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;

/**
 * Created by NP1014425901 on 2015-02-26.
 */
public class DeviceDetailFragment extends Fragment {

    private final String TAG = DeviceDetailFragment.class.getSimpleName();

    private Device device;

    private View mView;
    private TextView mTvDeviceName;
    private ImageView mIvOnline;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_device_detail, container, false);

        mTvDeviceName = (TextView) mView.findViewById(R.id.title);
        mTvDeviceName.setTypeface(GiGaIotApplication.getDefaultTypeFace());

        mIvOnline = (ImageView) mView.findViewById(R.id.iv_device_detail_online);

        refresh();

        return mView;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void refresh() {
        if (mView != null) {
            if (device != null) {
                if (device.getDevNm() != null) {

                    if (mTvDeviceName != null) {
                        mTvDeviceName.setText(device.getDevNm());
                    }
                }

            }
        }
    }

}
