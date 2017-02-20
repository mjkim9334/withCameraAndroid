package com.kt.iot.mobile.ui.activity;



import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.dashboard.DashboardFragment;
import com.kt.iot.mobile.ui.fragment.device.DeviceFragment;
import com.kt.iot.mobile.ui.fragment.device.list.DeviceListFragment;
import com.kt.iot.mobile.ui.fragment.drawer.DrawerMenuFragment;
import com.kt.iot.mobile.utils.Util;
/**
 * Created by User on 2017-01-10.
 */

public class MenuSelectActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String EXTRA_MEMBER_ID = "member_id";

    private String mMbrId;



    Button log, control, plant_type, plant_data,plant_image, plant_picture, plant_record, extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_menu_select);

        mMbrId = getIntent().getStringExtra(EXTRA_MEMBER_ID);
        log=(Button)findViewById(R.id.Log);
        log.setOnClickListener(this);

       plant_type=(Button)findViewById(R.id.plant_type);
        plant_type.setOnClickListener(this);
       /* plant_data=(Button)findViewById(R.id.plant_data);
        plant_data.setOnClickListener(this);*/
        plant_image=(Button)findViewById(R.id.plantImage);
        plant_image.setOnClickListener(this);

        plant_record=(Button)findViewById(R.id.Write);
        plant_record.setOnClickListener(this);



      /*  plant_picture=(Button)findViewById(R.id.Plant_picture);
        plant_picture.setOnClickListener(this);
*/







    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Log:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_MEMBER_ID, mMbrId);
                startActivity(intent);
                finish();


                break;


          case R.id.plant_type:

                Intent intent2 = new Intent(getApplicationContext(), CameraActivity.class);
                // intent2.putExtra(.EXTRA_MEMBER_ID, mMbrId);
                startActivity(intent2);
                //finish();
                break;


            case R.id.plantImage:   // 실시간 plant를 카메라 모튤을 이용해서 보는 것

                Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://203.252.118.95/Capture"));
                // intent2.putExtra(.EXTRA_MEMBER_ID, mMbrId);
                startActivity(intent4);
                //finish();
                break;

            case R.id.Write:

                Intent intent5 = new Intent(getApplicationContext(), RecordActivity.class);
                // intent2.putExtra(.EXTRA_MEMBER_ID, mMbrId);
                startActivity(intent5);
                //finish();
                break;

        }
    }


}
