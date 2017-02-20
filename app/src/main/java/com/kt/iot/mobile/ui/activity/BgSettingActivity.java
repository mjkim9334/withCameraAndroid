package com.kt.iot.mobile.ui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.settings.BgSettingFragment;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.BitmapUtil;
import com.kt.iot.mobile.utils.Util;

/**
 * Created by ceoko on 15. 4. 29..
 */
public class BgSettingActivity extends ActionBarActivity {

    private final String TAG = BgSettingActivity.class.getSimpleName();

    ImageView mIvBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_common);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, new BgSettingFragment()).commit();

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setCustomView(Util.getCustomTitleView(this, getResources().getString(R.string.actionbar_title_setting_bg)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.pre);

        mIvBg = (ImageView) findViewById(R.id.iv_common_bg);
        setBackground();
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

    private void setBackground(){

        String bgImgPath = ApplicationPreference.getInstance().getPrefBgUserCustom();
        int bgRsrcId = ApplicationPreference.getInstance().getPrefBgDefaultId();

        if(bgImgPath != null && bgImgPath.equals("") == false){

            mIvBg.setImageBitmap(BitmapFactory.decodeFile(bgImgPath));
            return;

        }

//        mIvBg.setImageResource(bgRsrcId);
        mIvBg.setImageBitmap(BitmapUtil.getBitmapFromResource(getResources(), bgRsrcId, 2048, 2048));

    }

}
