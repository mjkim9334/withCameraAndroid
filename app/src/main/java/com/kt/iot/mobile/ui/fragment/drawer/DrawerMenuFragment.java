package com.kt.iot.mobile.ui.fragment.drawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.activity.AccountActivity;
import com.kt.iot.mobile.ui.activity.LoginActivity;
import com.kt.iot.mobile.ui.activity.MenuSelectActivity;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 14..
 */
public class DrawerMenuFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final int MENU_INDEX_GUIDE = 0;
    public static final int MENU_INDEX_POLICY = 1;
    public static final int MENU_INDEX_SETTINS = 2;
    public static final int MENU_INDEX_LIST=3;

    private final int REQ_LOGOUT = 0;

    private String[] mArrMenuNames;
    private final int[] MENU_ICON_RSRC_IDS = {R.drawable.user, R.drawable.user, R.drawable.user, R.drawable.user};

    private ArrayList<DrawerMenu> mArrayMenus = new ArrayList<>();

    TextView mTvUserName;
    ListView mLvMenu;



    private OnDrawerMenuSelectListener mCallback;

    public interface OnDrawerMenuSelectListener {
        void onMenuSelected(int menuIndex);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArrMenuNames = getResources().getStringArray(R.array.drawer_menu);
        makeMenuItems();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drawer_menu, container, false);
        mTvUserName = (TextView) view.findViewById(R.id.tv_drawer_username);
        mTvUserName.setTypeface(GiGaIotApplication.getDefaultTypeFace());
        mTvUserName.setOnClickListener(this);



        mLvMenu = (ListView) view.findViewById(R.id.lv_drawer_menu);

        mLvMenu.setAdapter(new DrawerMenuListAdapter(getActivity(), mArrayMenus));
        mLvMenu.setOnItemClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.tv_drawer_username:

                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivityForResult(intent, REQ_LOGOUT);

                break;



        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (OnDrawerMenuSelectListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnDrawerMenuSelectListener");
        }

    }

    public void setUserName(String userName){
        mTvUserName.setText(userName);
    }

    void makeMenuItems(){
        for(int i=0; i<mArrMenuNames.length; i++){
            mArrayMenus.add(new DrawerMenu(mArrMenuNames[i], MENU_ICON_RSRC_IDS[i]));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.onMenuSelected(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != getActivity().RESULT_OK){

            return;
        }

        switch(requestCode){

            case REQ_LOGOUT:
            {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            break;

        }

    }
}
