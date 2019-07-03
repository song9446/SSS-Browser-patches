package com.meerkatbrowser.ad;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import java.util.List;
import java.util.ArrayList;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.util.Log;
import android.content.Context;
import android.os.Bundle;

import com.meerkatbrowser.ad.*;
import com.meerkatbrowser.ad.adapter.*;

public class BannerLayout extends LinearLayout {
    public static String TAG = "BannerLayout";
    List<Adapter> adapters = new ArrayList<Adapter>();
    List<View> views = new ArrayList<View>();
    boolean loadStarted = false;
    boolean loading = false;
    int index = 0;
    public BannerLayout(Context context){
        super(context);
    }
    public BannerLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        /*
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyCustomElement);
        Adapter provider = Ad.name2adapter(ta.getString(R.styleable.Banner_provider));
        String size = ta.getString(R.styleable.Banner_size, "SMALL");
        String app_id = ta.getString(R.styleable.Banner_id, null);
        ad.initialize();*/
        /*ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();

            }
        });*/
    }
        /*
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        Banner banner = (Banner) child;
        Adapter adapter = Ad.name2adapter(banner.provider);
        adapter.initialize(getContext(), banner.params, new Listener(){});
        adapters.add(adapter);
        View newChild = adapter.createAdView(getContext(), banner.size, banner.params);
        views.add(newChild);
        super.addView(newChild, index, params);
        if(!loading)
            loadNextAd();
    }
    */
    public void loadNextAd(){
        if(adapters.size() <= index) {
            Log.e(TAG, index + " is larger than adapters number");
            return;
        }
        loadStarted = true;
        loading = true;
        adapters.get(index).loadAdView(views.get(index), new Listener(){
            @Override
            public void onSuccess() {
                index += 1;
                loading = false;
            }
            @Override
            public void onError(String msg) {
                loading = false;
                Log.e(TAG, "error with " + index + "th banner ad load: " + msg);
                index += 1;
                loadNextAd();
            }
        });
    }
}
