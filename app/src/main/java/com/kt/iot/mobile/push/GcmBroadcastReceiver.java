package com.kt.iot.mobile.push;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * GCMReceiver
 * Push 메시지를 수신한다
 *
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		GCMIntentService.runIntentInService(context, intent);		//GCMIntentService 를 호출
	}
	
}