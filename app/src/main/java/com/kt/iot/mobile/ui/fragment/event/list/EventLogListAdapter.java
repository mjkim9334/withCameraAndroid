package com.kt.iot.mobile.ui.fragment.event.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kt.gigaiot_sdk.data.Event;
import com.kt.gigaiot_sdk.data.EventLog;
import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.Util;

import java.util.ArrayList;

/**
 * Created by NP1014425901 on 2015-02-26.
 */
public class EventLogListAdapter extends ArrayAdapter<EventLog>  {

    private Context m_Context   = null;

    public EventLogListAdapter(Context context, int textViewResourceId, ArrayList<EventLog> items)
    {
        super(context, textViewResourceId, items);
        this.m_Context = context;
    }



    @Override
    public View getView(int nPosition, View convertView, ViewGroup parent)
    {
        EventListItemView itemView = null;

        View view = convertView;

        if(view == null)
        {
            view = LayoutInflater.from(m_Context).inflate(R.layout.itemview_event, parent, false);
            itemView = new EventListItemView(view);
            view.setTag(itemView);
        }

        itemView = (EventListItemView)view.getTag();

        // 데이터 클래스에서 해당 리스트목록의 데이터를 가져온다.
        EventLog item = getItem(nPosition);

        if(item != null)
        {
            itemView.GetTitleView().setText(item.getEvetNm());
            itemView.getSubTitleView().setText(item.getOutbDtm());
        }

        return view;
    }

    /**
     * 뷰를 재사용 하기위해 필요한 클래스
     * 클래스 자체를 view tag로 저장/불러오므로 재사용가능
     */
    private class EventListItemView
    {
        private View mBaseView = null;
        private TextView mTvTitle = null;
        private TextView mTvSubTitle = null;

        public EventListItemView(View BaseView)
        {
            this.mBaseView = BaseView;
        }

        public TextView GetTitleView()
        {
            if(mTvTitle == null)
            {
                mTvTitle = (TextView) mBaseView.findViewById(R.id.textView3);
                mTvTitle.setTypeface(GiGaIotApplication.getDefaultTypeFace());
            }

            return mTvTitle;
        }

        public TextView getSubTitleView() {
            if (mTvSubTitle == null) {
                mTvSubTitle = (TextView) mBaseView.findViewById(R.id.textView4);
                mTvSubTitle.setTypeface(GiGaIotApplication.getDefaultTypeFace());
            }
            return mTvSubTitle;
        }
    }
}
