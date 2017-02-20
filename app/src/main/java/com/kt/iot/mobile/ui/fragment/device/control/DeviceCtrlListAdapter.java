package com.kt.iot.mobile.ui.fragment.device.control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.gigaiot_sdk.TagStrmApi;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.TagStrm;
import com.kt.gigaiot_sdk.data.TagStrmApiResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 13..
 */
public class DeviceCtrlListAdapter extends BaseAdapter{

    private final String TAG = DeviceCtrlListAdapter.class.getSimpleName();

    Context mContext;
    ArrayList<TagStrm> mData;
    Device mDevice;

    Handler mHandler;

    public DeviceCtrlListAdapter(Context context, ArrayList<TagStrm> data, Device device){
        mContext = context;
        mData = data;
        mDevice = device;
        mHandler = new Handler();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ItemHolder holder;

        if(row == null){

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.itemview_ctrl, parent, false);

            holder = new ItemHolder();
            holder.tvCtrlName = (TextView) row.findViewById(R.id.tv_ctrl_tagname);
            //holder.etCtrlMsg = (EditText) row.findViewById(R.id.et_ctrl_msg);
            holder.btSendMsg=(Button) row.findViewById(R.id.bt_ctrl_send);

            holder.tvCtrlName.setTypeface(GiGaIotApplication.getDefaultTypeFace());
            //holder.etCtrlMsg.setTypeface(GiGaIotApplication.getDefaultTypeFace());
            holder.btSendMsg.setTypeface(GiGaIotApplication.getDefaultTypeFace());
            holder.btSendMsg.setOnClickListener(mListener);
            row.setTag(holder);
        }

        holder = (ItemHolder) row.getTag();

        holder.tvCtrlName.setText(mData.get(position).getTagStrmId());
        //holder.btSendMsg.setTag(new CtrlTag(holder.etCtrlMsg, position));
        holder.btSendMsg.setTag(new CtrlTag(null, position));

        return row;
    }

    private class ItemHolder{
        TextView tvCtrlName;
        //EditText etCtrlMsg;
        Button btSendMsg;
    }

    private class CtrlTag{

        private EditText etCtrlMsg;
        private int position;

        public CtrlTag(EditText et, int position){
            etCtrlMsg = et;
            this.position = position;
        }

        public EditText getEtCtrlMsg() {
            return etCtrlMsg;
        }

        public int getPosition() {
            return position;
        }
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CtrlTag tag = (CtrlTag)v.getTag();
            //Log.i(TAG, "onClick position = " + tag.getPosition() + "ctrl msg = " + tag.getEtCtrlMsg().getText().toString());
            Log.i(TAG, "onClick position = " + tag.getPosition());
            createSendCtrlMsgDialog(tag.getPosition());
            //Toast.makeText(mContext, "제어 요청을 성공했습니다", Toast.LENGTH_LONG).show();
        }
    };

    private void createSendCtrlMsgDialog(final int position){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dilogView = inflater.inflate(R.layout.dialog_device_ctrl, null);
        final EditText etCtrlMsg = (EditText) dilogView.findViewById(R.id.et_device_ctrl);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("제어요청 보내기")
                .setView(dilogView)
                .setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String ctrlMsg = etCtrlMsg.getText().toString();

                        Log.d(TAG, "Dialog onClick ctrlMsg = " + ctrlMsg + " | tagStrmId=" + mData.get(position).getTagStrmId());
                        //TODO : 제어요청 API 호출

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                TagStrmApi tagStrmApi = new TagStrmApi(ApplicationPreference.getInstance().getPrefAccessToken());
                                final TagStrmApiResponse response = tagStrmApi.sendCtrlMsg(mDevice.getSvcTgtSeq(), mDevice.getSpotDevSeq(), mDevice.getSpotDevId(), mDevice.getGwCnctId(),
                                        mData.get(position).getTagStrmId(), mData.get(position).getTagStrmValTypeCd(),
                                        ApplicationPreference.getInstance().getPrefAccountId(), ctrlMsg);


                                if(response.getResponseCode().equals(ApiConstants.CODE_OK)){

                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, "제어 요청이 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }else if(response.getResponseCode().equals(ApiConstants.CODE_NG)){

                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, "제어 요청이 실패하였습니다.\n[" + response.getMessage() + "]", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }


                            }
                        }).start();





                        /*try {

                            JSONObject requestObj = new JSONObject();
                            requestObj.put("mbrId", ApplicationPreference.getInstance().getPrefAccountId());
                            requestObj.put("spotDevId", mDevice.getSpotDevId());
                            requestObj.put("tagStrmValTypeCd", mData.get(position).getTagStrmValTypeCd());
                            requestObj.put("gwCnctId", mDevice.getGwCnctId());
                            requestObj.put("tagStrmId", mData.get(position).getTagStrmId());
                            requestObj.put("ctrlMsg", ctrlMsg);

                            String requestJson = requestObj.toString();

                            Log.d(TAG, "Dialog onClick requestJson = " + requestJson);

                        }catch (Exception e){

                        }*/



                    }
                })
                .setNegativeButton(R.string.common_cancel, null);


        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
