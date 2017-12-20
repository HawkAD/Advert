package com.hawk.android.adsdk.demo.view;

import com.yandex.mobile.ads.nativeads.Rating;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RatingBar;

/**
 * Created by guangshun on 2017/12/7.
 */

public class YandexRatingView extends RatingBar implements Rating {

    public YandexRatingView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public YandexRatingView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public YandexRatingView(final Context context) {
        super(context);
    }
    @Override
    public void setRating(Float aFloat) {

    }

    @Override
    public float getRating() {
        return super.getRating();
    }
}
