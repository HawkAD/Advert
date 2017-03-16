package com.hawk.android.adsdk.demo;

import com.google.android.gms.ads.AdRequest;
import com.hawk.android.adsdk.ads.HKNativeAd;
import com.hawk.android.adsdk.ads.mediator.HawkAdRequest;
import com.hawk.android.adsdk.ads.mediator.HkNativeAdListener;
import com.hawk.android.adsdk.demo.view.NativeViewBuild;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


/**
 * Created by $ liuluchao@cmcm.com on 2016/7/4.
 */
public class NativeAdSpreadSampleActivity extends Activity implements View.OnClickListener {

    private HKNativeAd mHKNativeAd;
    private FrameLayout nativeAdContainer;//View Container
    private View mAdView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        nativeAdContainer = (FrameLayout) findViewById(R.id.big_ad_container);
        findViewById(R.id.btn_req).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
        initNativeAd();

    }


    private void initNativeAd() {
        //setp1 : create mHKNativeAd
        //The first parameter：Context
        //The second parameter: posid
        String testUnitId=getString(R.string.native_ad_unitid);
//        AdSettings.addTestDevice("e200707a9622f3472b8d3ecc8c59cac2");
        new AdRequest.Builder().addTestDevice("6167451E2EA511D5C40895AEFBD9615C");
        mHKNativeAd = new HKNativeAd(this,testUnitId);
        //setp2 : set callback listener(HkNativeAdListener)
        mHKNativeAd.setNativeAdListener(new HkNativeAdListener() {
            @Override
            public void onNativeAdLoaded() {
                //ad load  success ,you can do other something here;
                showAd();
                Toast.makeText(NativeAdSpreadSampleActivity.this, "ad load  success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNativeAdFailed(int i) {
                Toast.makeText(NativeAdSpreadSampleActivity.this, "ad load  failed,error code is:" + i, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdClick() {
                Toast.makeText(NativeAdSpreadSampleActivity.this, "ad click", Toast.LENGTH_LONG).show();
            }
        });
        mHKNativeAd.loadAd(new HawkAdRequest().addTestDevice("92cf3642de2764ca21a126a78d60894a"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_req:
                //step3 : start load nativeAd
                mHKNativeAd.loadAd(new HawkAdRequest());
                break;
            case R.id.btn_show:
                //showAd();
                break;
            default:
                break;
        }
    }

    /**
     * if load nativeAd success,you can get and show nativeAd;
     */
    private void showAd(){
        if(mHKNativeAd != null){
            Object ad = mHKNativeAd.getAd();
            if (ad == null) {
                Toast.makeText(NativeAdSpreadSampleActivity.this,
                        "no native ad loaded!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nativeAdContainer != null) {
                // remove old ad view
                nativeAdContainer.removeAllViews();
            }
            //the mAdView is custom by publisher
            mAdView = NativeViewBuild.createAdView(getApplicationContext(), ad, mHKNativeAd);
            if (mHKNativeAd != null) {
                mHKNativeAd.unregisterView();
                nativeAdContainer.addView(mAdView);
                mHKNativeAd.registerViewForInteraction(mAdView);
            }
            //add the mAdView into the layout of view container.(the container should be prepared by youself)
        }
    }
}
