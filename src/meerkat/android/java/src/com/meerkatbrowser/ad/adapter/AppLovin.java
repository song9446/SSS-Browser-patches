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
import com.applovin.sdk.AppLovinSdk;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinAdView;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinPrivacySettings;


public class AppLovin implements Adapter {
    public static String TAG = "AppLovinAdAdapter";
    boolean initialized = false;
    AppLovinAdView adView;
    AppLovinInterstitialAdDialog interstitialAd;
    @Override
    public void initialize(Context context, Bundle params, Listener listener) {
        if(initialized){
            listener.onInitialized();
            return;
        }
        initialized = true;
        AppLovinPrivacySettings.setHasUserConsent(true, context);
        AppLovinSdk.initializeSdk(context);
        //AppLovinSdk.getInstance(context).getSettings().setTestAdsEnabled(true);
        listener.onSuccess();
    }
    @Override
    public void interstitial(Context context, Bundle params, Listener listener) {
        interstitialAd = AppLovinInterstitialAd.create( AppLovinSdk.getInstance(context), context);
        AppLovinSdk.getInstance(context).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd ad) {
                interstitialAd.showAndRender(ad);
                listener.onSuccess();
            }
            @Override
            public void failedToReceiveAd(int errorCode) {
                listener.onError("applovin interstitial fail due to errorcode:" + errorCode);
            }
        });
    }
    @Override
    public void banner(Context context, ViewGroup container, AdSize adSize, Bundle params, Listener listener) {
        String zone_id = params.getString("ZONE_ID");
        AppLovinAdSize apAdSize = translateAdSize(adSize);
        if(params.containsKey("ZONE_ID"))
            adView = new AppLovinAdView(apAdSize, params.getString("ZONE_ID"), context);
        else
            adView = new AppLovinAdView(apAdSize, context);
        Log.e("hmm", "AppLovin ad view?");
        container.addView(adView, new android.widget.FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER ) );
        adView.setAdLoadListener( new AppLovinAdLoadListener() {
            @Override
            public void adReceived(final AppLovinAd ad) {
                Log.e("AppLovin", "??" + adView);
                Log.e("AppLovin", ad.getType() + " did load ad: " + ad.getAdIdNumber());
                listener.onSuccess();
            }
            @Override
            public void failedToReceiveAd(final int errorCode) {
                listener.onError("applovin banner fail due to errorcode:" + errorCode);
            }
        });
        adView.loadNextAd();
    }
    private AppLovinAdSize translateAdSize(AdSize adSize){
        switch(adSize){
        case SMALL:
            return AppLovinAdSize.BANNER;
        case MEDIUM:
            return AppLovinAdSize.LEADER;
        case LARGE:
            return AppLovinAdSize.MREC;
        default:
            return null;
        }
    }
}
