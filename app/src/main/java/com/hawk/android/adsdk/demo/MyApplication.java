package com.hawk.android.adsdk.demo;

import com.flurry.android.FlurryAgent;
import com.hawk.android.adsdk.ads.HkMobileAds;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import java.util.List;

/**
 * Created by tzh on 2016/11/3.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * service启动多进程，会导致初始化多次的问题，可以通过下面的代码解决
         */
        String curProcess = getProcessName(this, Process.myPid());
        if (!getPackageName().equals(curProcess)) {
            return;
        }
        init();
    }

    private void init() {
        /**
         * 初始化flurry
         */
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.init(this, "S2D44W5RVZ5QPSH42W5S");
        /**
         * 可以通过这个方法打开和关闭日志,用关键字“adSdk”可过滤广告的关键字
         */
        if (BuildConfig.DEBUG) {
            HkMobileAds.openLog();
        }
        /**
         * sdk初始化方法，拉取并刷新后台配置的相关信息，拉取成功后会保存在本地，请确保在load广告之前调用。
         * 参数：
         *    1、context
         *    2、appkey
         *    3、聚合平台广告位ID，有多个就需要传多个
         */
        HkMobileAds.initialize(getApplicationContext(),getString(R.string.app_key),
                getString(R.string.banner_ad_unitid),getString(R.string.intersitial_ad_unitid), getString(R.string.native_ad_unitid));
        /**
         * 这个方法可以判断本地是否有配置信息
         */
        HkMobileAds.isInitConfigSuccess(getApplicationContext());
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
