package com.kt.iot.mobile.ui.fragment.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.data.BgSettingItem;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Util;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 29..
 */
public class BgSettingFragment extends ListFragment {

    private final String TAG = BgSettingFragment.class.getSimpleName();

    private final int[] BG_IMG_RES_IDS = {R.drawable.city_01, R.drawable.city_02, R.drawable.education, R.drawable.home_01, R.drawable.home_02, R.drawable.home_03, R.drawable.house_01, R.drawable.weather_01, R.drawable.weather_02};
    private String[] mArrayBgImgNames;
    private ArrayList<BgSettingItem> mArrItems;
    private BgSettingAdapter mAdapter;

    private final int REQ_PICK_FROM_CAMERA = 0;
    private final int REQ_PICK_FROM_ALBUM = 1;
    private final int REQ_CROP_IMAGE = 2;

    private Uri mImageUri;

    private View mFooterView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mArrItems = new ArrayList<>();
        mArrayBgImgNames = getResources().getStringArray(R.array.bg_setting_menu);

        for(int i=0; i < BG_IMG_RES_IDS.length; i++){
            mArrItems.add(new BgSettingItem(BG_IMG_RES_IDS[i]));
        }

        mFooterView = inflater.inflate(R.layout.itemview_footer_add, null);
        TextView tvUserImg = (TextView) mFooterView.findViewById(R.id.tv_foot_add);
        tvUserImg.setText("사용자 이미지 설정");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setBackgroundColor(Color.WHITE);

        getListView().addFooterView(mFooterView);

        mAdapter = new BgSettingAdapter(getActivity(), R.layout.itemview_setting_bg, mArrItems);
        setListAdapter(mAdapter);

    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (BgChangeListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement BgChangeListener");
        }

    }*/

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Log.d(TAG, "onListItemClick position = " + position);

        if (position < BG_IMG_RES_IDS.length) {
            setAppBackground(BG_IMG_RES_IDS[position], "");
            getActivity().finish();
        } else {
            createChooseDialog();
        }

        /*switch(position){

            case 0:
                setAppBackground(R.drawable.bg_01, "");
                getActivity().finish();
                break;

            case 1:
                setAppBackground(R.drawable.background_01, "");
                getActivity().finish();
                break;

            case 2:
                setAppBackground(R.drawable.bg_2560, "");
                getActivity().finish();
                break;

            case 3:
                createChooseDialog();
                break;


        }*/

    }

    private void setAppBackground(int drawable, String customImgPath){

        String path = ApplicationPreference.getInstance().getPrefBgUserCustom();

        if(path != null && path.equals("") == false){

            File file = new File(path);

            if(file.exists()){
                file.delete();
            }

        }

        ApplicationPreference.getInstance().setPrefBgDefaultId(drawable);
        ApplicationPreference.getInstance().setPrefBgUserCustom(customImgPath);

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

                intent.putExtra("aspectX", 3);
                intent.putExtra("aspectY", 5);
                intent.putExtra("scale", true);
                intent.putExtra("output", mImageUri);
                startActivityForResult(intent, REQ_CROP_IMAGE);
            }
            break;

            case REQ_PICK_FROM_CAMERA:
            {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageUri, "image/*");

                intent.putExtra("aspectX", 3);
                intent.putExtra("aspectY", 5);
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

                setAppBackground(R.drawable.bg_01, full_path);
                getActivity().finish();

            }
            break;

        }

    }
}
