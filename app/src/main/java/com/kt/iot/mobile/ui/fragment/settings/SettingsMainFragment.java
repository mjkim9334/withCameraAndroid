package com.kt.iot.mobile.ui.fragment.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.activity.BgSettingActivity;

/**
 * Created by ceoko on 15. 4. 29..
 */
public class SettingsMainFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private final String TAG = SettingsMainFragment.class.getSimpleName();

    public static final int REQ_BG_CHANGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Preference prefBg = findPreference("pref_theme_bg");
        prefBg.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {


        if(preference.getKey().equals("pref_theme_bg")){

            Intent intent = new Intent(getActivity(), BgSettingActivity.class);
            getActivity().startActivity(intent);
        }

        return false;
    }
}
