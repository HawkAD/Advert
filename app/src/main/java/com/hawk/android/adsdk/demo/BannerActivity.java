package com.hawk.android.adsdk.demo;

import com.hawk.android.adsdk.ads.HkAdRequest;
import com.hawk.android.adsdk.ads.HkAdSize;
import com.hawk.android.adsdk.ads.HkAdView;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BannerActivity extends AppCompatActivity {
    private HkAdView mAdView;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        initActionBar();
        /**
         * 植入广告两种方式
         * 1、在xml布局文件中添加AdView
         */
        mAdView = (HkAdView)findViewById(R.id.adView);
        if(savedInstanceState != null) {
            HkAdSize hkAdSize = (HkAdSize)savedInstanceState.getParcelable("adSize");
            if (hkAdSize != null) {
                mAdView.setAdSize(hkAdSize);
            }
        }
        mAdView.setAdUnitId(getString(R.string.banner_ad_unitid));
        HkAdRequest request = new HkAdRequest.Builder().setLocation(getLocation(this)).build();
        mAdView.loadAd(request);

        mWebView = (WebView) findViewById(R.id.banner_web_content);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
      //  mWebView.loadUrl("http://m.tcl.com/");
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

    private Location getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if (location == null) {
                //获取NETWORK支持
                location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            }
        } catch (SecurityException e) {

        }
        return location;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mAdView != null) {
            this.mAdView.destory();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.mAdView != null) {
            this.mAdView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mAdView != null) {
            this.mAdView.resume();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menus,menu);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("adSize",mAdView.getAdSize());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
