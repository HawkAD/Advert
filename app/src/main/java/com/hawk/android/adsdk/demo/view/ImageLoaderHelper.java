
package com.hawk.android.adsdk.demo.view;

import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageLoaderHelper {

    private static boolean mIsInited = false;
    private static boolean mShouldToolboxInitImageLoader = true;

    public static void disableToolboxInitImageLoader() {
        mShouldToolboxInitImageLoader = false;
    }

    private static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .diskCacheSize(10 * 1024 * 1024)// 10MB
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static ImageLoader getInstance(Context context) {
        if (mShouldToolboxInitImageLoader && !mIsInited) {
            synchronized (ImageLoaderHelper.class) {
                if (mShouldToolboxInitImageLoader && !mIsInited) {
                    initImageLoader(context);
                    mIsInited = true;
                }
            }
        }

        return ImageLoader.getInstance();
    }
}
