# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\software\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn com.hawk.android.adsdk.ads.mediator.**
-keep class com.hawk.android.adsdk.ads.**{*;}
-keep class com.tcl.mediator.**{*;}
-keep class com.hawk.ownadsdk.**{*;}
-keep class com.flurry.** { *; }

-keep class com.my.target.** {*;}
#-kepp class com.google.android.gms.ads.**{*;}
-keep class com.google.android.gms.ads.MobileAds{
    <fields>;
    <methods>;
}
-keep class com.duapps.ad.base.DuAdNetwork{
    <fields>;
    <methods>;
}
-keep class com.mopub.mobileads.MoPubActivity{
    <fields>;
    <methods>;
}
-keep class com.mopub.mobileads.MraidActivity{
    <fields>;
    <methods>;
}
-keep class com.mopub.common.MoPubBrowser{
    <fields>;
    <methods>;
}
-keep class com.mopub.mobileads.MraidVideoPlayerActivity{
    <fields>;
    <methods>;
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
 public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
 }

-keepattributes Signature  #过滤泛型
-keepattributes *Annotation* # 过滤注解
-keepattributes InnerClasses #
-dontoptimize