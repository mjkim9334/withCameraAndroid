package com.kt.iot.mobile.ui.fragment.event.list;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.EventApi;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.Event;
import com.kt.gigaiot_sdk.data.EventApiResponse;
import com.kt.gigaiot_sdk.data.EventLog;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by NP1014425901 on 2015-02-26.
 */
public class EventListFragment extends ListFragment {

    private final String TAG = EventListFragment.class.getSimpleName();

    public static final String ACTION_RECIVER_EVENT = "com.kt.iot.mobile.action.EVENT";
    public static final String CATEGORY_RECIVER_EVENT = "com.kt.iot.mobile.category.EVENT";

    private Device mDevice;
    private EventLogListAdapter mAdapter;

    private ArrayList<Event> mArrayEvents;
    private ArrayList<EventLog> mArrayEventLogs;

    private EventPushReceiver mEventPushReceiver;

    /*******************/
  //  public static int eventID;
    /***********************/

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mArrayEvents = new ArrayList<>();

        setEmptyText("이벤트가 없습니다");
        getListView().setBackgroundResource(R.drawable.bg_gradient_right);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(mEventPushReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(ACTION_RECIVER_EVENT);
        filter.addCategory(CATEGORY_RECIVER_EVENT);

        mEventPushReceiver = new EventPushReceiver();
        getActivity().registerReceiver(mEventPushReceiver, filter);

    }

    public void setDevice(Device device){
        mDevice = device;
    }

    public void refresh(){

        new GetEventListTask().execute();
    }

    private class GetEventListTask extends AsyncTask<Void, Void, EventApiResponse>{


        @Override
        protected EventApiResponse doInBackground(Void... params) {


            EventApi eventApi = new EventApi(ApplicationPreference.getInstance().getPrefAccessToken());
            EventApiResponse response = eventApi.getEventList(ApplicationPreference.getInstance().getPrefAccountId(), mDevice.getSvcTgtSeq());

            return response;
        }

        @Override
        protected void onPostExecute(EventApiResponse result) {

            mArrayEvents = result.getEvents();

            if(mArrayEvents != null && mArrayEvents.size() > 0){


                Log.d(TAG, "onActivityCreated mArrayEvents.size = " + mArrayEvents.size());
                new GetEventLogListTask().execute();

            }else {
                setListAdapter(null);
            }

        }
    }

    private class GetEventLogListTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... params) {

            mArrayEventLogs = new ArrayList<>();

            EventApi eventApi = new EventApi(ApplicationPreference.getInstance().getPrefAccessToken());

            //long lastTime = System.currentTimeMillis() - 1000*60*60*24;        //하루전

            long lastTime = Util.getTodayStartTimestamp();

            Log.d(TAG, "GetEventLogListTask lastTime = " + lastTime + " | date = " + Util.timestampToFormattedStr(lastTime));


            for(Event event : mArrayEvents){

                Log.d(TAG, "GetEventLogListTask getLog : event name = " + event.getStatEvetNm());

                EventApiResponse response = eventApi.getEventLogList(mDevice.getSpotDevSeq(), mDevice.getSvcTgtSeq(), event.getEventId(), lastTime);

                if(response.getEventLogs() != null && response.getEventLogs().size() > 0){

                    for(EventLog eventLog : response.getEventLogs()){

                        mArrayEventLogs.add(eventLog);
                    }

                }
            }

            if(mArrayEventLogs.size() > 1){
                Collections.sort(mArrayEventLogs, eventLogComparator);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mAdapter = new EventLogListAdapter(getActivity(), android.R.layout.simple_list_item_1, mArrayEventLogs);

            setListAdapter(mAdapter);


        }
    }

    private Comparator<EventLog> eventLogComparator = new Comparator<EventLog>() {

        @Override
        public int compare(EventLog lhs, EventLog rhs) {
            return Util.FormatedTimeToTimestamp(lhs.getOutbDtm()) > Util.FormatedTimeToTimestamp(rhs.getOutbDtm())
                    ? -1 : Util.FormatedTimeToTimestamp(lhs.getOutbDtm()) < Util.FormatedTimeToTimestamp(rhs.getOutbDtm())
                    ? 1:0;        //최신 로그가 상단에 오도록 정렬한다.
        }
    };


    private class EventPushReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i(TAG, "EventPushReceiver onReceive!");
            String strEventLog = intent.getStringExtra("msg");

            EventLog eventLog = new Gson().fromJson(strEventLog, EventLog.class);
            /********************/
          /*  if(eventLog.getEvetNm().equals("tempAbove"))
                eventID=1;
            else if(eventLog.getEvetNm().equals("tempBelow"))
                eventID=2;
            else if(eventLog.getEvetNm().equals("HumAbove"))
                eventID=3;
            else if(eventLog.getEvetNm().equals("Humbelow"))
                eventID=4;
            else if(eventLog.getEvetNm().equals("WaterDegree"))
                eventID=5;*/

            /*********************/

            if(mArrayEventLogs != null && mAdapter != null) {
                mArrayEventLogs.add(0, eventLog);
                mAdapter.notifyDataSetChanged();
            }

        }
    }

}
