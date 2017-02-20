package com.kt.iot.mobile.push;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.kt.gigaiot_sdk.PushApi;
import com.kt.gigaiot_sdk.TagStrmApi;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.EventLog;
import com.kt.gigaiot_sdk.data.TagStrmApiResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.data.LogStream;
import com.kt.iot.mobile.ui.activity.IntroActivity;
import com.kt.iot.mobile.ui.fragment.event.list.EventListFragment;
import com.kt.iot.mobile.ui.fragment.rawdata.RawdataGraphWebViewFragment;

/*****/
import com.kt.iot.mobile.utils.ApplicationPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;
/**************for control*************************/
import static com.kt.iot.mobile.ui.activity.DeviceActivity.EXTRA_DEVICE;
import static com.kt.iot.mobile.ui.activity.DeviceActivity.strDevice;
import android.os.AsyncTask;

/**
 * Created by ceoko on 15. 6. 19..
 */
public class GCMIntentService extends IntentService {

    private static final String TAG = GCMIntentService.class.getSimpleName();

    private static PowerManager.WakeLock sWakeLock;
    private static final Object LOCK = GCMIntentService.class;
    private static long time;
    private static long ex_time=System.currentTimeMillis();
    private static int count=0;
/***************/
    NotificationManager nm;
    Notification.Builder builder;
    int eventID=0;

 //  BackgroundTask task;
   // int flag=1;

/*****************/




    /**
     * Constructor
     */
    public GCMIntentService(){
        super("GigaIot");
    }

    /**
     * Constructor
     * @param name
     */
    public GCMIntentService(String name){
        super("GigaIot");
    }

    /**
     * IntentService 시작
     * @param context Android Application Context
     * @param intent Intent
     */
    static void runIntentInService(Context context, Intent intent){
        Log.d(TAG, "GCMIntentService.runIntentInService() GCM runIntentInService");
        synchronized (LOCK) {
            if(sWakeLock == null){
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                sWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,"Push_WakeLock");
            }
            sWakeLock.acquire();
            intent.setClassName(context, GCMIntentService.class.getName());
            context.startService(intent);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "GCMIntentService.onHandleIntent()  GCM onHandleIntent");
        Log.d(TAG, "intent : " + intent.toString());
        Log.d(TAG, "intent : " + intent);

        try{
            final Context context = getApplicationContext();
            String action = intent.getAction();
            Log.d(TAG, "action : " + action);

            if(action.equals("com.google.android.c2dm.intent.REGISTRATION")){

                handleRegistration(context, intent);

            } else if(action.equals("com.google.android.c2dm.intent.RECEIVE")){


				/*new Handler(getMainLooper()).post(new Runnable() {

					@Override
					public void run() {

						Toast.makeText(GCMIntentService.this, "GCM push received!!", Toast.LENGTH_LONG).show();

					}
				});*/

                handleMessage(context, intent);
            }
        } finally{
            synchronized (LOCK) {
                if (sWakeLock.isHeld())
                    sWakeLock.release();//always release before acquiring for safety just in case


            }
        }
    }

    /**
     * GCM Registration ID 수신
     * @param context Android Application Context
     * @param intent Intent
     */
    private void handleRegistration(Context context, Intent intent){
        Log.d(TAG, "GCMIntentService.handleRegistration()  GCM handleRegistration");

        String regirationId = intent.getStringExtra("registration_id");
        String unregistered = intent.getStringExtra("unregistered");
        String error = intent.getStringExtra("error");


        Log.i(TAG, "registerId : " + regirationId);
        Log.i(TAG, "unregistered : " + unregistered);
        Log.i(TAG, "error : " + error);

        if(regirationId != null && regirationId.length() > 0){

        }
        else if(unregistered != null && unregistered.length() > 0){

            Log.i(TAG, "unregistered = " + unregistered);

        }
        else if(error != null && error.length() > 0){

        }
        else{

        }
    }

    /**
     * 수신된 메시지를 처리
     * @param context Android Application Context
     * @param intent Intent
     */
    private void handleMessage(Context context, Intent intent){
        Log.d(TAG, "GCMIntentService.handleMessage()  GCM handleMessage");

        Set<String> keys = intent.getExtras().keySet();
        for(String key : keys){
            Log.d(TAG, "GCM Key : " + key);

            /*Log.d(TAG, "GCM Value : " + intent.getExtras().getString(key));*/
        }
        sendIntent(context, intent);
    }

    /**
     * @param context Android Application Context
     * @param intent Push 메시지를 수신한 Intent
     */
    private void sendIntent(Context context, Intent intent) {
        Log.d(TAG, "GCMIntentService.sendIntent()  #### GCM sendIntent");
        String recvMsg = intent.getStringExtra("message");
        String type = "";
        String message = "";
        try {
            JSONObject objJson = new JSONObject(recvMsg);

            type = objJson.getString("type");
            message = objJson.getString("message");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "GCMIntentService.sendIntent() type = " + type + " message = " + message);
        if(type.equals(PushApi.PUSH_MSG_TYPE_COLLECT)){
            Log.d(TAG, "GCMIntentService.sendIntent()  TYPE = PushApi.PUSH_MSG_TYPE_COLLECT message = " + message);
            Intent msgIntent = new Intent(RawdataGraphWebViewFragment.ACTION_RECIVER_RAWDATA);
            msgIntent.addCategory(RawdataGraphWebViewFragment.CATEGORY_RECIVER_RAWDATA);

            /*************COMPLETE******************/


           /* JSONObject jsonObject= null;
            JSONObject jsonObject1=null;
            try {
                jsonObject = new JSONObject(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String temp=jsonObject.getString("attributes");
              jsonObject1=new JSONObject(temp);
                int log=jsonObject1.getInt("water");
                Log.w(TAG, "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+temp);
                Log.w(TAG, "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+Integer.toString(log));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/


            /***********************************/


            msgIntent.putExtra("msg", message);

            sendBroadcast(msgIntent);

        }else if(type.equals(PushApi.PUSH_MSG_TYPE_OUTBREAK)){
            Log.d(TAG, "GCMIntentService.sendIntent()  TYPE = PushApi.PUSH_MSG_TYPE_OUTBREAK message = " + message);

            Intent msgIntent = new Intent(EventListFragment.ACTION_RECIVER_EVENT);
            time=System.currentTimeMillis();

            /*******************************/

            String strEventLog = message;

            EventLog eventLog = new Gson().fromJson(strEventLog, EventLog.class);
            Log.d(TAG, "*******************EVENTNAME**************"+ eventLog.getEvetNm());
            Log.d(TAG, "ex_time="+Long.toString(ex_time));
            Log.d(TAG, "time="+Long.toString(time));
            Log.d(TAG, "Result = "+Long.toString(time-ex_time));

            if(eventLog.getEvetNm().equals("tempAbove"))
                eventID=1;
          //  task=new BackgroundTask();
         //  task.execute();

           /* nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            Intent intent1 = new Intent(getApplicationContext(),IntroActivity.class); //인텐트 생성.

            builder = new Notification.Builder(getApplicationContext());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity( getApplication(),eventID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
*/

            //case 1: tempAbove---->
            if(eventLog.getEvetNm().equals("tempAbove")&&(time-ex_time>50000)) // 50초 있다가
            {


                nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent1 = new Intent(getApplicationContext(),IntroActivity.class); //인텐트 생성.

                builder = new Notification.Builder(getApplicationContext());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingNotificationIntent = PendingIntent.getActivity( getApplication(),eventID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);




                count++;

                builder.setSmallIcon(R.drawable.user).setTicker("HETT").setWhen(System.currentTimeMillis())
                        .setNumber(count).setContentTitle("FlowerOre").setContentText("너무 더워요!")
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingNotificationIntent).setAutoCancel(true).setOngoing(true);




                nm.notify(eventID, builder.build());
                ex_time=time;
           /*********************************************/
             /*  String ctrlMsg="message";
                Gson gson = new Gson();
                Device mDevice = gson.fromJson(strDevice, Device.class);
                TagStrmApi tagStrmApi = new TagStrmApi(ApplicationPreference.getInstance().getPrefAccessToken());
                TagStrmApiResponse response = tagStrmApi.sendCtrlMsg(mDevice.getSvcTgtSeq(), mDevice.getSpotDevSeq(), mDevice.getSpotDevId(), mDevice.getGwCnctId(),
                        "led","0000020",
                        ApplicationPreference.getInstance().getPrefAccountId(), ctrlMsg); */
           /***********************************************************/

            }

        //    nm.notify(eventID, builder.build());

            /**************************************/

            msgIntent.addCategory(EventListFragment.CATEGORY_RECIVER_EVENT);

            msgIntent.putExtra("msg", message);

            sendBroadcast(msgIntent);

        }


    }


}
