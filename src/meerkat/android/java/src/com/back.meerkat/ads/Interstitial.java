package com.meerkat.ads;

import android.content.Context;
import android.util.Log;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;

import com.vungle.warren.Vungle;
import com.vungle.warren.AdConfig;              // Custom ad configurations
import com.vungle.warren.InitCallback;          // Initialization callback
import com.vungle.warren.LoadAdCallback;        // Load ad callback
import com.vungle.warren.PlayAdCallback;        // Play ad callback
import com.vungle.warren.VungleNativeAd;        // Flex-Feed ad
import com.vungle.warren.Vungle.Consent;        // GDPR consent
import com.vungle.warren.error.VungleException; // onError message

public class Interstitial {
    public static class Listener {
        public void onLoad(Interstitial i){} 
        public void onClose(Interstitial i){} 
        public void onError(Interstitial i, String msg){}
    }
    enum State {
        IDLE,
        LOADING,
        LOADED,
    }

    State state = State.IDLE;
    private static final String TAG = "meerkat.ads.Interstitial";
    Context context;
    Listener listener;

    com.facebook.ads.InterstitialAd fanAd;
    AppLovinAd loadedAppLovinAd;
    AppLovinInterstitialAdDialog applovinInterstitialAd;
    String applovinZoneId;
    String vunglePlacementId;

    public Interstitial(Context context, String fanId, String applovinZoneId, String vunglePlacementId, Listener listener){
        this.context = context;
        this.listener = listener;
        this.applovinZoneId = applovinZoneId;
        this.vunglePlacementId = vunglePlacementId;
        fanAd = new com.facebook.ads.InterstitialAd(context, fanId);
        fanAd.setAdListener(new com.facebook.ads.InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {
            }
            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                listener.onClose(Interstitial.this);
            }
            @Override
            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                if(!loadContinue())
                    listener.onError(Interstitial.this, adError.getErrorMessage());
            }
            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                loaded();
                listener.onLoad(Interstitial.this);
            }
            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
            }
            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
            }
        });
        applovinInterstitialAd = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(context), context);
        applovinInterstitialAd.setAdDisplayListener(new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {}
            @Override
            public void adHidden(AppLovinAd appLovinAd)
            {
                listener.onClose(Interstitial.this);
            }
        });
    }
    private void loaded() {
        state = State.LOADED;
    }
    private void load(int index) {
        Log.e(TAG, "load index: " + index);
        switch(Ads.providerLoadingOrder()[index]) {
        case FAN:
            fanAd.loadAd();
            break;
        case ADLOVIN:
            AppLovinSdk.getInstance(context).getAdService().loadNextAdForZoneId(applovinZoneId, new AppLovinAdLoadListener()
            {
                @Override
                public void adReceived(AppLovinAd ad)
                {
                    loaded();
                    loadedAppLovinAd = ad;
                    listener.onLoad(Interstitial.this);
                }
                @Override
                public void failedToReceiveAd(int errorCode)
                {
                    loadContinue();
                    // Look at AppLovinErrorCodes.java for list of error codes.
                }
            });
            break;
        case VUNGLE:
            if(!Vungle.isInitialized()) 
                loadContinue();
            Vungle.loadAd(vunglePlacementId, new LoadAdCallback() {
                @Override
                public void onAdLoad(String placementReferenceId) { 
                    loaded();
                    listener.onLoad(Interstitial.this);
                }
                @Override
                public void onError(String placementReferenceId, Throwable throwable) {
                    loadContinue();
                }
            });
            break;
        }
    }
    int providerIndex = 0;
    public boolean load() {
        state = State.LOADING;
        providerIndex = 0;
        load(providerIndex);
        return true;
    }
    private boolean loadContinue() {
        assert(state == State.LOADING);
        providerIndex += 1;
        if(providerIndex >= Ads.providerLoadingOrder().length){
            state = State.IDLE;
            providerIndex = 0;
            listener.onError(Interstitial.this, "no ad available..");
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
    public boolean show() {
        if(state != State.LOADED) return false;
        Log.e(TAG, providerIndex + "th provider show the add");
        switch(Ads.providerLoadingOrder()[providerIndex]) {
        case FAN:
            fanAd.show();
            break;
        case ADLOVIN:
            applovinInterstitialAd.showAndRender(loadedAppLovinAd);
            break;
        case VUNGLE:
            Vungle.playAd(vunglePlacementId, null, new PlayAdCallback() {
                @Override
                public void onAdStart(String placementReferenceId) { }
                @Override
                public void onAdEnd(String placementReferenceId, boolean completed, boolean isCTAClicked) { 
                    listener.onClose(Interstitial.this);
                }
                @Override
                public void onError(String placementReferenceId, Throwable throwable) {
                    // Play ad error occurred - throwable.getLocalizedMessage() contains error message
                }
            });
            break;
        }
        state = State.IDLE;
        return true;
    }
}
