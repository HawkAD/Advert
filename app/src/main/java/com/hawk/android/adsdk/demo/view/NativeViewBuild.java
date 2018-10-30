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

import com.applovin.nativeAds.AppLovinNativeAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkUtils;
import com.avocarrot.sdk.nativeassets.model.AdChoice;
import com.criteo.render.ViewBinder;
import com.criteo.view.CriteoNativeAd;
import com.duapps.ad.DuNativeAd;
import com.etap.Ad;
import com.etap.EtapNative;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.NativeAd;
import com.flurry.android.ads.FlurryAdNative;
import com.flurry.android.ads.FlurryAdNativeAsset;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.hawk.android.adsdk.ads.HKNativeAd;
import com.hawk.android.adsdk.ads.mediator.implAdapter.altamob.AltamobAd;
import com.hawk.android.adsdk.ads.mediator.implAdapter.glispa.GlispaNativeAssetsAd;
import com.hawk.android.adsdk.ads.nativ.HawkNativeAd;
import com.hawk.android.adsdk.demo.R;
import com.hawk.android.adsdk.demo.applovin.AppLovinCarouselViewSettings;
import com.hawk.android.adsdk.demo.applovin.InlineCarouselCardMediaView;
import com.hawk.android.adsdk.demo.applovin.InlineCarouselCardState;
import com.hawk.ownadsdk.HkOwnNativeAd;
import com.hawk.ownadsdk.nativeview.NativeAdView;
import com.hawk.ownadsdk.nativeview.NativeAdViewListenter;
import com.inmobi.ads.InMobiNative;
import com.mobvista.msdk.nativex.view.MVMediaView;
import com.mobvista.msdk.out.Campaign;
import com.my.target.nativeads.banners.NativePromoBanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pingstart.adsdk.model.BaseNativeAd;
import com.smartadserver.android.library.model.SASNativeAdElement;
import com.squareup.picasso.Picasso;
import com.yandex.mobile.ads.nativeads.NativeAdAssets;
import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.NativeAdImage;

import java.util.ArrayList;
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
        } else if (ad instanceof DuNativeAd) {//baidu native ad
            /**
             * 百度广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.baidu_native_ad_layout, null);
            DuNativeAd nativeAd = (DuNativeAd) ad;
            setBaiduAdView(nativeAd);
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
        } else if (ad instanceof List && ((List) ad).size() > 0 && ((List) ad).get(0) instanceof HkOwnNativeAd) {//hawk native
            /**
             * Hawk 直客广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.native_ad_layout, null);
            List<HkOwnNativeAd> nativeAds = (List<HkOwnNativeAd>) ad;
            setMobpalmAdView(nativeAds.get(0));
        } else if (ad instanceof FlurryAdNative) {
            /**
             * flurry广告
             */
            FlurryAdNative mAdNative = (FlurryAdNative) ad;
            mNativeAdView = View.inflate(mContext, R.layout.flurry_native_ad_layout, null);
            mAdNative.setTrackingView(mNativeAdView);
            setFlurryAdView(mAdNative);
        } else if (ad instanceof EtapNative) {//batmobi平台
            mNativeAdView = View.inflate(mContext, R.layout.batmobi_native_ad_layout, null);
            setBatmobiAdView((EtapNative) ad);
        } else if (ad instanceof GlispaNativeAssetsAd) {//Glispa平台
            mNativeAdView = View.inflate(mContext, R.layout.glispa_native_ad_layout, null);
            setGlispaAdView((GlispaNativeAssetsAd) ad);
        } else if (ad instanceof com.yandex.mobile.ads.nativeads.NativeAppInstallAd) {//yandex native install ad
            /**
             * yandex native install广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.yandex_native_install_ad_layout, null);
            setYandexInstallAdView((com.yandex.mobile.ads.nativeads.NativeAppInstallAd) ad);
        } else if (ad instanceof com.yandex.mobile.ads.nativeads.NativeContentAd) {//yandex native content ad
            /**
             * yandex native content 广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.yandex_native_content_ad_layout, null);
            setYandexContentAdView((com.yandex.mobile.ads.nativeads.NativeContentAd) ad);
        } else if (ad instanceof Campaign) {
            /**
             * mobvista广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.mobvista_native_ad_layout, null);
            setMobvistaAdView((Campaign) ad);
        } else if (ad instanceof SASNativeAdElement) {
            /**
             * smart广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.smart_native_ad_layout, null);
            setSmartNativeAdView((SASNativeAdElement) ad);
        }else if (ad instanceof AltamobAd) {
            /**
             * AltamobAd广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.hawk_native_ad_layout, null);
            setAltamobNativeAdView((AltamobAd) ad);
        }else if(ad instanceof InMobiNative){
            /**
             * InMobi广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.inmobi_native_ad, null);
            setInmobiNativeAdView((InMobiNative) ad);
        } else if (ad instanceof com.mobpower.api.Ad) {
            /**
             * MobPower广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.layout_mobpower_native_ad, null);
            setMobPowerNativeAdView((com.mobpower.api.Ad) ad);
        } else if (ad instanceof AppLovinNativeAd) {
            /**
             * AppLovin广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.layout_applovin_native_ad, null);
            setAppLovinNativeAdView((AppLovinNativeAd)ad);
        }else if (ad instanceof BaseNativeAd) {
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
        }else if (ad instanceof com.u3k.app.external.NativeAd) {
            /**
             * U3K 广告
             */
            mNativeAdView = View.inflate(mContext, R.layout.layout_adflash_native_ad, null);
            setU3KNativeAdView((com.u3k.app.external.NativeAd)ad);
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

    private void setU3KNativeAdView(com.u3k.app.external.NativeAd ad) {
        View container = mNativeAdView.findViewById(R.id.ad_container);
        TextView titleView = (TextView) mNativeAdView.findViewById(R.id.ad_headline);
        TextView subtitleView = (TextView) mNativeAdView.findViewById(R.id.sub_title);
        TextView bodyView = (TextView) mNativeAdView.findViewById(R.id.ad_body);
        com.u3k.app.external.AdIconView adIcon = (com.u3k.app.external.AdIconView) mNativeAdView.findViewById(R.id.ad_icon);
        com.u3k.app.external.MediaView mediaView = (com.u3k.app.external.MediaView) mNativeAdView.findViewById(R.id.ad_media);
        //设置广告标题等
        String title = ad.getAdTitle();
        String spon = ad.getAdSubtitle();
        String body = ad.getAdBody();
        titleView.setText(title);
        subtitleView.setText(spon);
        bodyView.setText(body);
        //adchoice
        com.u3k.app.external.AdChoicesView adChoicesView = new com.u3k.app.external.AdChoicesView(mContext,ad, true);
        LinearLayout adChoiceContainer = (LinearLayout) mNativeAdView.findViewById(R.id.adchoice_frame);
        adChoiceContainer.addView(adChoicesView);
        //添加可点击的 view
        List<View> list = new ArrayList<View>();
        list.add(adIcon);
        list.add(mediaView);
        list.add(titleView);
        list.add(bodyView);
        list.add(adChoicesView);
        //调用该方法进行绑定，绑定后，广告将会展示出来
        ad.registerViewForInteraction(container, mediaView, adIcon, list);
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


    private void setAppLovinNativeAdView(final AppLovinNativeAd ad) {
        ImageView appRating = (ImageView) mNativeAdView.findViewById(R.id.appRating);
        TextView appTitleTextView = (TextView) mNativeAdView.findViewById(R.id.appTitleTextView);
        TextView appDescriptionTextView = (TextView) mNativeAdView.findViewById(R.id.appDescriptionTextView);
        FrameLayout mediaViewPlaceholder = (FrameLayout) mNativeAdView.findViewById(R.id.mediaViewPlaceholder);
        ImageView appIcon = (ImageView) mNativeAdView.findViewById(R.id.appIcon);
        Button appDownloadButton = (Button) mNativeAdView.findViewById(R.id.appDownloadButton);
        appDownloadButton.setText( ad.getCtaText() );
        appRating.setImageDrawable( getStarRatingDrawable( ad.getStarRating() ) );

        appTitleTextView.setText( ad.getTitle() );
        appDescriptionTextView.setText( ad.getDescriptionText() );
        AppLovinSdkUtils.safePopulateImageView( appIcon, Uri.parse( ad.getIconUrl() ), AppLovinSdkUtils.dpToPx( mContext, AppLovinCarouselViewSettings.ICON_IMAGE_MAX_SCALE_SIZE ) );
        InlineCarouselCardMediaView mediaView = new InlineCarouselCardMediaView(mContext);
        mediaView.setAd(ad);
        mediaView.setCardState( new InlineCarouselCardState() );
        mediaView.setSdk( AppLovinSdk.getInstance(mContext) );
        mediaView.setUiHandler( new Handler( Looper.getMainLooper() ) );
        mediaView.setUpView();
        mediaView.autoplayVideo();

        mediaViewPlaceholder.removeAllViews();
        mediaViewPlaceholder.addView(mediaView);
    }

    private Drawable getStarRatingDrawable(final float starRating)
    {
        final String sanitizedRating = Float.toString( starRating ).replace( ".", "_" );
        final String resourceName = "applovin_star_sprite_" + sanitizedRating;
        final int drawableId = mContext.getResources().getIdentifier( resourceName, "mipmap", mContext.getPackageName() );

        return mContext.getResources().getDrawable( drawableId );
    }

    private void setMobPowerNativeAdView(com.mobpower.api.Ad adReg) {
        ImageView nativeImage = (ImageView) mNativeAdView.findViewById(R.id.native_ad_big_image);
        ImageView adIcon = (ImageView) mNativeAdView.findViewById(R.id.native_ad_image);
        TextView titleView = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
        TextView descView = (TextView) mNativeAdView.findViewById(R.id.native_ad_desc);
        TextView installBtn = (TextView) mNativeAdView.findViewById(R.id.native_ad_install_btn);
        ImageView faceBookAdIcon = (ImageView) mNativeAdView.findViewById(R.id.fb_native_ad_logo);
        mNativeAdView.setVisibility(View.INVISIBLE);
        if (adReg != null) {
            if (adReg.getNativeType() == com.mobpower.api.Ad.MP_NATIVE_TYPE) {
                faceBookAdIcon.setVisibility(View.GONE);
            } else {
                faceBookAdIcon.setVisibility(View.VISIBLE);
                faceBookAdIcon.setImageURI(Uri.parse(adReg.getAdChoiceIconUrl()));
            }
            titleView.setText(adReg.getTitle());
            descView.setText(adReg.getBody());
            Uri uri = Uri.parse(adReg.getImageUrl());
            Uri uri1 = Uri.parse(adReg.getIconUrl());
            //ImageView 无法加载Uri资源
            Picasso.with(mContext).load(uri).into(nativeImage);
            Picasso.with(mContext).load(uri1).into(adIcon);
            mNativeAdView.setVisibility(View.VISIBLE);
            installBtn.setText(TextUtils.isEmpty(adReg.getCta()) ? "Install" : adReg.getCta());
        }
    }


    private void setBatmobiAdView(EtapNative etapNatives) {
        if (null == mNativeAdView || null == etapNatives || null == etapNatives.getAds() || 1 > etapNatives.getAds().size()) {
            return;
        }
        TextView appName = (TextView) mNativeAdView.findViewById(R.id.app_name);
        TextView appDes = (TextView) mNativeAdView.findViewById(R.id.app_des);
        ImageView icon = (ImageView) mNativeAdView.findViewById(R.id.icon);
        ImageView img = (ImageView) mNativeAdView.findViewById(R.id.img_big);
        TextView install = (TextView) mNativeAdView.findViewById(R.id.btn_install);
        TextView campId = (TextView) mNativeAdView.findViewById(R.id.text_camp_id);
        Ad item = etapNatives.getAds().get(0);
        if (null != item) {
            imageLoader.displayImage(item.getIcon(), icon);
            List<String> creatives = item.getCreatives(Ad.AD_CREATIVE_SIZE_1200x627);//聚合SDk里面请求了两种尺寸的广告（Ad.AD_CREATIVE_SIZE_1200
            // x627和Ad.AD_CREATIVE_SIZE_320X200)请根据广告使用的场景大小去取对应合适的图片.
            if (null == creatives || creatives.size() == 0) {//如果没有这个尺寸的图片，正常情况下应该不会出现，各app根据情况自己选择是否加上这个处理逻辑
                creatives = item.getCreatives(Ad.AD_CREATIVE_SIZE_320X200);
            }
            if (null != creatives && creatives.size() > 0 && null != creatives.get(0)) {
                imageLoader.displayImage(creatives.get(0), img);
            }

            appName.setText(item.getName());
            appDes.setText(item.getDescription());
            install.setText(item.getAdCallToAction());
            campId.setText(new StringBuilder("CampId:").append(item.getCampId()).toString());
        }
        etapNatives.registerView(mNativeAdView, item);
    }

    /**
     * 百度广告
     *
     * @param nativeAd
     */
    private void setBaiduAdView(DuNativeAd nativeAd) {
        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
        ImageView nativeAdImage = (ImageView) mNativeAdView.findViewById(R.id.native_ad_image);
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getTitle());
        nativeAdBody.setText(nativeAd.getShortDesc());
        nativeAdCallToAction.setText(nativeAd.getCallToAction());
        // Download and display the ad icon.
        if (nativeAd.getIconUrl() != null) {
            imageLoader.displayImage(nativeAd.getIconUrl(), nativeAdIcon);
        }

        if (nativeAd.getImageUrl() != null) {
            imageLoader.displayImage(nativeAd.getImageUrl(), nativeAdImage);
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

        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
//        ImageView nativeAdImage = (ImageView) mNativeAdView.findViewById(R.id.native_ad_media);
        com.facebook.ads.MediaView nativeAdMedia = (com.facebook.ads.MediaView) mNativeAdView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = (TextView) mNativeAdView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.native_ad_call_to_action);

        if (nativeAd.getAdTitle() != null) {
            nativeAdTitle.setText(nativeAd.getAdTitle());
        }
        if (nativeAd.getAdSocialContext() != null) {
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        }
        if (nativeAd.getAdBody() != null) {
            nativeAdBody.setText(nativeAd.getAdBody());
        }
        if (nativeAd.getAdCallToAction() != null) {
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        }

        // Download and display the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        if (adIcon != null) {
            NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);
        }
        // Download and display the cover image.
        nativeAdMedia.setNativeAd(nativeAd);
//        NativeAd.Image adImage = nativeAd.getAdCoverImage();
//        if (adImage != null) {
//            NativeAd.downloadAndDisplayImage(adImage, nativeAdImage);
//        }
        /**
         * facebook广告必须要加AdChoicesView
         */
        LinearLayout adChoicesContainer = (LinearLayout) mNativeAdView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(mContext, nativeAd, true);
        adChoicesContainer.addView(adChoicesView);

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
     * 直客广告
     *
     * @param nativeAd
     * @return
     */
    private View setMobpalmAdView(HkOwnNativeAd nativeAd) {
        if (null == nativeAd || null == mNativeAdView) {
            return null;
        }
        NativeAdView adView = nativeAd.getAdView(mContext, mNativeAdView);
        adView.setTitleID(R.id.or_title).setDescriptionViewID(R.id.or_content);
        adView.setIconViewID(R.id.or_icon).setImageViewID(R.id.or_img);
        adView.setActionID(R.id.or_btn);
        adView.setViewListenter(new NativeAdViewListenter() {
            @Override
            public void onImpression() {
            }

            @Override
            public void onClick() {
            }
        });
        return mNativeAdView;
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
     * flurry渠道
     *
     * @param nativeAd
     */
    private void setFlurryAdView(FlurryAdNative nativeAd) {
        try {
            if (null == mNativeAdView || null == imageLoader || null == nativeAd) {
                return;
            }
            loadAdAssetInView(nativeAd, "headline", (TextView) mNativeAdView.findViewById(R.id.ad_title));
            loadAdAssetInView(nativeAd, "summary", (TextView) mNativeAdView.findViewById(R.id.ad_summary));
            loadAdAssetInView(nativeAd, "source", (TextView) mNativeAdView.findViewById(R.id.ad_publisher));
            loadAdAssetInView(nativeAd, "secHqBrandingLogo", (ImageView) mNativeAdView.findViewById(R.id.sponsored_image));
//            imageLoader.displayImage(nativeAd.getAsset("secHqBrandingLogo").getValue(), (ImageView) mNativeAdView.findViewById(R.id.sponsored_image));
            if (nativeAd.isVideoAd()) {
                mNativeAdView.findViewById(R.id.ad_video).setVisibility(View.VISIBLE);
                mNativeAdView.findViewById(R.id.ad_image).setVisibility(View.GONE);
                loadAdAssetInView(nativeAd, "videoUrl", (ViewGroup) mNativeAdView.findViewById(R.id.ad_video));

            } else {

                mNativeAdView.findViewById(R.id.ad_video).setVisibility(View.GONE);
                mNativeAdView.findViewById(R.id.ad_image).setVisibility(View.VISIBLE);
                loadAdAssetInView(nativeAd, "secHqImage", (ImageView) mNativeAdView.findViewById(R.id.ad_image));
//                imageLoader.displayImage(nativeAd.getAsset("secHqImage").getValue(), (ImageView) mNativeAdView.findViewById(R.id.ad_image));
            }

        } catch (Exception e) {
        }
    }

    private void loadAdAssetInView(FlurryAdNative adNative, String assetName, View view) {
        FlurryAdNativeAsset adNativeAsset = adNative.getAsset(assetName);
        if (adNativeAsset != null) {
            adNativeAsset.loadAssetIntoView(view);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void setGlispaAdView(GlispaNativeAssetsAd nativeAd) {
        if (null == mNativeAdView || null == nativeAd) {
            return;
        }
        final List<View> clickableViews = new ArrayList<>();
        TextView title = (TextView) mNativeAdView.findViewById(R.id.native_assets_ad_title);
        TextView body = (TextView) mNativeAdView.findViewById(R.id.native_assets_ad_body);
        TextView adChoiceText = (TextView) mNativeAdView.findViewById(R.id.native_assets_ad_choices_caption);
        TextView cta = (TextView) mNativeAdView.findViewById(R.id.native_assets_ad_call_to_action);
        ImageView icon = (ImageView) mNativeAdView.findViewById(R.id.native_assets_ad_icon);
        ImageView image = (ImageView) mNativeAdView.findViewById(R.id.native_assets_ad_image);
        ImageView adChoiceIcon = (ImageView) mNativeAdView.findViewById(R.id.native_assets_ad_choices_icon);
        RatingBar rating = (RatingBar) mNativeAdView.findViewById(R.id.native_assets_ad_star_rating);


        if (title != null) {
            title.setText(nativeAd.getNativeAssets().getTitle());
            clickableViews.add(title);
        }
        if (body != null) {
            body.setText(nativeAd.getNativeAssets().getText());
        }
        if (icon != null) {
            renderImageView(icon, nativeAd.getNativeAssets().getIcon());
            clickableViews.add(icon);
        }
        if (image != null) {
            renderImageView(image, nativeAd.getNativeAssets().getImage());
            clickableViews.add(image);
        }
        if (cta != null) {
            cta.setText(nativeAd.getNativeAssets().getCallToAction());
            clickableViews.add(cta);
        }
        final AdChoice adChoice = nativeAd.getNativeAssets().getAdChoice();
        if (adChoice != null) {
            if (adChoiceIcon != null) {
                adChoiceIcon.setImageDrawable(adChoice.getIcon().getDrawable());
                nativeAd.getNativeAssetsAd().registerAdChoiceViewForClick(adChoiceIcon);
            }
            if (adChoiceText != null) {
                adChoiceText.setText(adChoice.getIconCaption());
                nativeAd.getNativeAssetsAd().registerAdChoiceViewForClick(adChoiceText);
            }
        }
        if (rating != null) {
            renderRatingBarView(rating, nativeAd.getNativeAssets().getRating());
        }
        nativeAd.getNativeAssetsAd().registerViewsForClick(clickableViews);

    }

    private void renderImageView(final ImageView view, final com.avocarrot.sdk.nativeassets.model.Image image) {
        if (image != null) {
            view.setVisibility(View.VISIBLE);
            view.setAdjustViewBounds(true);
            final int width = image.getWidth();
            final int height = image.getHeight();
            if ((width > 0) && (height > 0)) {
                view.setMaxWidth(width);
                view.setMaxHeight(height);
            }
            view.setImageDrawable(image.getDrawable());
        } else {
            view.setImageDrawable(null);
            view.setVisibility(View.GONE);
        }
    }

    private void renderRatingBarView(final RatingBar view, final com.avocarrot.sdk.nativeassets.model.Rating starRating) {
        view.setStepSize(0.1F);
        view.setIsIndicator(true);
        if (starRating != null) {
            view.setNumStars((int) starRating.getScale());
            view.setRating((float) starRating.getValue());
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * Yandex content ad
     *
     * @param nativeContentAd
     */
    private void setYandexContentAdView(com.yandex.mobile.ads.nativeads.NativeContentAd nativeContentAd) {
        if (null == mNativeAdView && !(mNativeAdView instanceof com.yandex.mobile.ads.nativeads.NativeContentAdView)) {
            return;
        }
        com.yandex.mobile.ads.nativeads.NativeContentAdView nativeContentAdView = (com.yandex.mobile.ads.nativeads.NativeContentAdView) mNativeAdView;
        nativeContentAdView.setAgeView((TextView) nativeContentAdView.findViewById(R.id.content_age));
        nativeContentAdView.setBodyView((TextView) nativeContentAdView.findViewById(R.id.content_body));
        nativeContentAdView.setCallToActionView((Button) nativeContentAdView.findViewById(R.id.content_call_to_action));
        nativeContentAdView.setDomainView((TextView) nativeContentAdView.findViewById(R.id.content_domain));
        nativeContentAdView.setIconView((ImageView) nativeContentAdView.findViewById(R.id.content_favicon));
        nativeContentAdView.setSponsoredView((TextView) nativeContentAdView.findViewById(R.id.content_sponsored));
        nativeContentAdView.setTitleView((TextView) nativeContentAdView.findViewById(R.id.content_title));
        nativeContentAdView.setWarningView((TextView) nativeContentAdView.findViewById(R.id.content_warning));
        configureContentAdImages(nativeContentAdView, nativeContentAd);
        try {
            nativeContentAd.bindContentAd(nativeContentAdView);
        } catch (NativeAdException exception) {
        }
    }

    /**
     * Yandex install ad
     *
     * @param nativeAppInstallAd
     */
    private void setYandexInstallAdView(com.yandex.mobile.ads.nativeads.NativeAppInstallAd nativeAppInstallAd) {
        if (null == mNativeAdView && !(mNativeAdView instanceof com.yandex.mobile.ads.nativeads.NativeAppInstallAdView)) {
            return;
        }
        com.yandex.mobile.ads.nativeads.NativeAppInstallAdView mAppInstallAdView = (com.yandex.mobile.ads.nativeads.NativeAppInstallAdView) mNativeAdView;
        mAppInstallAdView.setAgeView((TextView) mAppInstallAdView.findViewById(R.id.appinstall_age));
        mAppInstallAdView.setBodyView((TextView) mAppInstallAdView.findViewById(R.id.appinstall_body));
        mAppInstallAdView.setCallToActionView((Button) mAppInstallAdView.findViewById(R.id.appinstall_call_to_action));
        mAppInstallAdView.setDomainView((TextView) mAppInstallAdView.findViewById(R.id.appinstall_domain));
        mAppInstallAdView.setIconView((ImageView) mAppInstallAdView.findViewById(R.id.appinstall_icon));
        mAppInstallAdView.setImageView((ImageView) mAppInstallAdView.findViewById(R.id.appinstall_image));
        mAppInstallAdView.setPriceView((TextView) mAppInstallAdView.findViewById(R.id.appinstall_price));
        mAppInstallAdView.setRatingView((YandexRatingView) mAppInstallAdView.findViewById(R.id.appinstall_rating));
        mAppInstallAdView.setReviewCountView((TextView) mAppInstallAdView.findViewById(R.id.appinstall_review_count));
        mAppInstallAdView.setSponsoredView((TextView) mAppInstallAdView.findViewById(R.id.appinstall_sponsored));
        mAppInstallAdView.setTitleView((TextView) mAppInstallAdView.findViewById(R.id.appinstall_title));
        mAppInstallAdView.setWarningView((TextView) mAppInstallAdView.findViewById(R.id.appinstall_warning));
        try {
            nativeAppInstallAd.bindAppInstallAd(mAppInstallAdView);
        } catch (NativeAdException e) {
        }
    }

    private void configureContentAdImages(final com.yandex.mobile.ads.nativeads.NativeContentAdView nativeContentAdView, final com.yandex.mobile.ads.nativeads.NativeContentAd nativeContentAd) {
        final ImageView image = (ImageView) nativeContentAdView.findViewById(R.id.content_image);
        final ImageView largeImage = (ImageView) nativeContentAdView.findViewById(R.id.content_large_image);

        final NativeAdAssets nativeAdAssets = nativeContentAd.getAdAssets();
        final NativeAdImage nativeAdImage = nativeAdAssets.getImage();
        if (nativeAdImage != null) {
            final int imageWidth = nativeAdImage.getWidth();
            if (imageWidth >= 450) {
                nativeContentAdView.setImageView(largeImage);
                image.setVisibility(View.GONE);
            } else {
                nativeContentAdView.setImageView(image);
                largeImage.setVisibility(View.GONE);
            }
        }
    }

    /**
     * mobvista native layout
     *
     * @param nativeAd
     */
    private void setMobvistaAdView(Campaign nativeAd) {
        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
        MVMediaView nativeMediaView = (MVMediaView) mNativeAdView.findViewById(R.id.native_ad_media);
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAppName());
        nativeAdBody.setText(nativeAd.getAppDesc());
        nativeAdCallToAction.setText(nativeAd.getAdCall());
        // Download and display the ad icon.
        if (nativeAd.getIconUrl() != null) {
            imageLoader.displayImage(nativeAd.getIconUrl(), nativeAdIcon);
        }

        nativeMediaView.setNativeAd(nativeAd);
        //如果你设置了这个TRUE，用户可以在观看视频时，点击屏幕进行全屏切换。
        nativeMediaView.setIsAllowFullScreen(true);
//        设置当Native Video广告位的视频达到可播放状态之后，是否可以由图片刷新成视频
//        true表示允许刷新，false表示不允许刷新，默认是true
        nativeMediaView.setAllowVideoRefresh(true);
//        设置播放完成之后是否允许循环播放
//        true表示允许，false表示不允许，默认是true
        nativeMediaView.setAllowLoopPlay(true);
//        设置是否允许屏幕 横竖屏切换
//        true表示允许，false表示不允许，默认是true
        nativeMediaView.setAllowScreenChange(true);
//        if (nativeAd.getImageUrl() != null) {
//            imageLoader.displayImage(nativeAd.getImageUrl(), nativeAdImage);
//        }
    }


    /**
     * smart native ad layout
     *
     * @param nativeAd
     */
    private void setSmartNativeAdView(SASNativeAdElement nativeAd) {
        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
        ImageView nativeAdImage = (ImageView) mNativeAdView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = (TextView) mNativeAdView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.native_ad_call_to_action);
        // Set the Text.
        nativeAdTitle.setText(nativeAd.getTitle());
        nativeAdSocialContext.setText(nativeAd.getSubtitle());
        nativeAdBody.setText(nativeAd.getBody());
        nativeAdCallToAction.setText(nativeAd.getCalltoAction());

        // Download and display the ad icon.
        if (nativeAd.getIcon() != null) {
            imageLoader.displayImage(nativeAd.getIcon().getUrl(), nativeAdIcon);
        }

        Log.e("adSdk", "nativeAd.getCoverImage().getUrl() : " + nativeAd.getCoverImage());
        if (nativeAd.getCoverImage() != null) {
            imageLoader.displayImage(nativeAd.getCoverImage().getUrl(), nativeAdImage);
        }
        View[] clickViews = new View[]{nativeAdIcon, nativeAdTitle, nativeAdCallToAction};

        nativeAd.registerView(mNativeAdView, clickViews);
    }

    /**
     * Altamob
     * @param ad
     */
    private void setAltamobNativeAdView(AltamobAd ad) {
        ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(R.id.native_ad_title);
        ImageView nativeAdImage = (ImageView) mNativeAdView.findViewById(R.id.native_ad_image);
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(ad.getAd().getTitle());
        nativeAdBody.setText(ad.getAd().getDesc());
        nativeAdCallToAction.setText(ad.getAd().getAdAction());
        if (ad.getAd().getIcon_url() != null)
            imageLoader.displayImage(ad.getAd().getIcon_url(), nativeAdIcon);
        if (ad.getAd().getCover_url() != null)
            imageLoader.displayImage(ad.getAd().getCover_url(), nativeAdImage);
        List<View> clickViews = new ArrayList<>();
        clickViews.add(nativeAdIcon);
        clickViews.add(nativeAdImage);
        clickViews.add(nativeAdCallToAction);
        ad.getAdNatived().registerViewForInteraction(ad.getAd(), clickViews);
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
