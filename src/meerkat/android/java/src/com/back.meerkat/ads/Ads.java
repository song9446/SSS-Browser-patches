package com.meerkat.ads;

import android.content.Context;
import android.util.Log;
import java.util.Arrays;
import java.net.URL;
import java.net.MalformedURLException;
import com.applovin.sdk.AppLovinSdk;

import com.vungle.warren.Vungle;
import com.vungle.warren.AdConfig;              // Custom ad configurations
import com.vungle.warren.InitCallback;          // Initialization callback
import com.vungle.warren.LoadAdCallback;        // Load ad callback
import com.vungle.warren.PlayAdCallback;        // Play ad callback
import com.vungle.warren.VungleNativeAd;        // Flex-Feed ad
import com.vungle.warren.Vungle.Consent;        // GDPR consent
import com.vungle.warren.error.VungleException; // onError message
//import com.facebook.ads.*;

public class Ads {
    public enum Provider {
        FAN,
        ADLOVIN,
        VUNGLE,
    }
    private static final String TAG = "Ads";
    private static boolean initialized = false;
    private static String[] blocked_host_list;
    private static Provider[] providerLoadingOrder_ = {
        Provider.VUNGLE,
        Provider.FAN,
        Provider.ADLOVIN,
    };
    public static Provider[] providerLoadingOrder() {
        return providerLoadingOrder_;
    }

    public static void initialize(Context context, String vungleAppId) {
        if(initialized)
            return;
        initialized = true;
        String blocked_list_string = context.getString(R.string.blocked_website_list);
        blocked_host_list = Arrays
            .stream(blocked_list_string.trim().split(" "))
            .filter(s-> !s.trim().equals(""))
            .toArray(String[]::new);
        for(int i=0; i<blocked_host_list.length; ++i){
            String url = blocked_host_list[i].trim().toLowerCase();
            url = url.startsWith("m.") ? url.substring(2) : url;
            url = url.startsWith("www.") ? url.substring(4) : url;
            blocked_host_list[i] = url; 
        }
        com.facebook.ads.AudienceNetworkAds.initialize(context);
        AppLovinSdk.initializeSdk(context);
        AppLovinSdk.getInstance(context).getSettings().setTestAdsEnabled(true);
        Vungle.init(vungleAppId, context, new InitCallback() {
            @Override
            public void onSuccess() { }
            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "Vungle error: " + throwable.getLocalizedMessage());
            }
            @Override
            public void onAutoCacheAdAvailable(String placementId) { }
        });
    }
    public static boolean isBlockedURL(String urlString) throws MalformedURLException {
        String host = (new URL(urlString)).getHost().toLowerCase();
        host = host.startsWith("m.")? host.substring(2) : host;
        host = host.startsWith("www.")? host.substring(4) : host;
        Log.e(TAG, "host: " + host);
        for(int i=0; i<blocked_host_list.length; ++i)
            if(blocked_host_list[i].equals(host))
                return true;
        return false;
    }
    public static boolean isInitialized() {
        return initialized;
    }
}
