package com.kt.iot.mobile;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import com.kt.iot.mobile.ui.custom.GiGaIotImageDownloader;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Util;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ceoko on 15. 3. 30..
 */
public class GiGaIotApplication extends Application{

    public static final String TAG = GiGaIotApplication.class.getSimpleName();

   private static Typeface mTypeface;

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationPreference.init(this);
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/NotoSansKR-Thin.otf");

        initChartLibrary();
        initImageLoader(getApplicationContext());
    }

    public static Typeface getDefaultTypeFace(){

        return mTypeface;
    }

    private void initChartLibrary(){

        if(TextUtils.isEmpty(Util.getChartHomeDir())){
            Util.setChartHomeDir(getExternalFilesDir(null).getAbsolutePath());
            File chartHomeDir = new File(Util.getChartHomeDir());

            if(!chartHomeDir.exists()){
                Log.d(TAG, "initChartLibrary chartHomeDir is not exist. so, make dir");
                chartHomeDir.mkdir();
            }
        }

        String chartLibFilepath = Util.getChartHomeDir() + "/Chart.js";

        File libFile = new File(chartLibFilepath);

        if(!libFile.exists()){

            Log.d(TAG, "initChartLibrary Chart.js is not exist");

            AssetManager assetManager = getAssets();

            try {

                InputStream in = assetManager.open("chart/Chart.js");

                Util.copyToFile(in, libFile);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Log.d(TAG, "initChartLibrary Chart.js is exist");
            return;
        }
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        //config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.imageDownloader(new GiGaIotImageDownloader(context));
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

}
