package com.meerkatbrowser.ad.adapter;

import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;
import com.meerkatbrowser.ad.AdSize;
import com.meerkatbrowser.ad.Listener;
public interface Adapter {
    void initialize(Context context, Bundle params, Listener listener);
    void loadInterstitial(Context context, Bundle params, Listener listener);
    void showInterstitial(Context context, Listener listener);
    View createAdView(Context context, AdSize adsize, Bundle params);
    void loadAdView(View view, Listener listener);
}
