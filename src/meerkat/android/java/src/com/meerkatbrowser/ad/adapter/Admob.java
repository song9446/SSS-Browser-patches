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
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdListener;


public class Admob implements Adapter {
    public static String TAG = "AdmobAdapter";
    static boolean initialized = false;
    InterstitialAd mInterstitialAd;
    AdView adView;
    //AppLovinAdView adView;
    //AppLovinInterstitialAdDialog interstitialAd;
    //AppLovinAd loadedAd;
    @Override
    public void initialize(Context context, Bundle params, Listener listener) {
        if(initialized){
            listener.onInitialized();
            return;
        }
        initialized = true;
        MobileAds.initialize(context, params.getString("APP_ID"));
        listener.onSuccess();
    }
    @Override
    public void loadInterstitial(Context context, Bundle params, Listener listener) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(params.getString("AD_UNIT_ID"));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                listener.onSuccess();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                listener.onError("admob interstitial fail due to errorcode:" + errorCode);
            }
            @Override
            public void onAdOpened() {
            }
            @Override
            public void onAdClicked() {
            }
            @Override
            public void onAdLeftApplication() {
            }
            @Override
            public void onAdClosed() {
            }
        });
    }
    @Override
    public void showInterstitial(Context context, Listener listener) {
        mInterstitialAd.show();
        listener.onSuccess();
    }
    public void banner(Context context, ViewGroup container, AdSize adSize, Bundle params, Listener listener) {
        /*
        Log.e(TAG, "admob banner ad view?");
        adView = context.findViewById(R.id.adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                listener.onSuccess();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                listener.onError("admob banner fail due to errorcode:" + errorCode);
            }
            @Override
            public void onAdOpened() {
            }
            @Override
            public void onAdClicked() {
            }
            @Override
            public void onAdLeftApplication() {
            }
            @Override
            public void onAdClosed() {
            }
        });
        adView.loadAd(new AdRequest.Builder().build());*/
    }
    @Override
    public View createAdView(Context context, AdSize adsize, Bundle params) {
        AdView adView = new AdView(context);
        adView.setAdSize(translateAdSize(adsize));
        adView.setAdUnitId(params.getString("UNIT_ID"));
        return adView;
    };
    @Override
    public void loadAdView(View view, Listener listener){
        AdView adView = (AdView) view;
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                listener.onSuccess();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                listener.onError("admob banner fail due to errorcode:" + errorCode);
            }
            @Override
            public void onAdOpened() {
            }
            @Override
            public void onAdClicked() {
            }
            @Override
            public void onAdLeftApplication() {
            }
            @Override
            public void onAdClosed() {
            }
        });
        adView.loadAd(new AdRequest.Builder().build());
    }
    private com.google.android.gms.ads.AdSize translateAdSize(AdSize adSize){
        switch(adSize){
        case SMALL:
            return com.google.android.gms.ads.AdSize.BANNER;
        case LARGE:
            return com.google.android.gms.ads.AdSize.LARGE_BANNER;
        case RECTANGLE:
            return com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE;
        default:
            return null;
        }
    }
}
