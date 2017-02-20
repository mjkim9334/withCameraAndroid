package com.kt.iot.mobile.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by ceoko on 15. 4. 13..
 */
public class Util {

    public static void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public static View getCustomTitleView(Context context, String title){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View titleView = inflater.inflate(R.layout.actionbar_custom_title, null);
        TextView tvActionBarTitle = (TextView) titleView.findViewById(R.id.tv_actionbar_title);
        tvActionBarTitle.setTypeface(GiGaIotApplication.getDefaultTypeFace());
        tvActionBarTitle.setText(title);

        return titleView;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * Crop된 이미지가 저장될 파일을 만든다.
     * @return Uri
     */
    public static Uri createSaveCropFile(Context context){
        Uri uri;
        String url = String.valueOf(System.currentTimeMillis()) + ".jpg";
        uri = Uri.fromFile(new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), url));
        //Log.d(TAG, "createSaveCropFile new file path = " + getImageFile(uri).getAbsolutePath());
        return uri;
    }

    /**
     * 선택된 uri의 사진 Path를 가져온다.
     * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     * @param uri
     * @return
     */
    public static File getImageFile(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if(cursor == null || cursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String path = cursor.getString(column_index);

        if (cursor !=null ) {
            cursor.close();
            cursor = null;
        }

        return new File(path);
    }

    /**
     * 파일 복사
     * @param srcFile : 복사할 File
     * @param destFile : 복사될 File
     * @return
     */
    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally  {
                in.close();
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String CHART_HOME_DIR = null;

    public static void setChartHomeDir(String dir) {
        CHART_HOME_DIR = dir + "/" + "ChartHome";
    }

    public static String getChartHomeDir() {
        return CHART_HOME_DIR;
    }

    public static String timestampToFormattedStr(long timemills){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        return df.format(new Date(timemills));
    }

    public static long getTodayStartTimestamp(){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date currentTime = new Date();
        String strCurrentTime = df.format(currentTime);

        Log.i("time_test", "getTodayStartTimestamp strCurrentTime = " + strCurrentTime);

        Date todayStartTime = null;

        try {
            todayStartTime = df.parse(strCurrentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(todayStartTime != null)
            return todayStartTime.getTime();
        else
            return -1;

    }

    public static int getDayOfWeek(){

        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String calTimestampToHour(long watchTime){

        Log.i("time_test", "calTimestampToHour watchTime = " + watchTime);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(watchTime);

        long remain = minutes%60;

        if(remain >= 6){

            DecimalFormat format = new DecimalFormat(".#");

            double dMinuite = minutes;
            return format.format(dMinuite/60);

        }else{
            return String.valueOf(minutes/60);
        }

    }

    public static String timeFormatToSimple(String time){

        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(time);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            return sdf.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static long FormatedTimeToTimestamp(String time){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(time);

            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
