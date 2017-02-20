package com.kt.iot.mobile.ui.fragment.settings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kt.gigaiot_sdk.MemberApi;
import com.kt.gigaiot_sdk.PushApi;
import com.kt.gigaiot_sdk.data.MemberApiResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;

/**
 * Created by ceoko on 15. 5. 11..
 */
public class AccountFragment extends Fragment implements View.OnClickListener {


    private TextView mEtUserName, mEtUserPhone, mEtUserEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, null);

        RelativeLayout layoutLogout = (RelativeLayout) view.findViewById(R.id.layout_linear_account_logout);
        layoutLogout.setOnClickListener(this);

        mEtUserName = (TextView) view.findViewById(R.id.et_account_username);
        mEtUserPhone = (TextView) view.findViewById(R.id.et_account_phone);
        mEtUserEmail = (TextView) view.findViewById(R.id.et_account_mail);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new GetMemberInfoTask().execute();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.layout_linear_account_logout:

                ApplicationPreference.getInstance().setPrefAccountId("");
                new PushSessionDeleteTask().execute();                      //Push Session 등록해제

                getActivity().setResult(getActivity().RESULT_OK);
                getActivity().finish();

                break;
        }

    }

    private class GetMemberInfoTask extends AsyncTask<Void, Void, MemberApiResponse>{

        @Override
        protected MemberApiResponse doInBackground(Void... params) {

            MemberApi memberApi = new MemberApi(ApplicationPreference.getInstance().getPrefAccessToken());
            MemberApiResponse response = memberApi.getMemberInfo(ApplicationPreference.getInstance().getPrefAccountMbrSeq());

            return response;
        }

        @Override
        protected void onPostExecute(MemberApiResponse result) {

            if(result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)){

                mEtUserName.setText(result.getMember().getUserNm());
                mEtUserEmail.setText(result.getMember().getEmail());
                mEtUserPhone.setText(result.getMember().getTelNo());

            }

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
