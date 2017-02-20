package com.kt.iot.mobile.ui.fragment.settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.data.BgSettingItem;
import com.kt.iot.mobile.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ceoko on 15. 4. 29..
 */
public class BgSettingAdapter extends ArrayAdapter<BgSettingItem> {

    public static final int POOL_SIZE = 1;
    public ExecutorService executorService;

    private HashMap<Integer, Bitmap> cache = new HashMap<Integer, Bitmap>();

    private Activity activity;
    private Context mContext;
    private ArrayList<BgSettingItem> mData;

    private int width = 0;

    public BgSettingAdapter(Context context, int resource, ArrayList<BgSettingItem> data) {
        super(context, resource, data);

        executorService = Executors.newFixedThreadPool(POOL_SIZE);

        activity = (Activity) context;
        mContext = context;
        mData = data;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ItemHolder holder;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.itemview_setting_bg, parent, false);
            holder = new ItemHolder();

            holder.ivBgThumb = (ImageView) row.findViewById(R.id.iv_setting_bg);
            /*holder.tvItemName = (TextView) row.findViewById(R.id.tv_setting_bg_name);
            holder.tvItemName.setTypeface(GiGaIotApplication.getDefaultTypeFace());*/

            row.setTag(holder);
        }

        holder = (ItemHolder)row.getTag();

        if(mData.get(position).getResId() == -1){
            holder.ivBgThumb.setVisibility(View.INVISIBLE);
        }else{
//            holder.ivBgThumb.setImageResource(mData.get(position).getResId());
            holder.ivBgThumb.setVisibility(View.VISIBLE);
            holder.ivBgThumb.setTag(mData.get(position).getResId());
            Bitmap bitmap = cache.get(mData.get(position).getResId());
            if (bitmap != null) {
                holder.ivBgThumb.setImageBitmap(bitmap);
                holder.ivBgThumb.setTag(null);
            } else {
                holder.ivBgThumb.setImageBitmap(null);
                final  ItemHolder tempItemHolder = holder;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = BitmapUtil.getBitmapFromResource(mContext.getResources(), mData.get(position).getResId(), width, width);
                        cache.put(mData.get(position).getResId(), bitmap);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (tempItemHolder.ivBgThumb.getTag() != null && tempItemHolder.ivBgThumb.getTag().equals(mData.get(position).getResId())) {
                                    tempItemHolder.ivBgThumb.setImageBitmap(bitmap);
                                    tempItemHolder.ivBgThumb.setTag(null);
                                }
                            }
                        });
                    }
                });
            }

        }

//        holder.tvItemName.setText(mData.get(position).getMenuName());

        return row;
    }

    private class ItemHolder {
        ImageView ivBgThumb;
//        TextView tvItemName;
    }

}
