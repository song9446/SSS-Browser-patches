package com.meerkatbrowser.ad.adapter;

import com.meerkatbrowser.ad.adapter.Adapter;
import com.meerkatbrowser.ad.Listener;
import com.meerkatbrowser.ad.AdSize;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

//import com.vungle.warren.Vungle;
import com.vungle.warren.AdConfig;              // Custom ad configurations
import com.vungle.warren.InitCallback;          // Initialization callback
import com.vungle.warren.LoadAdCallback;        // Load ad callback
import com.vungle.warren.PlayAdCallback;        // Play ad callback
import com.vungle.warren.VungleNativeAd;        // Flex-Feed ad
import com.vungle.warren.Vungle.Consent;        // GDPR consent
import com.vungle.warren.error.VungleException; // onError message


public class Vungle implements Adapter {
    public static String TAG = "VungleAdAdapter";
    @Override
    public void initialize(Context context, Bundle params, Listener listener) {
        if(com.vungle.warren.Vungle.isInitialized()){
            listener.onInitialized();
            return;
        }
        Log.e(TAG, "vungle init start");
        com.vungle.warren.Vungle.init(params.getString("APP_ID"), context, new InitCallback() {
            @Override
            public void onSuccess() { 
                Log.e(TAG, "vungle initialized..");
                listener.onSuccess();
            }
            @Override
            public void onError(Throwable throwable) {
                listener.onError(throwable.getLocalizedMessage());
            }
            @Override
            public void onAutoCacheAdAvailable(String placementId) { 
                Log.e(TAG, "auto cache available...");
            }
        });
    }
    @Override
    public void interstitial(Context context, Bundle params, Listener listener) {
        Log.e(TAG, "vungle inter start");
        com.vungle.warren.Vungle.loadAd(params.getString("PLACEMENT_REFERENCE_ID"), new LoadAdCallback() {
            @Override
            public void onAdLoad(String placementReferenceId) { 
                Log.e(TAG, "vungle inter loaded");
                showInterstitial(context, params, listener);
            }
            @Override
            public void onError(String placementReferenceId, Throwable throwable) {
                listener.onError("placementId: " + placementReferenceId + ": " + throwable.getLocalizedMessage());
            }
        });
    }
    @Override
    public void banner(Context context, ViewGroup container, AdSize adsize, Bundle params, Listener listener) {
        listener.onError("vungle has no banner");
    }
    private void showInterstitial(Context context, Bundle params, Listener listener){
        Log.e(TAG, "vungle inter show");
        com.vungle.warren.Vungle.playAd(params.getString("PLACEMENT_REFERENCE_ID"), null, new PlayAdCallback() {
            @Override
            public void onAdStart(String placementReferenceId) { 
                listener.onSuccess();
            }
            @Override
            public void onAdEnd(String placementReferenceId, boolean completed, boolean isCTAClicked) { 
            }
            @Override
            public void onError(String placementReferenceId, Throwable throwable) {
                listener.onError("placementId: " + placementReferenceId + ": " + throwable.getLocalizedMessage());
            }
        });
    }
}

