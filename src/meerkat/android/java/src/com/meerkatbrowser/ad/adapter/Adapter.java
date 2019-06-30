package com.meerkatbrowser.ad.adapter;

import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.ViewGroup;
import com.meerkatbrowser.ad.AdSize;
import com.meerkatbrowser.ad.Listener;
public interface Adapter {
    void initialize(Context context, Bundle params, Listener listener);
    void interstitial(Context context, Bundle params, Listener listener);
    void banner(Context context, ViewGroup container, AdSize adsize, Bundle params, Listener listener);
}
