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
-keep class com.hawk.android.adsdk.ads.**{*;}
-keep class com.tcl.mediator.**{*;}
 -kepp class com.google.android.gms.ads.**{*;}
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

-keepattributes Signature  #过滤泛型
-keepattributes *Annotation* # 过滤注解
-keepattributes InnerClasses #
-dontoptimize