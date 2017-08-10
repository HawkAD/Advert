package com.hawk.android.adsdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
/**
 * Created by tzh on 2016/11/3.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBannerBtn;
    private Button mInterstitialBtn;
    private Button mNativeBtn;
    private Button mNativePoolBtn;
    private Button mNativeDefaultBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBannerBtn = (Button)this.findViewById(R.id.btn_banner);
        mInterstitialBtn = (Button)this.findViewById(R.id.btn_interstitial);
        mNativeBtn = (Button)this.findViewById(R.id.btn_native);
        mNativePoolBtn = (Button)this.findViewById(R.id.btn_native_pool);
        mNativeDefaultBtn = (Button)this.findViewById(R.id.btn_default_native);
        mBannerBtn.setOnClickListener(this);
        mInterstitialBtn.setOnClickListener(this);
        mNativeBtn.setOnClickListener(this);
        mNativePoolBtn.setOnClickListener(this);
        mNativeDefaultBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_banner:
                Intent intent = new Intent(this,BannerActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btn_interstitial:
                Intent intent1 = new Intent(this,InterstitialActivity.class);
                this.startActivity(intent1);
                break;
            case R.id.btn_native:
                Intent intent2 = new Intent(this,NativeAdSpreadSampleActivity.class);
                this.startActivity(intent2);
                break;
            case R.id.btn_native_pool:
                Intent intent3 = new Intent(this,NativeAdPoolActiveity.class);
                this.startActivity(intent3);
            case R.id.btn_default_native:
                this.startActivity(new Intent(this,NativeAdDefaultActivity.class));
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
