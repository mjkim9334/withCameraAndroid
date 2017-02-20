package com.kt.iot.mobile.ui.fragment.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 13..
 */
public class SvcTgtListAdapter extends BaseAdapter{

    Context mContext;
    ArrayList<SvcTgt> mData;

    public SvcTgtListAdapter(Context context, ArrayList<SvcTgt> data){
        mContext = context;
        mData = data;
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
            row = inflater.inflate(R.layout.itemview_svctgt, parent, false);
            holder = new ItemHolder();
            holder.tvSvcTgtName = (TextView) row.findViewById(R.id.tv_svctgtlist_seq);
            holder.ivSvcTgtIcon = (ImageView) row.findViewById(R.id.iv_svctgtlist_icon);
            //holder.tvSvcTgtName.setTypeface(GiGaIotApplication.getDefaultTypeFace());
            row.setTag(holder);
        }

        holder = (ItemHolder) row.getTag();

        holder.tvSvcTgtName.setText(mData.get(position).getSvcTgtNm());

        /*switch(position){

            case 0:
                holder.ivSvcTgtIcon.setImageResource(R.drawable.hlth);
                break;
            case 1:
                holder.ivSvcTgtIcon.setImageResource(R.drawable.home);
                break;
            case 2:
                holder.ivSvcTgtIcon.setImageResource(R.drawable.light);
                break;
            case 3:
                holder.ivSvcTgtIcon.setImageResource(R.drawable.lightno);
                break;
            case 4:
                holder.ivSvcTgtIcon.setImageResource(R.drawable.plus);
                break;


        }*/


        return row;
    }

    private class ItemHolder{
        ImageView ivSvcTgtIcon;
        TextView tvSvcTgtName;
    }

}
