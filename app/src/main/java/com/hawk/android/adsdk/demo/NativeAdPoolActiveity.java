package com.hawk.android.adsdk.demo;

import com.hawk.android.adsdk.ads.HKNativeAd;
import com.hawk.android.adsdk.ads.HkAdPool;
import com.hawk.android.adsdk.ads.HkNativeAdFactory;
import com.hawk.android.adsdk.ads.mediator.HawkAdRequest;
import com.hawk.android.adsdk.ads.mediator.HkNativeAdListener;
import com.hawk.android.adsdk.demo.view.NativeViewBuild;
import com.mopub.nativeads.ViewBinder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Author: wangpeng
 * Date: 2017/4/18 19:45
 */
public class NativeAdPoolActiveity extends Activity implements View.OnClickListener {


    private HKNativeAd mHKNativeAd;
    private FrameLayout nativeAdContainer;//View Container
    private View mAdView = null;
    private HkAdPool mHkAdPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        nativeAdContainer = (FrameLayout) findViewById(R.id.big_ad_container);
        findViewById(R.id.btn_req).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
        Log.e("ceshi","启动"+getString(R.string.native_ad_unitid)+"缓存池");
        //启动缓存池，可以在要显示广告之前的任何位置启动缓存池,如app启动时
        mHkAdPool = new HkAdPool(this);
        mHkAdPool.power(new HawkAdRequest()
                        .addTestDevice("5545e13a65e654f10f8e1fc4b7a35ca2")
                        .setMoPubViewBinder(new ViewBinder.Builder(R.layout.mopub_native_ad_layout)//设置mopub的viewbinder
                                .titleId(R.id.native_ad_title)
                                .textId(R.id.native_ad_text)
                                .mainImageId(R.id.native_ad_main_image)
                                .iconImageId(R.id.native_ad_icon_image)
                                .privacyInformationIconImageId(R.id.native_ad_daa_icon_image)
                                .build()),
                getString(R.string.native_ad_unitid),"[2]");//广告位ID，缓存条数
        initNativeAd();
    }

    long netTime;
    private void initNativeAd() {
        //setp1 : create mHKNativeAd
        //The first parameter：Context
        //The second parameter: posid
        String testUnitId=getString(R.string.native_ad_unitid);
//        AdSettings.addTestDevice("92cf3642de2764ca21a126a78d60894a");
        mHKNativeAd = HkNativeAdFactory.getAdPoolInstance(this,testUnitId);
        mHKNativeAd.setNativeAdListener(new HkNativeAdListener() {
            @Override
            public void onNativeAdLoaded() {
                Log.e("app测试"," app 请求时间="+(System.currentTimeMillis()-netTime));
                //ad load  success ,you can do other something here;
                showAd();
                Log.e("监听测试","原生加载成功");
                Toast.makeText(NativeAdPoolActiveity.this, "原生加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNativeAdFailed(int var1) {
                Log.e("监听测试","原生加载失败");
                Toast.makeText(NativeAdPoolActiveity.this, "原生加载失败,error code is:"+var1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClick() {
                Log.e("监听测试","原生被点击");
                Toast.makeText(NativeAdPoolActiveity.this, "原生被点击", Toast.LENGTH_SHORT).show();
            }

        });
        mHKNativeAd.loadAd();
        //setp2 : set callback listener(INativeAdLoaderListener)
        netTime = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_req:
                //step3 : start load nativeAd
                mHKNativeAd.loadAd();
                netTime = System.currentTimeMillis();
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
            if(mHKNativeAd != null&&!mHKNativeAd.isLoaded()){
                Toast.makeText(NativeAdPoolActiveity.this,
                        "no native ad loaded!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nativeAdContainer != null) {
                // remove old ad view
                nativeAdContainer.removeAllViews();
            }
            //the mAdView is custom by publisher
            mAdView = NativeViewBuild.createAdView(getApplicationContext(),mHKNativeAd);
            if (mHKNativeAd != null) {
                mHKNativeAd.unregisterView();
                //The app must call this method,or click event will unavailable
                mHKNativeAd.registerViewForInteraction(mAdView);
            }
            //add the mAdView into the layout of view container.(the container should be prepared by youself)
            nativeAdContainer.addView(mAdView);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHkAdPool != null) {
            mHkAdPool.pauseAll();
        }
    }
}
