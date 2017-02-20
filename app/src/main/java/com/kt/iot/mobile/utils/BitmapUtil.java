package com.kt.iot.mobile.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by NP1014425901 on 2015-06-16.
 */
public class BitmapUtil {

    public static Bitmap getBitmapFromResource(Resources resources, int resId, int maxResolutionX, int maxResolutionY) {

        Bitmap bitmap = null;

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = computeSampleSize(resources, resId, maxResolutionX, maxResolutionY);
            options.inDither = false;
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeResource(resources, resId, options);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static final int UNCONSTRAINED = -1;

    private static int computeSampleSize(Resources resources, int resId, int maxResolutionX, int maxResolutionY) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        int maxNumOfPixels = maxResolutionX * maxResolutionY;
        int minSideLength = Math.min(maxResolutionX, maxResolutionY) / 2;
        return computeSampleSize(options, minSideLength, maxNumOfPixels);
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8 ) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == UNCONSTRAINED) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == UNCONSTRAINED) &&
                (minSideLength == UNCONSTRAINED)) {
            return 1;
        } else if (minSideLength == UNCONSTRAINED) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
