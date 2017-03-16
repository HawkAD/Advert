package com.hawk.android.adsdk.demo;

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
        //service启动多进程，会导致初始化多次的问题，可以通过下面的代码解决
        String curProcess = getProcessName(this, Process.myPid());
        if (!getPackageName().equals(curProcess)) {
            return;
        }
        init();
    }

    private void init() {
        //init the ad sdk,must init before ad request
        HkMobileAds.initialize(getApplicationContext(),getString(R.string.app_key),getString(R.string.banner_ad_unitid),getString(R.string.intersitial_ad_unitid), getString(R.string.native_ad_unitid));
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
