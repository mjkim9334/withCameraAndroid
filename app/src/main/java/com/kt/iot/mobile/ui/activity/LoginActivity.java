package com.kt.iot.mobile.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kt.gigaiot_sdk.GigaIotOAuth;
import com.kt.gigaiot_sdk.data.GiGaIotOAuthResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Blur;
import com.kt.iot.mobile.utils.BitmapUtil;
import com.kt.iot.mobile.utils.Common;

import java.io.IOException;

/**
 * Created by NP1014425901 on 2015-03-18.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private final String TAG = LoginActivity.class.getSimpleName();
    private EditText mEtId, mEtPw;

    private ImageView mIvBg;
    private String mBgImgPath;
    private int mBgRsrcId;

    private GoogleCloudMessaging mGcm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mEtId = (EditText) findViewById(R.id.et_login_id);
        mEtPw = (EditText) findViewById(R.id.et_login_pw);
        mEtId.setTypeface(GiGaIotApplication.getDefaultTypeFace());
        mEtPw.setTypeface(GiGaIotApplication.getDefaultTypeFace());

        //mEtId.setText("irisda");        //test

        Button ivLogin = (Button) findViewById(R.id.iv_login_bt);
        /*ImageView ivFindId = (ImageView) findViewById(R.id.iv_login_bt_findid);
        ImageView ivFindPw = (ImageView) findViewById(R.id.iv_login_bt_findpw);*/

        ivLogin.setOnClickListener(this);
        /*ivFindId.setOnClickListener(this);
        ivFindPw.setOnClickListener(this);*/

        /*TextView tvFindIdTitle = (TextView) findViewById(R.id.tv_login_findid_title);
        TextView tvFindPwTitle = (TextView) findViewById(R.id.tv_login_findpw_title);
        tvFindIdTitle.setTypeface(GiGaIotApplication.getDefaultTypeFace());
        tvFindPwTitle.setTypeface(GiGaIotApplication.getDefaultTypeFace());*/

        mIvBg = (ImageView) findViewById(R.id.iv_login_bg);

        setBackground();
    }


    private void startMainActivity(String id) {
        Intent intent = new Intent(getApplicationContext(), MenuSelectActivity.class);
        intent.putExtra(MenuSelectActivity.EXTRA_MEMBER_ID, id);
        startActivity(intent);
        finish();
    }

    private void setBackground(){ // 배경 화면 지정해주는 것

        String bgImgPath = ApplicationPreference.getInstance().getPrefBgUserCustom();
        int bgRsrcId = ApplicationPreference.getInstance().getPrefBgDefaultId();

        if(bgImgPath != null && bgImgPath.equals("") == false){

            Bitmap bitmap = BitmapFactory.decodeFile(bgImgPath);

            if(bitmap == null){
                ApplicationPreference.getInstance().setPrefBgUserCustom("");
                ApplicationPreference.getInstance().setPrefBgDefaultId(R.drawable.bg_01);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_01);
            }

            mIvBg.setImageBitmap(Blur.fastblur(this, bitmap, 14));
            return;

        }

        Bitmap bitmap = BitmapUtil.getBitmapFromResource(getResources(), bgRsrcId, 2048, 2048);
        mIvBg.setImageBitmap(Blur.fastblur(this, bitmap, 14));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.iv_login_bt:

                String id = mEtId.getText().toString();
                String pw = mEtPw.getText().toString();

                if(TextUtils.isEmpty(id)){
                    Toast.makeText(LoginActivity.this, R.string.login_id_empty, Toast.LENGTH_SHORT).show();
                    return;

                }else if(TextUtils.isEmpty(pw)){
                    Toast.makeText(LoginActivity.this, R.string.login_pw_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                new LoginTask().execute();

                break;

        }
    }


    private class LoginTask extends AsyncTask<Void, Void, GiGaIotOAuthResponse>{

        ProgressDialog progressDialog;

        String id;

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getString(R.string.common_wait), true, false);
        }

        @Override
        protected GiGaIotOAuthResponse doInBackground(Void... params) {

            id = mEtId.getText().toString();
            String pw = mEtPw.getText().toString();

            //테스트용
            GigaIotOAuth gigaIotOAuth = new GigaIotOAuth(Common.CLIENT_ID, Common.CLIENT_SECRET);

            GiGaIotOAuthResponse response = gigaIotOAuth.loginWithPassword(id, pw);

            return response;
        }

        @Override
        protected void onPostExecute(GiGaIotOAuthResponse result) {
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
                progressDialog = null;
            }

            if(result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)){
                ApplicationPreference.getInstance().setPrefAccountId(id);

                ApplicationPreference.getInstance().setPrefAccessToken(result.getAccessToken()); // 받은 access token 저장
                Log.w(TAG, ApplicationPreference.getInstance().getPrefAccessToken());



                ApplicationPreference.getInstance().setPrefAccountMbrSeq(result.getMbrSeq()); // 서비스 대상 일련 번호 받음?

                Log.w(TAG, ApplicationPreference.getInstance().getPrefAccountMbrSeq());

                startMainActivity(id); // 그 아애디를 전달하여 mainactivity 실행

                //TODO :GCM 등록 테스트
                mGcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                //mGcm.register("772329232378");

                new GetGcmRegIdTask().execute();

            }else{


                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_fail) + result.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class GetGcmRegIdTask extends AsyncTask<Void, Void, String>{


        @Override
        protected String doInBackground(Void... params) {

            String regId = null;

            try {
                //regId = mGcm.register("772329232378");
                regId = mGcm.register("371742022785");
          //  regId = mGcm.register("885794332483");

                Log.w(TAG, "GetGcmRegIdTask regId = " + regId);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ApplicationPreference.getInstance().setPrefGcmRegId(regId);

            return regId;
        }

    }


}