package com.hawk.android.adsdk.demo;

import com.hawk.android.adsdk.ads.HkAdListener;
import com.hawk.android.adsdk.ads.HkInterstitialAd;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class InterstitialActivity extends AppCompatActivity {
    private Button mButtonLoadAd;
    private Button mButtonShowAd;
    private HkInterstitialAd ad;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        ad = new HkInterstitialAd(this);
        ad.setAdUnitId(getString(R.string.intersitial_ad_unitid));
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
        ad.setAdListner(new HkAdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(InterstitialActivity.this,"插屏加载成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdFailedLoad(int errcode) {
                super.onAdFailedLoad(errcode);
            }

            @Override
            public void onAdShowed() {
                super.onAdShowed();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });
        mWebView = (WebView) findViewById(R.id.web_content);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
       // mWebView.loadUrl("http://m.tcl.com/");
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadAd() {
        if (!ad.isLoaded()) {
            ad.loadAd(null);
        }
    }

    private void show(){
        Log.e("InterstitialActivity","测试广告点击没反应   是否加载了"+ad.isLoaded());
        if (ad.isLoaded()) {
            ad.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ad !=  null) {
            ad.close();
        }
    }

    public void showInterstitial(View view){
        show();
    }
}
