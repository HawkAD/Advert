package com.hawk.android.adsdk.demo;

import com.hawk.android.adsdk.ads.HKNativeAd;
import com.hawk.android.adsdk.ads.HkNativeAdFactory;
import com.hawk.android.adsdk.ads.mediator.HkNativeAdListener;
import com.hawk.android.adsdk.demo.view.NativeViewBuild;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by $ liuluchao@cmcm.com on 2016/7/4.
 */
public class NativeAdDefaultActivity extends Activity implements View.OnClickListener {

    private HKNativeAd mDefaultNativeAd;
    private FrameLayout nativeAdContainer;//View Container
    private View mAdView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_default);
        nativeAdContainer = (FrameLayout) findViewById(R.id.big_ad_container);
        findViewById(R.id.btn_req).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
        loadDefaultAd();
    }


    private void loadDefaultAd() {
        mDefaultNativeAd = HkNativeAdFactory.getDefultAdInstance(this,getString(R.string.native_ad_unitid));
        mDefaultNativeAd.setNativeAdListener(new HkNativeAdListener() {
            @Override
            public void onNativeAdLoaded(Object Ad) {
                Log.e("监听测试","内推广告加载成功");
                Toast.makeText(NativeAdDefaultActivity.this, "内推广告加载成功", Toast.LENGTH_SHORT).show();
                showDefaultAdAd(mDefaultNativeAd);
            }

            @Override
            public void onNativeAdFailed(int errorCode) {
                Log.e("监听测试","内推广告加载失败");
                Toast.makeText(NativeAdDefaultActivity.this, "内推广告加载失败,error code is:"+errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClick() {
                Log.e("监听测试","原生被点击");
                Toast.makeText(NativeAdDefaultActivity.this, "内推广告被点击", Toast.LENGTH_SHORT).show();
            }
        });
        mDefaultNativeAd.loadAd();
    }

    private void showDefaultAdAd(HKNativeAd defaultNativeAd){
        if(defaultNativeAd != null){

            if(defaultNativeAd.getAd() == null){
                Toast.makeText(NativeAdDefaultActivity.this,
                        "no native ad loaded!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nativeAdContainer != null) {
                // remove old ad view
                nativeAdContainer.removeAllViews();
            }
            //the mAdView is custom by publisher
            mAdView = NativeViewBuild.createAdView(getApplicationContext(),defaultNativeAd);
            if (defaultNativeAd != null) {
                defaultNativeAd.unregisterView();
                //The app must call this method,or click event will unavailable
                defaultNativeAd.registerViewForInteraction(mAdView);
            }
            //add the mAdView into the layout of view container.(the container should be prepared by youself)
            nativeAdContainer.addView(mAdView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_req:
                //step3 : start load nativeAd
                loadDefaultAd();
                break;
            case R.id.btn_show:
                //showAd();
                break;
            default:
                break;
        }
    }

}
