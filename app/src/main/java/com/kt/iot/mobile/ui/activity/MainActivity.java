package com.kt.iot.mobile.ui.activity;

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
 * Created by NP1014425901 on 2015-02-26.
 */
public class MainActivity extends ActionBarActivity implements DeviceListFragment.OnDeviceListSelectedListener,
        DashboardFragment.OnSvcTgtListSelectedListener, DrawerMenuFragment.OnDrawerMenuSelectListener {

    private DrawerMenuFragment mDrawerMenuFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Device mDevice;

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_MEMBER_ID = "member_id";
    String mbrId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mbrId = getIntent().getStringExtra(EXTRA_MEMBER_ID);
      //  Toast.makeText(MainActivity.this, "여기다111111111111111111111111111", Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDrawerLayout = (DrawerLayout) inflater.inflate(R.layout.decor, null);

        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);

        FrameLayout contaniner = (FrameLayout) mDrawerLayout.findViewById(R.id.container);
        contaniner.addView(child);
        mDrawerLayout.findViewById(R.id.drawer_menu).setPadding(0, Util.getStatusBarHeight(this), 0, 0);

        decor.addView(mDrawerLayout);

        mDrawerMenuFragment = (DrawerMenuFragment) getFragmentManager().findFragmentById(R.id.drawer_menu);
        mDrawerMenuFragment.setUserName(mbrId);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.desc_open_drawer, R.string.desc_close_drawer);

        initDashboardFragment(mbrId);

        getSupportActionBar().setElevation(0);

    }

    private void initDashboardFragment(String mbrId){
        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardFragment.setMbrId(mbrId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, dashboardFragment);

        transaction.commit();
    }

    private void initializeDeviceListFragment(SvcTgt svcTgt) {

        DeviceListFragment fragment = (DeviceListFragment) getSupportFragmentManager().findFragmentById(R.id.device_list_fragment);

        if (fragment != null) {                 //태블릿

            fragment.setSvcTgt(svcTgt);
            fragment.refresh();

        } else {                                //폰

            Gson gson = new Gson();
            String strSvcTgt = gson.toJson(svcTgt, SvcTgt.class);
           // Toast.makeText(MainActivity.this, "여기다222222222222222222222222222", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
            intent.putExtra(DeviceListActivity.EXTRA_SVCTGT, strSvcTgt);

            startActivity(intent);

            /*fragment = new DeviceListFragment();
            fragment.setSvcTgtSeq(svcTgtSeq);

            fragment.setArguments(getIntent().getExtras());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);

            transaction.commit();*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      /*  MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.w(TAG, "onOptionsItemSelected!!");

        switch (item.getItemId()) {

            case android.R.id.home:
                Log.w(TAG, "onOptionsItemSelected!! id = android.R.id.home");

                if(getSupportFragmentManager().getBackStackEntryCount() > 0){

                    getSupportFragmentManager().popBackStack();
                    return true;
                }else {

                    mDrawerToggle.onOptionsItemSelected(item);
                    return true;
                }

            default :
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDeviceSelected(int position, Device device) {

        mDevice = device;

        DeviceFragment fragment = (DeviceFragment) getSupportFragmentManager().findFragmentById(R.id.device_fragment);

        if (fragment != null) {             //Tablet
            fragment.setDevice(device);
//            fragment.refresh();

        } else {                            //mobile

            Gson gson = new Gson();
            String strDevice = gson.toJson(mDevice);
           // Toast.makeText(MainActivity.this, "여기다!!!!!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
            // 여기는 안 되는 거 같음!!
            Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
            intent.putExtra(DeviceActivity.EXTRA_DEVICE, strDevice);

            startActivity(intent);

            /*fragment = new DeviceFragment();
            fragment.setDevice(device);

            fragment.setArguments(getIntent().getExtras());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);

            transaction.commit();*/

        }
    }

    @Override
    public void onSvcTgtSelected(SvcTgt svcTgt) {
        Log.i(TAG, "onSvcTgtSelected! svcTgtSeq = " + svcTgt);
        initializeDeviceListFragment(svcTgt);
    }

    @Override
    public void onBackPressed() {

        Log.i(TAG, "onBackPressed()!");

        if(getSupportFragmentManager().getBackStackEntryCount() > 0){

            getSupportFragmentManager().popBackStack();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onMenuSelected(int menuIndex) {

        switch(menuIndex){
            case DrawerMenuFragment.MENU_INDEX_SETTINS: {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
                break;

            case DrawerMenuFragment.MENU_INDEX_POLICY: {
                Intent intent = new Intent(getApplicationContext(), PolicyActivity.class);
                startActivity(intent);
            }
                break;

            case DrawerMenuFragment.MENU_INDEX_GUIDE: {
                Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
                startActivity(intent);
            }
            break;

            case DrawerMenuFragment.MENU_INDEX_LIST: {
                Intent intent=new Intent(getApplicationContext(), MenuSelectActivity.class);
                intent.putExtra(MenuSelectActivity.EXTRA_MEMBER_ID, mbrId);
                startActivity(intent);
                finish();
            }
            break;
        }

    }
}
