package com.kt.iot.mobile.ui.fragment.device.list;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kt.gigaiot_sdk.data.Device;
import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<Device>
{
    private Context mContext   = null;
    private DisplayImageOptions mOptions;

    public DeviceListAdapter(Context context, int textViewResourceId, ArrayList<Device> items)
    {
        super(context, textViewResourceId, items);
        this.mContext = context;

        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.white_solid)
                .showImageForEmptyUri(R.drawable.noming)
                .showImageOnFail(R.drawable.noming)
                //.displayer(new RoundedBitmapDisplayer(1000))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

    }

    @Override
    public View getView(int nPosition, View convertView, ViewGroup parent)
    {
        PointerView pView = null;

        View view = convertView;

        if(view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.itemview_device, parent, false);
            pView = new PointerView(view);
            view.setTag(pView);
        }

        pView = (PointerView)view.getTag();

        // 데이터 클래스에서 해당 리스트목록의 데이터를 가져온다.
        Device item = getItem(nPosition);

        if(item != null)
        {
            // 현재 item의 position에 맞는 이미지와 글을 넣어준다.
            /*if (item.getThumbImgResId() != 0) {
                pView.GetIconView().setBackgroundResource(item.getThumbImgResId());
            }*/

            if(!TextUtils.isEmpty(item.getAtcFileNm())){

                //http://220.90.216.73:8080/masterapi/v1/devices/1000000569/1000000511/downloadImgStream

                String uriTemplate = "http://220.90.216.73:8080/masterapi/v1/devices/%s/%s/image/%s";

                String imageUri = String.format(uriTemplate, item.getSvcTgtSeq(), item.getSpotDevSeq(), item.getAtcFileNm());

                ImageLoader.getInstance().displayImage(imageUri, pView.GetIconView(), mOptions);

            }else{
                ImageLoader.getInstance().displayImage("", pView.GetIconView(), mOptions);
            }


            if (item.getDevNm() != null)  {
                pView.GetTitleView().setText(item.getDevNm());

            }
            if (item.getDevSttusCd() !=null) {
                //pView.GetSubTitleView().setText(item.getDevSttusCd());
                pView.GetSubTitleView().setText(item.getSpotDevSeq());
            }
        }

        return view;
    }

    /**
     * 뷰를 재사용 하기위해 필요한 클래스
     * 클래스 자체를 view tag로 저장/불러오므로 재사용가능
     */
    private class PointerView
    {
        private View        mBaseView = null;
        private ImageView   mIvIcon = null;
        private TextView    mTvTitle = null;
        private TextView    mTvSubTitle = null;

        public PointerView(View BaseView)
        {
            this.mBaseView = BaseView;
        }

        public ImageView GetIconView()
        {
            if(mIvIcon == null)
            {
                mIvIcon = (ImageView) mBaseView.findViewById(R.id.custom_list_image);
            }

            return mIvIcon;
        }

        public TextView GetTitleView()
        {
            if(mTvTitle == null)
            {
                mTvTitle = (TextView) mBaseView.findViewById(R.id.custom_list_title_main);
                //mTvTitle.setTypeface(GiGaIotApplication.getDefaultTypeFace());
            }

            return mTvTitle;
        }

        public TextView GetSubTitleView()
        {
            if(mTvSubTitle == null)
            {
                mTvSubTitle = (TextView) mBaseView.findViewById(R.id.custom_list_title_sub);
                mTvSubTitle.setTypeface(GiGaIotApplication.getDefaultTypeFace());
            }

            return mTvSubTitle;
        }
    }
}
