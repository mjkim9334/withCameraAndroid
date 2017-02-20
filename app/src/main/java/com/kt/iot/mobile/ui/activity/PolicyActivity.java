package com.kt.iot.mobile.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.policy.PolicyFragment;

/**
 * Created by ceoko on 15. 6. 16..
 */
public class PolicyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_common);

        PolicyFragment fragment = new PolicyFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, fragment).commit();

        getSupportActionBar().setElevation(0);

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


}
