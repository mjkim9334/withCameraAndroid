package com.kt.iot.mobile.ui.fragment.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 15..
 */
public class DrawerMenuListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DrawerMenu> mData;

    public DrawerMenuListAdapter(Context context, ArrayList<DrawerMenu> data){

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
            row = inflater.inflate(R.layout.itemview_drawermenu_list, null);

            holder = new ItemHolder();
            holder.icon = (ImageView) row.findViewById(R.id.iv_drawermenu_icon);
            holder.name = (TextView) row.findViewById(R.id.tv_drawermenu_name);
            holder.name.setTypeface(GiGaIotApplication.getDefaultTypeFace());
            row.setTag(holder);
        }

        holder = (ItemHolder) row.getTag();
        holder.icon.setImageResource(mData.get(position).getMenuImgRsrcId());
        holder.name.setText(mData.get(position).getMenuName());

        return row;
    }

    private class ItemHolder{
        ImageView icon;
        TextView name;
    }

}
