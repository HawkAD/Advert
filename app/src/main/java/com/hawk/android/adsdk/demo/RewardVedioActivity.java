package com.hawk.android.adsdk.demo;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.facebook.ads.AdSettings;
import com.hawk.android.adsdk.ads.HKRewardVedioAd;
import com.hawk.android.adsdk.ads.RewardVedioAdListener;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RewardVedioActivity extends AppCompatActivity {
    private final String TAG = "adSdk";
    private Button mButtonLoadAd;
    private Button mButtonShowAd;
    private HKRewardVedioAd ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        ad = new HKRewardVedioAd(this,getString(R.string.reward_ad_unitid));
        initView();
    }

    private void initView() {
        initActionBar();
        mButtonLoadAd = (Button)findViewById(R.id.btn_load_interstitial);
        mButtonShowAd = (Button)findViewById(R.id.btn_show_interstitial);

        mButtonLoadAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        mButtonShowAd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                show();
            }
        });
        /**
         * log中出现 1001 errorcode时，添加facebook的deviceID,从facebook的log中获取，可以用"AdSettings"关键字过滤。
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
        AdSettings.addTestDevice("5545e13a65e654f10f8e1fc4b7a35ca2");

        /**
         * Configure AdColony in your launching Activity's onCreate() method so that cached ads can
         * be available as soon as possible.
         * 必须在activity里调用此方法，传入appid、和zoneid(如果有多个就传入多个)，调用此方法后才能拉取到广告，不然会失败。
         * 调用此方法默认会开始缓存广告。
         */
        AdColony.configure( this, "app9a9ce3f173ef40539d", "vz568b78984af8487e95" );
        ad.setAdListener(new RewardVedioAdListener() {

            @Override
            public void onAdLoaded() {
                Log.e(TAG,"激励视频加载成功");
                Toast.makeText(RewardVedioActivity.this,"激励视频加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedLoad(int errcode) {
                Log.e(TAG,"激励视频加载失败");
                Toast.makeText(RewardVedioActivity.this,"激励视频加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdShow() {
                Log.e(TAG,"激励视频开始展示");
                Toast.makeText(RewardVedioActivity.this,"激励视频开始展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {
                Log.e(TAG,"激励视频播放完成");
                Toast.makeText(RewardVedioActivity.this,"激励视频播放完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                Log.e("adSdk","激励视频点击");
                Toast.makeText(RewardVedioActivity.this,"激励视频点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                Log.e(TAG,"激励视频关闭");
                Toast.makeText(RewardVedioActivity.this,"激励视频关闭", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
      //  toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadAd() {
        if (!ad.isAdLoaded()) {
            ad.loadAd();
        }
    }

    private void show(){
        Log.e(TAG, "ad.isLoaded()" + ad.isAdLoaded());
        if (ad.isAdLoaded()) {
            ad.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
        if (ad !=  null) {
            ad.destory();
        }
    }
}
