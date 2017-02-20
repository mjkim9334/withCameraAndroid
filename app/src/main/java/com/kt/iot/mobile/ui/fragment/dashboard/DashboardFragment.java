package com.kt.iot.mobile.ui.fragment.dashboard;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.kt.gigaiot_sdk.PushApi;
import com.kt.gigaiot_sdk.SvcTgtApi;
import com.kt.gigaiot_sdk.data.PushTypePair;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.gigaiot_sdk.data.SvcTgtApiResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.BitmapUtil;
import com.kt.iot.mobile.utils.Common;
import com.kt.iot.mobile.utils.ModifyDeviceMgr;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 13..
 */
public class DashboardFragment extends Fragment implements AdapterView.OnItemClickListener {

    private String TAG = this.getClass().getSimpleName();

    private String mMbrId;
    private ArrayList<SvcTgt> mArraySvcTgt;
    private SvcTgtListAdapter mListAdapter;

    private ListView mlvSvcTgt;

    //private View mFootAddview;

    private ImageView mIvMainBg;
    private String mBgImgPath;
    private int mBgRsrcId;

    public interface OnSvcTgtListSelectedListener {
        void onSvcTgtSelected(SvcTgt svcTgt);
    }

    private OnSvcTgtListSelectedListener mCallback;


    public void setMbrId(String mbrId){
        mMbrId = mbrId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, null);
        mlvSvcTgt = (ListView) view.findViewById(R.id.lv_svctgt);
        mlvSvcTgt.setOnItemClickListener(this);

        mIvMainBg = (ImageView) view.findViewById(R.id.iv_dashboard_bg);

        /*mFootAddview = inflater.inflate(R.layout.itemview_footer_add, null);
        ((TextView) mFootAddview.findViewById(R.id.tv_foot_add)).setText(getString(R.string.item_add_service));

        mlvSvcTgt.addFooterView(mFootAddview);*/

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (OnSvcTgtListSelectedListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnSvcTgtListSelectedListener");
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new GetSvcTgtTask().execute();

       /* mArraySvcTgt = new ArrayList<>();
        SvcTgt svcTgt = new SvcTgt();
        svcTgt.setSvcTgtSeq("1000000573");
        mArraySvcTgt.add(svcTgt);

        mListAdapter = new SvcTgtListAdapter(getActivity(), mArraySvcTgt);
        mlvSvcTgt.setAdapter(mListAdapter);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "onOptionsItemSelected!!");

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.homemenu);

        ((ActionBarActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_custom_logo);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);

        setBackground();

        ModifyDeviceMgr.setModifyDevice(null);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick position = " + position);

        mCallback.onSvcTgtSelected(mArraySvcTgt.get(position));

        /*if(position < mArraySvcTgt.size())
            mCallback.onSvcTgtSelected(mArraySvcTgt.get(position));
        else{
            //TODO : FooterView 선택 시 동작 정의
            Toast.makeText(getActivity(), "서비스 추가가 선택되었습니다.", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void setBackground(){

        String bgImgPath = ApplicationPreference.getInstance().getPrefBgUserCustom();
        int bgRsrcId = ApplicationPreference.getInstance().getPrefBgDefaultId();

        if(mBgImgPath != null && mBgImgPath.equals(bgImgPath) && mBgRsrcId == bgRsrcId){
            return;
        }

        if(bgImgPath != null && bgImgPath.equals("") == false){

            mIvMainBg.setImageBitmap(BitmapFactory.decodeFile(bgImgPath));
            mBgImgPath = bgImgPath;
            mBgRsrcId = bgRsrcId;
            return;

        }

        mIvMainBg.setImageBitmap(BitmapUtil.getBitmapFromResource(getResources(), bgRsrcId, 2048, 2048));
        mBgImgPath = bgImgPath;
        mBgRsrcId = bgRsrcId;

    }

    private class GetSvcTgtTask extends AsyncTask<Void, Void, SvcTgtApiResponse>{

        @Override
        protected SvcTgtApiResponse doInBackground(Void... params) {

            try{
                SvcTgtApi svcTgtApi = new SvcTgtApi(ApplicationPreference.getInstance().getPrefAccessToken());
                return svcTgtApi.getSvcTgtSeqList(mMbrId);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(SvcTgtApiResponse result) {


            if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                Log.w(TAG, "&&&&&&&&&&&&&&&&&&&&&&GOOD!!!!!!!!!!!!&&&&&&&&&&&&&&&&&&");

                mArraySvcTgt = result.getSvcTgts();

                mListAdapter = new SvcTgtListAdapter(getActivity(), mArraySvcTgt);
                mlvSvcTgt.setAdapter(mListAdapter);

                new PushSessionRegTask().execute();

            } else if (result != null && result.getResponseCode().equals(ApiConstants.CODE_NG) && result.getMessage().equals("Unauthorized")) {
                Log.w(TAG, "!!!!!!!!!!!!&&&&&&&&&&&&&&&&&&");
                ApplicationPreference.getInstance().setPrefAccountId("");
                new PushSessionDeleteTask().execute();                      //Push Session 등록해제

                getActivity().setResult(getActivity().RESULT_OK);
                getActivity().finish();
            } else {
                // todo
            }

        }
    }

    private class PushSessionRegTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PushTypePair> pushTypePairs = new ArrayList<>();
//            PushTypePair pair = new PushTypePair(mArraySvcTgt.get(0).getSvcTgtSeq(), PushApi.PUSH_MSG_TYPE_COLLECT);
            pushTypePairs.add(new PushTypePair(mArraySvcTgt.get(0).getSvcTgtSeq(), PushApi.PUSH_MSG_TYPE_COLLECT));
            pushTypePairs.add(new PushTypePair(mArraySvcTgt.get(0).getSvcTgtSeq(), PushApi.PUSH_MSG_TYPE_OUTBREAK));

            PushApi pushApi = new PushApi(ApplicationPreference.getInstance().getPrefAccessToken());
            pushApi.gcmSessionRegistration(ApplicationPreference.getInstance().getPrefAccountMbrSeq(),
                    Common.CLIENT_ID,
                    ApplicationPreference.getInstance().getPrefGcmRegId(),
                    pushTypePairs);

            Log.w(TAG, "&&&& PUSHAPI48 &&&&&&&&"+ApplicationPreference.getInstance().getPrefAccountMbrSeq()+" and "+  Common.CLIENT_ID + " and "+  ApplicationPreference.getInstance().getPrefGcmRegId() + "and"+ pushTypePairs);

            return null;
        }
    }

    private class PushSessionDeleteTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            PushApi pushApi = new PushApi(ApplicationPreference.getInstance().getPrefAccessToken());
            pushApi.gcmSessionDelete(ApplicationPreference.getInstance().getPrefGcmRegId());
            return null;
        }
    }



}
