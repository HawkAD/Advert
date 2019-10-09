package com.hawk.android.adsdk.demo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.criteo.render.ViewBinder;
import com.criteo.view.CriteoNativeAd;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdIconView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.hawk.android.adsdk.ads.HKNativeAd;
import com.hawk.android.adsdk.ads.nativ.HawkNativeAd;
import com.hawk.android.adsdk.demo.R;
import com.inmobi.ads.InMobiNative;
import com.my.target.nativeads.banners.NativePromoBanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pingstart.adsdk.model.BaseNativeAd;
import java.util.List;

import ads.com.adsdk.admanagers.adutils.AdManager;

/**
 * Created by tzh on 2016/11/3.
 */
public class NativeViewBuild {

    final protected Context mContext;
    protected View mNativeAdView;

    private ImageLoader imageLoader;

    public static View createAdView(Context context, HKNativeAd nativeAd) {
        if (!nativeAd.isLoaded()) {
            return null;
        }
        NativeViewBuild build = new NativeViewBuild(context);
        Object ad = nativeAd.getAd();
        return build.initAdView(ad);
    }

    public static View createAdView(Context context, HKNativeAd nativeAd, Object Ad) {
        if (!nativeAd.isLoaded()) {
            return null;
        }
        NativeViewBuild build = new NativeViewBuild(context);
        return build.initAdView(Ad);
    }

    public NativeViewBuild(Context context) {
        mContext = context;
        imageLoader = ImageLoaderHelper.getInstance(context);
    }

    public View initAdView(Object ad) {
        /**
         * 注意事项：
         * 1、admob安装型广告必须已com.google.android.gms.ads.formats.NativeAppInstallAdView为广告元素的跟View。
         * 2、admob内容型广告必须已com.google.android.gms.ads.formats.NativeContentAdView为广告元素的跟View。
         * 3、facebook没有要求，但必须在广告中加入facebook广告的标识AdChoicesView。
         * 4、设计的广告中必须要包含"AD","Advertise","广告"等能明确表示是广告的字样和元素。
         *
         * 5、admob SDK 升级之后不再区分安装型广告和内容型广告，使用com.google.android.gms.ads.formats.UnifiedNativeAdView为广告元素的根View
         */

        if (ad instanceof UnifiedNativeAd) {
            mNativeAdView = View.inflate(mContext, R.layout.admob_native_ad, null);
            setAdMobNativeAdView((UnifiedNativeAd)ad,(UnifiedNativeAdView)mNativeAdView);
        }/*else if (ad instanceof NativeAppInstallAd) {
            *//**
             * admob安装型广告
             *//*
            mNativeAdView = View.inflate(mContext, R.layout.admob_native_ad_layout_install, null);
            NativeAppInstallAd installAd = (NativeAppInstallAd) ad;
            populateAppInstallAdView(installAd, (NativeAppInstallAdView) mNativeAdView);
        } else if (ad instanceof NativeContentAd) {
            *//**
             * admob内容型广告
             *//*
            mNativeAdView = View.inflate(mContext, R.layout.admob_native_ad_layout_content, null);
            mNativeAdView = mNativeAdView.findViewById(R.id.admob_native_content_adview);
            NativeContentAd contentAd = (NativeContentAd) ad;
            populateContentAdView(contentAd, (NativeContentAdView) mNativeAdView);
        }*/ else if (ad instanceof NativeAd) {
            /**
             * facebook广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.facebook_native_ad_layout, null);
            NativeAd nativeAd = (NativeAd) ad;
            setFacebookAdView(nativeAd);
        } else if (ad instanceof HawkNativeAd) {
            /**
             * server-to-server广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.hawk_native_ad_layout, null);
            HawkNativeAd nativeAd = (HawkNativeAd) ad;
            setHawkAdView(nativeAd);
        } else if (ad instanceof com.mopub.nativeads.NativeAd) {
            /**
             * Mopub 广告
             */
//            mNativeAdView = View.inflate(mContext, R.layout.mopub_native_ad_layout, null);
            com.mopub.nativeads.NativeAd nativeAd = (com.mopub.nativeads.NativeAd) ad;
            View convertView = nativeAd.createAdView(mContext, null);
            nativeAd.prepare(convertView);
            nativeAd.renderAdView(convertView);
            mNativeAdView = convertView;
        } else if (ad instanceof NativePromoBanner) {//vk native adV
            /**
             * VK广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.ruvk_native_ad_layout, null);
            NativePromoBanner nativeAd = (NativePromoBanner) ad;
            setRuVkAdView(nativeAd);
        } else if (ad instanceof AdManager) {//oc native ad
            /**
             * NGC广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.oc_native_ad_layout, null);
            setNgcAdView((AdManager) ad);
        } else if(ad instanceof InMobiNative){
            /**
             * InMobi广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.inmobi_native_ad, null);
            setInmobiNativeAdView((InMobiNative) ad);
        } else if (ad instanceof BaseNativeAd) {
            /**
             * SOLO 广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.layout_solo_native_ad, null);
            setSoloNativeAdView((BaseNativeAd)ad);
        } else if (ad instanceof CriteoNativeAd) {
            /**
             * Criteo 广告
             */
            setCriteoNativeAdView((CriteoNativeAd) ad);
        }
        return mNativeAdView;
    }

    private void setAdMobNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        com.google.android.gms.ads.formats.MediaView mediaView = (MediaView) adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {

        }

    }

    private void setCriteoNativeAdView(CriteoNativeAd ad) {
        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.layout_criteo_native_ad)
                .mainImageId(R.id.native_image)
                .titleId(R.id.native_title)
                .descriptionId(R.id.native_text)
                .priceId(R.id.native_price)
                .callToActionId(R.id.native_cta)
                .privacyIconImageId(R.id.native_privacy_icon_image)
                .build();
        mNativeAdView = ad.getNativeAd(viewBinder);
    }

    private void setSoloNativeAdView(BaseNativeAd ad) {
        com.pingstart.adsdk.view.MediaView mPsMediaView = (com.pingstart.adsdk.view.MediaView) mNativeAdView.findViewById(R.id.ad_media_view);
        TextView mTitleTextView = (TextView) mNativeAdView.findViewById(R.id.title);
        TextView mContentTextView = (TextView) mNativeAdView.findViewById(R.id.content);
        Button mActionButton = (Button) mNativeAdView.findViewById(R.id.ad_btn);
        ViewGroup lytNative = (ViewGroup) mNativeAdView.findViewById(R.id.test);
        lytNative.setVisibility(View.GONE);
        NativeContentAdView lytAdMobContent = (NativeContentAdView) lytNative.findViewById(R.id.lyt_admob_content);
        lytAdMobContent.setVisibility(View.GONE);
        NativeAppInstallAdView lytAdMobInstall = (NativeAppInstallAdView) lytNative.findViewById(R.id.lyt_admob_install);
        lytAdMobInstall.setVisibility(View.GONE);
        ViewGroup lytPsNative = (ViewGroup) lytNative.findViewById(R.id.ps_native);
        lytPsNative.setVisibility(View.GONE);

        com.pingstart.adsdk.view.MediaView mFbNative = new com.pingstart.adsdk.view.MediaView(mContext);
        ViewGroup main = (ViewGroup) mNativeAdView.findViewById(R.id.lyt_main);
        mFbNative.setVisibility(View.GONE);
        main.addView(mFbNative);

        if (ad != null) {
            String workName = ad.getNetworkName();
            Log.d("MY_TEST", "setSoloNativeAdView: === "+ workName);
            if (workName != null && workName.equalsIgnoreCase("facebook")) {

            } else if (workName != null && workName.equalsIgnoreCase("admob")) {

            } else {
                mFbNative.setVisibility(View.GONE);
                lytNative.setVisibility(View.VISIBLE);
                lytAdMobContent.setVisibility(View.GONE);
                lytAdMobInstall.setVisibility(View.GONE);
                lytPsNative.setVisibility(View.VISIBLE);
                mTitleTextView.setText(ad.getTitle());
                mContentTextView.setText(ad.getDescription());
                mActionButton.setText(ad.getAdCallToAction());
                mPsMediaView.setMediaInfo(ad);
            }
        }
    }





    /**
     * admob 内容型广告
     *
     * @param nativeContentAd
     * @param adView
     */
    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       NativeContentAdView adView) {
        /**
         * 注意：下面这些广告的元素必须设置，不然点击不生效
         */
        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));
        if (nativeContentAd.getHeadline() != null) {
            ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        }
        if (nativeContentAd.getBody() != null) {
            ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        }
        if (nativeContentAd.getCallToAction() != null) {
            ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        }
        if (nativeContentAd.getAdvertiser() != null) {
            ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());
        }

        /**
         * 判断广告有视频的时候显示视频广告
         * 判断广告有视频的时候显示视频广告 升级之后不需要，视频和图片都是用MediaView这一个控件
         */
        if (nativeContentAd.getVideoController().hasVideoContent()) {
            com.google.android.gms.ads.formats.MediaView coverImage = (MediaView) adView.findViewById(R.id.contentad_image);
            adView.setMediaView(coverImage);
            Log.d("MY_TEST", "populateContentAdView:  ------------ >  " + nativeContentAd.getVideoController().hasVideoContent());
//        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.contentad_media);
        /*if (nativeContentAd.getVideoController().hasVideoContent()) {
            mediaView.setVisibility(View.VISIBLE);
            coverImage.setVisibility(View.GONE);
            adView.setMediaView(mediaView);
        } else {
            adView.setImageView(coverImage);
            adView.setMediaView(coverImage);
            coverImage.setVisibility(View.VISIBLE);
            mediaView.setVisibility(View.GONE);
            List<com.google.android.gms.ads.formats.NativeAd.Image> images = nativeContentAd.getImages();
            if (images.size() > 0) {
                ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
            }
        }*/

            List<com.google.android.gms.ads.formats.NativeAd.Image> images = nativeContentAd.getImages();
            if (images != null && images.size() > 0 && adView.getImageView() != null) {
                ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
            }


            com.google.android.gms.ads.formats.NativeAd.Image logoImage = nativeContentAd.getLogo();
            if (logoImage == null) {
                adView.getLogoView().setVisibility(View.INVISIBLE);
            } else {
                ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
                adView.getLogoView().setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * admob 安装型广告
     *
     * @param nativeAppInstallAd
     * @param adView
     */
    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                          NativeAppInstallAdView adView) {
        /**
         * 注意：下面这些广告的元素必须设置，不然点击不生效
         */
        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        if (nativeAppInstallAd.getHeadline() != null) {
            ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        }
        if (nativeAppInstallAd.getBody() != null) {
            ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        }
        if (nativeAppInstallAd.getCallToAction() != null) {
            ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
        }
        if (nativeAppInstallAd.getIcon() != null) {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon().getDrawable());
        }

        /**
         * 判断广告有视频的时候显示视频广告
         */
        MediaView mediaView = (MediaView) adView.findViewById(R.id.appinstall_media);
        ImageView coverImage = (ImageView) adView.findViewById(R.id.appinstall_image);
        if (nativeAppInstallAd.getVideoController().hasVideoContent()) {
            mediaView.setVisibility(View.VISIBLE);
            coverImage.setVisibility(View.GONE);
            adView.setMediaView(mediaView);
        } else {
            adView.setImageView(coverImage);
            coverImage.setVisibility(View.VISIBLE);
            mediaView.setVisibility(View.GONE);
            List<com.google.android.gms.ads.formats.NativeAd.Image> images = nativeAppInstallAd.getImages();
            if (images.size() > 0) {
                ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
            }
        }

        // Some aren't guaranteed, however, and should be checked.
        if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
        }

        if (nativeAppInstallAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        }

        if (nativeAppInstallAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }
    }

    /**
     * facebook 广告
     * @param nativeAd
     */
    private void setFacebookAdView(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = (LinearLayout) mNativeAdView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(mContext, nativeAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = (AdIconView) mNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView nativeAdMedia = (com.facebook.ads.MediaView) mNativeAdView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = (TextView) mNativeAdView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = (TextView) mNativeAdView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());


    }


    /**
     * 内推、server端广告
     *
     * @param nativeAd
     */
    private void setHawkAdView(HawkNativeAd nativeAd) {
        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
        ImageView nativeAdImage = (ImageView) mNativeAdView.findViewById(R.id.native_ad_image);
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdDescription());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

        // Download and display the ad icon.
        if (nativeAd.getAdIcons() != null && nativeAd.getAdIcons().size() > 0) {
            HawkNativeAd.downloadAndDisplayImage(mContext, nativeAd.getAdIcons().get(0), nativeAdIcon);
        }
        nativeAdImage.setVisibility(View.GONE);
        if (nativeAd.getAdImages() != null && nativeAd.getAdImages().size() > 0) {
            HawkNativeAd.downloadAndDisplayImage(mContext, nativeAd.getAdImages().get(0), nativeAdImage);
        }
    }


    /**
     * NGC渠道
     *
     * @param nativeAd
     */
    private void setNgcAdView(AdManager nativeAd) {
        if (null == nativeAd || null == mNativeAdView) {
            return;
        }
        final ImageView or_img = (ImageView) mNativeAdView.findViewById(R.id.or_img);
        TextView or_title = (TextView) mNativeAdView.findViewById(R.id.or_title);
        TextView or_content = (TextView) mNativeAdView.findViewById(R.id.or_content);
        TextView or_btn = (TextView) mNativeAdView.findViewById(R.id.or_btn);
        final ImageView or_icon = (ImageView) mNativeAdView.findViewById(R.id.or_icon);
        or_title.setText(nativeAd.getAdTitle());
        or_content.setText(nativeAd.getAdBody());
        if (null != or_icon) {
            HawkNativeAd.downloadAndDisplayImage(mContext, nativeAd.getAdIcon(), or_icon);
        }
        if (null != or_img) {
            HawkNativeAd.downloadAndDisplayImage(mContext, nativeAd.getBigimage(), or_img);
        }
        or_btn.setText(nativeAd.getAdCallToAction());
    }


    /**
     * 俄罗斯Vk渠道
     *
     * @param nativeAd
     */
    private void setRuVkAdView(NativePromoBanner nativeAd) {
        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
        ImageView nativeAdImage = (ImageView) mNativeAdView.findViewById(R.id.native_ad_image);
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getTitle());
        nativeAdBody.setText(nativeAd.getDescription());
        nativeAdCallToAction.setText(nativeAd.getCtaText());
        // Download and display the ad icon.
        if (nativeAd.getIcon() != null) {
            com.my.target.nativeads.NativeAd.loadImageToView(nativeAd.getIcon(), nativeAdIcon);
        }

        if (nativeAd.getImage() != null) {
            com.my.target.nativeads.NativeAd.loadImageToView(nativeAd.getImage(), nativeAdImage);
        }
    }





    /**
     * inmobi
     * @param ad
     */
    private void setInmobiNativeAdView(final InMobiNative ad) {
        ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
        LinearLayout view = (LinearLayout) mNativeAdView.findViewById(R.id.view);
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.native_ad_call_to_action);

        nativeAdTitle.setText(ad.getAdTitle());
        nativeAdBody.setText(ad.getAdDescription());
        nativeAdCallToAction.setText(ad.getAdCtaText());
        if (ad.getAdIconUrl() != null)
            imageLoader.displayImage(ad.getAdIconUrl(), nativeAdIcon);

        view.removeAllViews();
        view.addView(ad.getPrimaryViewOfWidth(mContext,view,view,view.getWidth()));
        nativeAdIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.reportAdClickAndOpenLandingPage();
            }
        });
        nativeAdCallToAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.reportAdClickAndOpenLandingPage();
            }
        });
    }
}
