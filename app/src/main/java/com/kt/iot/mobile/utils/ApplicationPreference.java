package com.kt.iot.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.kt.iot.mobile.android.R;

public class ApplicationPreference {

	public static final String PREF_NAME = "gigaiot.pref";

	public static final String PREF_GCM_REG_ID = "pref.gcm_regid";
	public static final String PREF_ACCOUNT_ID = "pref.account_id";
	public static final String PREF_ACCOUNT_MEMBER_SEQ = "pref.account_mbrseq";
	public static final String PREF_ACCESS_TOKEN = "pref.access_token";
	public static final String PREF_BG_DEFAULT_ID = "pref.bg_resource";
	public static final String PREF_BG_USER_CUSTOM = "pref.bg_user_custom";


	public static ApplicationPreference applicationPref;

	SharedPreferences pref;

	Editor editor;

	public static boolean isInitilized = false;

	public ApplicationPreference(Context context) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		editor = pref.edit();
		isInitilized = true;
	}

	public static void init(Context context) {
		applicationPref = new ApplicationPreference(context);
	}

	public static ApplicationPreference getInstance() {
		return applicationPref;
	}

	public void setPrefGcmRegId(String data) {
		editor.putString(PREF_GCM_REG_ID, data);
		editor.commit();
	}

	public String getPrefGcmRegId() {
		return pref.getString(PREF_GCM_REG_ID, "");
	}

	public void setPrefAccountId(String data) {
		editor.putString(PREF_ACCOUNT_ID, data);
		editor.commit();
	}

	public String getPrefAccountId() {
		return pref.getString(PREF_ACCOUNT_ID, "");
	}

	public String getPrefAccountMbrSeq() {
		return pref.getString(PREF_ACCOUNT_MEMBER_SEQ, "");
	}

	public void setPrefAccountMbrSeq(String data) {
		editor.putString(PREF_ACCOUNT_MEMBER_SEQ, data);
		editor.commit();
	}


	public void setPrefAccessToken(String data) {
		editor.putString(PREF_ACCESS_TOKEN, data);
		editor.commit();
	}

	public String getPrefAccessToken() {
		return pref.getString(PREF_ACCESS_TOKEN, "");
	}

	public void setPrefBgUserCustom(String data) {
		editor.putString(PREF_BG_USER_CUSTOM, data);
		editor.commit();
	}

	public String getPrefBgUserCustom() {
		return pref.getString(PREF_BG_USER_CUSTOM, "");
	}

	public void setPrefBgDefaultId(int drawable) {
		editor.putInt(PREF_BG_DEFAULT_ID, drawable);
		editor.commit();
	}

	public int getPrefBgDefaultId() {
		return pref.getInt(PREF_BG_DEFAULT_ID, R.drawable.bg_01);
	}

}



