package com.hawk.android.adsdk.demo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.AdChoicesView;
import com.hawk.android.adsdk.demo.R;

import java.util.ArrayList;
import java.util.List;

public class NativeBannerViewBuild {

    /*final protected Context mContext;
    protected View adView;

    public static View createAdView(Context context, ViewGroup nativeBannerAdContainer, Object Ad) {
        NativeBannerViewBuild build = new NativeBannerViewBuild(context);
        return build.initAdView(Ad, nativeBannerAdContainer);
    }

    public NativeBannerViewBuild(Context context) {
        mContext = context;
    }

    public View initAdView(Object ad, ViewGroup nativeBannerAdContainer) {
        if (ad instanceof NativeBannerAd) {//facebook native banner
            LayoutInflater inflater = LayoutInflater.from(mContext);
            // Inflate the Ad view.  The layout referenced is the one you created in the last step.
            adView = inflater.inflate(R.layout.facebook_native_banner_layout, nativeBannerAdContainer, false);
            NativeBannerAd nativeBannerAd = (NativeBannerAd) ad;
            setFacebookNativeBanner(nativeBannerAd);
        }
        return adView;
    }

    private void setFacebookNativeBanner(NativeBannerAd nativeBannerAd) {
        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = (RelativeLayout) adView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(mContext, nativeBannerAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = (TextView) adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = (AdIconView) adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }*/


}
