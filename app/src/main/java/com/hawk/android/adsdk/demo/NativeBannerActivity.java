package com.hawk.android.adsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.ads.AdSettings;
import com.hawk.android.adsdk.ads.HKNativeBannerAd;
import com.hawk.android.adsdk.ads.NativeBannerAdListener;
import com.hawk.android.adsdk.demo.view.NativeBannerViewBuild;


public class NativeBannerActivity extends Activity {

    private HKNativeBannerAd hkNativeBannerAd;
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
        String unitId = getString(R.string.native_banner_ad_unitid);
        hkNativeBannerAd = new HKNativeBannerAd(this, unitId);
        hkNativeBannerAd.setAdListener(new NativeBannerAdListener() {
            @Override
            public void onAdLoaded(Object ad) {
                Log.e("app测试", " app 请求时间=" + (System.currentTimeMillis() - netTime));
                //ad load  success ,you can do other something here;
                showAd(ad);
                Log.e("监听测试", "Native Banner 加载成功");
                Toast.makeText(NativeBannerActivity.this, "Native Banner 加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int errorCode) {
                Log.e("监听测试", "Native Banner 加载失败");
                Toast.makeText(NativeBannerActivity.this, "Native Banner 加载失败,error code is:" + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClick() {
                Log.e("监听测试", "Native Banner 被点击");
                Toast.makeText(NativeBannerActivity.this, "Native Banner 被点击", Toast.LENGTH_SHORT).show();
            }
        });
        hkNativeBannerAd.loadAd();
        netTime = System.currentTimeMillis();
    }

    private void showAd(Object Ad) {
        if (hkNativeBannerAd != null) {
            if (!hkNativeBannerAd.isLoaded()) {
                Toast.makeText(NativeBannerActivity.this, "no native banner ad loaded!", Toast.LENGTH_SHORT).show();
                return;
            }

            nativeBannerAdContainer.removeAllViews();
            View mAdView = NativeBannerViewBuild.createAdView(this, nativeBannerAdContainer, Ad);
            nativeBannerAdContainer.addView(mAdView);
        }
    }
}
