package com.meerkatbrowser.ad.adapter;

import com.meerkatbrowser.ad.adapter.Adapter;
import com.meerkatbrowser.ad.Listener;
import com.meerkatbrowser.ad.AdSize;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.ads.*;

public class Facebook implements Adapter {
    public static String TAG = "FacebookAdAdapter";
    InterstitialAd interstitialAd;
    AdView adView;
    @Override
    public void initialize(Context context, Bundle params, Listener listener) {
        if(AudienceNetworkAds.isInitialized(context)){
            listener.onInitialized();
            return;
        }
        AudienceNetworkAds
            .buildInitSettings(context)
            .withInitListener(new AudienceNetworkAds.InitListener(){
                @Override
                public void onInitialized(AudienceNetworkAds.InitResult result) {
                    if(result.isSuccess()) listener.onSuccess();
                    else listener.onError(result.getMessage());
                }
            })
        .initialize();
    }
    private com.facebook.ads.AdSize translateAdSize(AdSize adsize){
        switch(adsize){
        case SMALL:
            return com.facebook.ads.AdSize.BANNER_HEIGHT_50;
        case LARGE:
            return com.facebook.ads.AdSize.BANNER_HEIGHT_90;
        case RECTANGLE:
            return com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250;
        default:
            return null;
        }
    }
    @Override
    public void loadInterstitial(Context context, Bundle params, Listener listener) {
        interstitialAd = new InterstitialAd(context, params.getString("PLACEMENT_ID"));
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {
            }
            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
            }
            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                listener.onError(adError.getErrorMessage());
            }
            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                if(interstitialAd.isAdInvalidated()){
                    listener.onError("invalidate facebook ad");
                    return;
                }
                listener.onSuccess();
            }
            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
            }
            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
            }
        });
        interstitialAd.loadAd();
    }
    @Override
    public void showInterstitial(Context context, Listener listener) {
        interstitialAd.show();
        listener.onSuccess();
    }
    public void banner(Context context, ViewGroup container, AdSize adsize, Bundle params, Listener listener) {
        adView = new AdView(context, params.getString("PLACEMENT_ID"), translateAdSize(adsize));
        container.addView(adView);
        adView.loadAd();
    }
    @Override
    public View createAdView(Context context, AdSize adsize, Bundle params) {
        return new AdView(context, params.getString("PLACEMENT_ID"), translateAdSize(adsize));
    }
    @Override
    public void loadAdView(View view, Listener listener){
        AdView adView = (AdView) view;
        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                listener.onError("facebook banner fail due to: " + adError.getErrorMessage());
            }
            @Override
            public void onAdLoaded(Ad ad) {
                listener.onSuccess();
            }
            @Override
            public void onAdClicked(Ad ad) {
            }
            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });
        adView.loadAd();
    }
}
