package com.meerkatbrowser.ad.adapter;

import com.meerkatbrowser.ad.adapter.Adapter;
import com.meerkatbrowser.ad.Listener;
import com.meerkatbrowser.ad.AdSize;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.google.android.gms.ads.MobileAds;

class Admob implement Adapter {
    InterstitialAd interstitialAd;
    AdView adView;
    public void initialize(Context context, Bundle params, Listener listener) {
        if(AudienceNetworkAds.isInitialized())
            return;
        AudienceNetworkAds
            .buildInitSettings(context)
            .withInitListener(new AudienceNetworkAds.InitListener(){
                @Override
                public void onInitialized(AudienceNetworkAds.InitResult result) {
                    if(result.isSuccess()) lisener.onSuccess();
                    else lisener.onError(result.getMessage());
                }
            })
        .initialize();
    }
    public void interstitial(Context context, Bundle params, Listener listener) {
        interstitialAd = new InterstitialAd(context, params.get("PLACEMENT_ID"));
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                listener.onError(adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if(!interstitialAd.isAdInvalidated()){
                    listener.onError("invalidate facebook ad");
                    return;
                }
                interstitial.show();
                listener.onSuccess();
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });
    }
    public void banner(Context context, ViewGroup container, ad.AdSize adsize, Bundle params, Listener listener) {
        AdSize adsize_;
        switch(adsize){
        case SMALL:
            adsize_ = AdSize.BANNER_HEIGHT_50;
            break;
        case MEDIUM:
            adsize_ = AdSize.BANNER_HEIGHT_90;
            break;
        case LARGE:
            adsize_ = AdSize.RECTANGLE_HEIGHT_250;
            break;
        }
        adView = new AdView(context, params.get("PLACEMENT_ID"), adsize_);
        container.addView(adView);
        adView.loadAd();
    }
}
