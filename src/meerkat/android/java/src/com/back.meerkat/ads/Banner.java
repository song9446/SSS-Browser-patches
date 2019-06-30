package com.meerkat.ads;

import android.util.Log;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.applovin.adview.AppLovinInterstitialAdDialog;

public class Banner {
    enum State {
        IDLE,
        LOADING,
        LOADED,
    }
    State state = State.IDLE;
    private static final String TAG = "meerkat.ads.Banner";
    Context context;
    ViewGroup container;


    com.facebook.ads.AdView fanAd;
    public enum Size {
        STANDARD,
        LARGE,
        RECTANGLE,
    }
    public com.facebook.ads.AdSize sizeConvert(Ads.Provider provider, Size size){
        switch(provider){
        case FAN:
            switch(size){
                case STANDARD: return com.facebook.ads.AdSize.BANNER_HEIGHT_50;
                case LARGE: return com.facebook.ads.AdSize.BANNER_HEIGHT_90;
                case RECTANGLE: return com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250;
            }
        }
        return null;
    }

    Ads.Provider[] providerLoadingOrder = {
        Ads.Provider.FAN,
    };

    public Banner(Context context, String fanId, Size size) {//, String applovinZoneId, Listener listener){
        this.context = context;
        fanAd = new com.facebook.ads.AdView(context, fanId, sizeConvert(Ads.Provider.FAN, size));
        fanAd.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                if(!loadContinue())
                    Log.e(TAG, "fan banner laod error: " + adError.getErrorMessage());
                loadContinue();
            }
            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                loaded();
            }
            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) { }
            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) { }
        });
    }
    private void loaded() {
        Log.e(TAG, "loaded!");
        state = State.LOADED;
    }
    private void load(int index) {
        Log.e(TAG, "load index: " + index);
        switch(providerLoadingOrder[index]) {
        case FAN:
            View view = fanAd;
            container.removeAllViews();
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) 
                parent.removeView(view);
            container.addView(view);
            fanAd.loadAd();
            break;
        case ADLOVIN:
            break;
        }
    }
    int providerIndex = 0;
    public boolean load(ViewGroup container) {
        this.container = container;
        if(state == State.LOADING)
            return false;
        state = State.LOADING;
        providerIndex = 0;
        load(providerIndex);
        return true;
    }
    private boolean loadContinue() {
        assert(state == State.LOADING);
        providerIndex += 1;
        if(providerIndex >= providerLoadingOrder.length){
            state = State.IDLE;
            providerIndex = 0;
            return false;
        }
        load(providerIndex);
        return true;
    }
    public boolean isLoading() {
        return state == State.LOADING;
    }
    public boolean isLoaded() {
        return state == State.LOADED;
    }
}
