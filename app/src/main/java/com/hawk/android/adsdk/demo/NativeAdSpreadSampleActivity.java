package com.hawk.android.adsdk.demo;

import com.google.android.gms.ads.AdRequest;

import com.facebook.ads.AdSettings;
import com.hawk.android.adsdk.ads.HKNativeAd;
import com.hawk.android.adsdk.ads.mediator.HawkAdRequest;
import com.hawk.android.adsdk.ads.mediator.HkNativeAdListener;
import com.hawk.android.adsdk.demo.view.NativeViewBuild;
import com.mopub.nativeads.ViewBinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


/**
 * Created by tzh on 2016/11/3.
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
        /**
         * 添加facebook的deviceID,从facebook的log中获取，可以用"facebook"关键字过滤。
         * 注意：1、deviceID会每天变化;
         *       2、facebook的deviceID和admob的不一样
         *       3、开发debug阶段发现facebook报错误码为1001 时，请确认是否添加了deviceID
         *       4、AdSettings.addTestDevice("e200707a9622f3472b8d3ecc8c59cac2");和
         *       new HawkAdRequest().addTestDevice("e200707a9622f3472b8d3ecc8c59cac2")作用是一样的。
         *       5、deviceID可以添加多个
         */

        /**
         * 添加facebook DeviceID
         */
        AdSettings.addTestDevice("8e7dc10cdcf65ea73ad283a5070d9c36");
        AdSettings.setTestAdType(AdSettings.TestAdType.VIDEO_HD_16_9_46S_APP_INSTALL);
        /**
         * 添加Admob DeviceID
         */
        new AdRequest.Builder().addTestDevice("789963D0B48FB29311FE58F9F1EA870F");
        mHKNativeAd = new HKNativeAd(this,testUnitId);
        //setp2 : set callback listener(HkNativeAdListener)
        mHKNativeAd.setNativeAdListener(new HkNativeAdListener() {
            @Override
            public void onNativeAdLoaded(Object Ad) {
                //ad load  success ,you can do other something here;
                showAd(Ad);
                Toast.makeText(NativeAdSpreadSampleActivity.this, "ad load  success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNativeAdFailed(int errorCode) {
                /**
                 * errorCode 为聚合sdk内部统一转码后的code，具体的errorCode可以查看log
                 * 每种errorCode产生原因可以参考admob和facebook平台的错误码说明
                 */

                Toast.makeText(NativeAdSpreadSampleActivity.this, "ad load  failed,error code is:" + errorCode, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdClick() {
                Toast.makeText(NativeAdSpreadSampleActivity.this, "ad click", Toast.LENGTH_LONG).show();
            }
        });
        /**
         * setImgUrlMode(false) 可以设置返回图片的模式,true为只返回图片的Url，false为返回图片资源，默认为false
         */
        mHKNativeAd.loadAd(new HawkAdRequest()
                .setMoPubViewBinder(new ViewBinder.Builder(R.layout.mopub_native_ad_layout)
                .titleId(R.id.native_ad_title)
                .textId(R.id.native_ad_text)
                .mainImageId(R.id.native_ad_main_image)
                .iconImageId(R.id.native_ad_icon_image)
                .privacyInformationIconImageId(R.id.native_ad_daa_icon_image)
                .build()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_req:
                //step3 : start load nativeAd
                mHKNativeAd.loadAd(new HawkAdRequest());
                break;
            case R.id.btn_show:
//                showAd();
                break;
            default:
                break;
        }
    }

    /**
     * if load nativeAd success,you can get and show nativeAd;
     */
    private void showAd(Object Ad){
        if(mHKNativeAd != null){

            if(mHKNativeAd != null&&!mHKNativeAd.isLoaded()){
                Toast.makeText(NativeAdSpreadSampleActivity.this,
                        "no native ad loaded!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nativeAdContainer != null) {
                // remove old ad view
                nativeAdContainer.removeAllViews();
            }
            //the mAdView is custom by publisher
            mAdView = NativeViewBuild.createAdView(getApplicationContext(),mHKNativeAd,Ad);
            if (mHKNativeAd != null) {
                mHKNativeAd.unregisterView();
                //The app must call this method,or click event will unavailable
                mHKNativeAd.registerViewForInteraction(mAdView);
            }
            //add the mAdView into the layout of view container.(the container should be prepared by youself)
            nativeAdContainer.addView(mAdView);
        }
    }
}
