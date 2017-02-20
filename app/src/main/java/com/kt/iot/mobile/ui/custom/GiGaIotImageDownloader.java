package com.kt.iot.mobile.ui.custom;

import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ceoko on 15. 5. 26..
 */
public class GiGaIotImageDownloader extends BaseImageDownloader {

    private final String TAG = GiGaIotImageDownloader.class.getSimpleName();

    public GiGaIotImageDownloader(Context context) {
        super(context);
    }

    @Override
    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {

        Log.d(TAG, "getStreamFromNetwork imageUri = " + imageUri);

        imageUri = imageUri.substring(0, imageUri.lastIndexOf("/"));

        Log.d(TAG, "getStreamFromNetwork after substring imageUri = " + imageUri);

        return super.getStreamFromNetwork(imageUri, extra);
    }
}
