package com.kt.iot.mobile.ui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.settings.AccountFragment;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.BitmapUtil;
import com.kt.iot.mobile.utils.Util;

/**
 * Created by ceoko on 15. 5. 8..
 */
public class AccountActivity extends ActionBarActivity{

    private final String TAG = AccountActivity.class.getSimpleName();

    private ImageView mIvBg;
    private String mBgImgPath;
    private int mBgRsrcId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_common);

        mIvBg = (ImageView) findViewById(R.id.iv_common_bg);

        getSupportActionBar().setCustomView(Util.getCustomTitleView(this, getString(R.string.actionbar_title_setting_account)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.pre);


        AccountFragment fragment = new AccountFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, fragment).commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();
                return true;
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

//        mIvBg.setImageResource(bgRsrcId);
        mIvBg.setImageBitmap(BitmapUtil.getBitmapFromResource(getResources(), bgRsrcId, 2048, 2048));
        mBgImgPath = bgImgPath;
        mBgRsrcId = bgRsrcId;

    }
}
