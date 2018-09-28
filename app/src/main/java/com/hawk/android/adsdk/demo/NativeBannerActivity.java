package com.hawk.android.adsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.ads.AdSettings;
import com.hawk.android.adsdk.demo.view.NativeBannerViewBuild;


public class NativeBannerActivity extends Activity {

    private RelativeLayout nativeBannerAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_banner);
        nativeBannerAdContainer = (RelativeLayout) findViewById(R.id.native_banner_ad_container);
        loadAd();
    }

    long netTime;

    private void loadAd() {
        AdSettings.addTestDevice("c6a5cf8e-e8ae-4403-8cf0-297eb15692a0");
//        AdSettings.addTestDevice("e52ef714-59bb-49e7-a801-08e722ef97b0");
        AdSettings.addTestDevice("3acac5a2-1932-4906-9893-07000c24b399");
        String unitId = getString(R.string.native_banner_ad_unitid);
    }

}
