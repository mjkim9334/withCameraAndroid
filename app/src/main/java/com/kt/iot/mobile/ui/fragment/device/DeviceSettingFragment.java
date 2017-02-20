package com.kt.iot.mobile.ui.fragment.device;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kt.gigaiot_sdk.DeviceApi;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.DeviceApiResponse;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.ModifyDeviceMgr;
import com.kt.iot.mobile.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by ceoko on 15. 5. 4..
 */
public class DeviceSettingFragment extends Fragment implements View.OnClickListener {

    private final String TAG = DeviceSettingFragment.class.getSimpleName();

    private final int REQ_PICK_FROM_CAMERA = 0;
    private final int REQ_PICK_FROM_ALBUM = 1;
    private final int REQ_CROP_IMAGE = 2;

    private ImageView mIvMain, mIvImageSetting;
    private EditText mEtDevNm, mEtSpotDevId, mEtDevModelNm, mEtDevManufacturer;
    private Button mBtModify;

    private Uri mImageUri;

    private Device mDevice;
    private SvcTgt mSvcTgt;

    private DisplayImageOptions mOptions;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_device_setting, container, false);

        mIvMain = (ImageView) v.findViewById(R.id.iv_device_setting_main);
        mIvImageSetting = (ImageView) v.findViewById(R.id.iv_device_setting_camera);
        mIvImageSetting.setOnClickListener(this);

        mEtDevNm = (EditText) v.findViewById(R.id.et_device_setting_devnm);
        mEtSpotDevId = (EditText) v.findViewById(R.id.et_device_setting_spotdev_id);
        mEtDevModelNm = (EditText) v.findViewById(R.id.et_device_setting_dev_model);
        mEtDevManufacturer = (EditText) v.findViewById(R.id.et_device_setting_manufacturer);

        mBtModify = (Button) v.findViewById(R.id.bt_device_setting_modify);
        mBtModify.setOnClickListener(this);

        mEtDevNm.setText(mDevice.getDevNm());
        mEtSpotDevId.setText(mDevice.getSpotDevId());
        mEtDevModelNm.setText(mDevice.getDevModelNm());
        mEtDevManufacturer.setText(mDevice.getTermlMakrNm());

        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.white_solid)
                .showImageForEmptyUri(R.drawable.bg_01)
                .showImageOnFail(R.drawable.bg_01)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mDevice != null) {

            if(!TextUtils.isEmpty(mDevice.getAtcFileNm())){

                String uriTemplate = "http://220.90.216.73:8080/masterapi/v1/devices/%s/%s/image/%s";

                String imageUri = String.format(uriTemplate, mDevice.getSvcTgtSeq(), mDevice.getSpotDevSeq(), mDevice.getAtcFileNm());

                ImageLoader.getInstance().displayImage(imageUri, mIvMain, mOptions);

            }else{
                ImageLoader.getInstance().displayImage("", mIvMain, mOptions);
            }

        }


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.iv_device_setting_camera:
                createChooseDialog();
                break;

            case R.id.bt_device_setting_modify:

                if(checkInput()){
                    new DeviceModifyTask().execute();
                }

                break;

        }
    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(mEtDevNm.getText().toString())){
            Toast.makeText(getActivity(),"현장장치 이름을 작성해주세요", Toast.LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(mEtSpotDevId.getText().toString())){
            Toast.makeText(getActivity(),"현장장치 아이디를 작성해주세요", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    public void setDevice(Device device){

        mDevice = device;
    }

    public void setSvcTgt(SvcTgt svcTgt){
        mSvcTgt = svcTgt;
    }

    private void createChooseDialog(){

        //String[] items = getActivity().getResources().getStringArray(R.array.image_menu);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("사진 설정하기")
                .setItems(R.array.image_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d(TAG, "onClick item which = " + which);

                        switch (which) {

                            case 0: {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //촬영된 이미지를 저장할 파일의 경로를 생성
                                mImageUri = Util.createSaveCropFile(getActivity());
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                                startActivityForResult(intent, REQ_PICK_FROM_CAMERA);
                            }
                            break;

                            case 1:
                                //사진 선택
                            {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                startActivityForResult(intent, REQ_PICK_FROM_ALBUM);
                            }
                            break;

                        }

                    }
                })
                .setNegativeButton(R.string.common_cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != getActivity().RESULT_OK){

            return;
        }

        switch(requestCode){

            case REQ_PICK_FROM_ALBUM:
            {
                Uri pickImageUri = data.getData();

                File originalFile = Util.getImageFile(getActivity(), pickImageUri);

                mImageUri = Util.createSaveCropFile(getActivity());
                File cropFile = new File(mImageUri.getPath());

                Util.copyFile(originalFile, cropFile);

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageUri, "image/*");

                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("output", mImageUri);
                startActivityForResult(intent, REQ_CROP_IMAGE);
            }
                break;

            case REQ_PICK_FROM_CAMERA:
            {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageUri, "image/*");

                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                //intent.putExtra("return-data", true);
                intent.putExtra("output", mImageUri);
                startActivityForResult(intent, REQ_CROP_IMAGE);
            }
            break;

            case REQ_CROP_IMAGE:
            {
                Log.d(TAG, "onActivityResult REQ_CROP_IMAGE");

                String full_path = mImageUri.getPath();
                Log.d(TAG, "onActivityResult REQ_CROP_IMAGE full_path = " + full_path);

                new UploadImgTask().execute(full_path);
            }
                break;

        }


    }


    private class UploadImgTask extends AsyncTask<String, Void, DeviceApiResponse>{

        ProgressDialog progressDialog;
        String imgPath;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.common_wait), true, false);
        }

        @Override
        protected DeviceApiResponse doInBackground(String... params) {

            DeviceApi deviceApi = new DeviceApi(ApplicationPreference.getInstance().getPrefAccessToken());

            imgPath = params[0];
            DeviceApiResponse response = deviceApi.uploadDeviceImg(mDevice, imgPath);
            return response;
        }

        @Override
        protected void onPostExecute(DeviceApiResponse response) {

            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
                progressDialog = null;
            }

            if(response != null && response.getResponseCode().equals(ApiConstants.CODE_OK)){

                Toast.makeText(getActivity(), "이미지 업로드가 정상 처리되었습니다..", Toast.LENGTH_LONG).show();

                Bitmap crop = BitmapFactory.decodeFile(imgPath);
                mIvMain.setImageBitmap(crop);

                String imgFileName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

                mDevice.setAtcFileNm(imgFileName);

                ModifyDeviceMgr.setModifyDevice(mDevice);

            }else{

                Toast.makeText(getActivity(), "이미지 업로드가 실패되었습니다.\n[" + response.getMessage() +"]", Toast.LENGTH_LONG).show();

            }


        }
    }


    private class DeviceModifyTask extends AsyncTask<Void, Void, DeviceApiResponse> {

        @Override
        protected DeviceApiResponse doInBackground(Void... params) {

            DeviceApi deviceApi = new DeviceApi(ApplicationPreference.getInstance().getPrefAccessToken());

            DeviceApiResponse response = deviceApi.deviceModify(mSvcTgt.getSvcTgtSeq(), mDevice.getSpotDevSeq(), mEtDevNm.getText().toString(),
                    mEtSpotDevId.getText().toString(), mDevice.getDevPwd(), mDevice.getDevModelNm(), mDevice.getTermlMakrNm(), mSvcTgt.getMbrSeq(),
                    ApplicationPreference.getInstance().getPrefAccountId(),
                    mDevice.getGwCnctId(), mDevice.getUseYn(), "A");

            return response;
        }

        @Override
        protected void onPostExecute(DeviceApiResponse response) {
            if(response != null && response.getResponseCode().equals(ApiConstants.CODE_OK)){

                Toast.makeText(getActivity(), "디바이스 수정이 성공하였습니다.", Toast.LENGTH_LONG).show();

                mDevice.setDevNm(mEtDevNm.getText().toString());
                mDevice.setSpotDevId(mEtSpotDevId.getText().toString());

                ModifyDeviceMgr.setModifyDevice(mDevice);

            }else{

                Toast.makeText(getActivity(), "디바이스 수정이 실패하였습니다.\n[" + response.getMessage() +"]", Toast.LENGTH_LONG).show();

            }
        }
    }

}
