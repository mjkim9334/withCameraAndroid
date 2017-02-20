package com.kt.iot.mobile.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;

/**
 * Created by ceoko on 15. 5. 12..
 */
public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginState();
            }
        }, 2000);

    }


    private void checkLoginState(){

        String id = ApplicationPreference.getInstance().getPrefAccountId();

        if(id != null && id.equals("") == false){

            Intent intent = new Intent(IntroActivity.this, MenuSelectActivity.class);
            intent.putExtra(MenuSelectActivity.EXTRA_MEMBER_ID, id);
            startActivity(intent);
            finish();

        }else {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
