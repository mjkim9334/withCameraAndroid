package com.kt.iot.mobile.ui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.settings.SettingsMainFragment;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.BitmapUtil;
import com.kt.iot.mobile.utils.Util;

/**
 * Created by NP1014425901 on 2015-03-12.
 */
public class SettingActivity extends ActionBarActivity {

    private final String TAG = SettingActivity.class.getSimpleName();

    private ImageView mIvBg;
    private String mBgImgPath;
    private int mBgRsrcId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_common);

        getFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, new SettingsMainFragment()).commit();

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setCustomView(Util.getCustomTitleView(this, getResources().getString(R.string.actionbar_title_settings)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.pre);

        mIvBg = (ImageView) findViewById(R.id.iv_common_bg);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case android.R.id.home:

                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setBackground();
    }

    private void setBackground(){

        String bgImgPath = ApplicationPreference.getInstance().getPrefBgUserCustom();
        int bgRsrcId = ApplicationPreference.getInstance().getPrefBgDefaultId();

        if(mBgImgPath != null && mBgImgPath.equals(bgImgPath) && mBgRsrcId == bgRsrcId){
            return;
        }

        if(bgImgPath != null && bgImgPath.equals("") == false){

            mIvBg.setImageBitmap(BitmapFactory.decodeFile(bgImgPath));
            mBgImgPath = bgImgPath;
            mBgRsrcId = bgRsrcId;
            return;

        }

        mIvBg.setImageBitmap(BitmapUtil.getBitmapFromResource(getResources(), bgRsrcId, 2048, 2048));
        mBgImgPath = bgImgPath;
        mBgRsrcId = bgRsrcId;

    }

}
