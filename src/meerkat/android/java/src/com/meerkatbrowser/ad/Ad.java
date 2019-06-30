package com.meerkatbrowser.ad;

import com.meerkatbrowser.ad.*;
import com.meerkatbrowser.ad.adapter.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import android.os.Bundle;
import android.content.Context;
import android.view.ViewGroup;

public class Ad {
    public static String TAG = "Meerkat.Ad";
    List<Adapter> channels = new ArrayList<Adapter>();
    List<Boolean> initialized = new ArrayList<Boolean>();
    List<Boolean> initializeDone = new ArrayList<Boolean>();
    Listener initializeListener;
    Listener internalInitializeListener = new Listener(){};
    Bundle[] initializeParams;
    Bundle[] adParams;
    int shownNum = 0;
    long lastShownTime = System.currentTimeMillis();
    private boolean initializedOne = false;
    public Adapter name2adapter(String name){
        switch(name){
        case "Facebook":
            return new Facebook();
        case "AppLovin":
            return new AppLovin();
        case "Vungle":
            return new Vungle();
        }
        return null;
    }
    private void updateStatsAfterShown() {
        shownNum += 1;
        lastShownTime = System.currentTimeMillis();
    }
    public int getShownNum(){
        return shownNum; 
    }
    public long getTimeMillisDeltaFromLastShown(){
        return System.currentTimeMillis() - lastShownTime;
    }
    public Ad(String[] channelNames, Bundle[] initializeParams, Bundle[] adParams) {
        assert(adParams.length == channelNames.length && initializeParams.length == channelNames.length);
        for(int i=0; i<channelNames.length; ++i){
            Adapter adapter = name2adapter(channelNames[i]);
            if(adapter == null){
                Log.e(TAG, "error while translate channel name to class name: " + channelNames[i]);
                initializeDone.add(true);
            }
            else {
                initializeDone.add(false);
            }
            channels.add(adapter);
            initialized.add(false);
        }
        this.initializeParams = initializeParams;
        this.adParams = adParams;
    }
    public boolean isInitialized() {
        return initializedOne;
    }
    public boolean isInitializedAll() {
        boolean allInitialized = true;
        for(boolean i: initializeDone)
            if(!i)allInitialized = false;
        return allInitialized;
    }
    public void setInitializeListener(Listener listener) {
        initializeListener = listener;
    }
    public void initialize(Context context) {
        initialize(context, new Listener(){});
    }
    public void initialize(Context context, Listener listener) {
        initializeListener = listener;
        for(int i=0; i<channels.size(); ++i){
            final int index = i;
            if(channels.get(i) == null){ 
                initializeDone.set(index, true);
                continue;
            }
            Log.e(TAG, "initializing.." + i);
            channels.get(i).initialize(context, initializeParams[i], new Listener(){
                @Override
                public void onSuccess(){ 
                    initialized.set(index, true);
                    initializeDone.set(index, true);
                    if(!initializedOne) initializeListener.onInitialized();
                    if(!initializedOne) internalInitializeListener.onInitialized();
                    initializedOne = true;
                    boolean allInitialized = true;
                    for(boolean i: initializeDone)
                        if(!i)allInitialized = false;
                    if(allInitialized) initializeListener.onInitializedAll();
                    if(allInitialized) internalInitializeListener.onInitializedAll();
                }
                @Override
                public void onInitialized(){ 
                    initialized.set(index, true);
                    initializeDone.set(index, true);
                    if(!initializedOne) initializeListener.onInitialized();
                    if(!initializedOne) internalInitializeListener.onInitialized();
                    initializedOne = true;
                    boolean allInitialized = true;
                    for(boolean i: initializeDone)
                        if(!i)allInitialized = false;
                    if(allInitialized) initializeListener.onInitializedAll();
                    if(allInitialized) internalInitializeListener.onInitializedAll();
                }
                @Override
                public void onInitializing(){
                    initialized.set(index, false);
                    initializeDone.set(index, true);
                    boolean allInitialized = true;
                    for(boolean i: initializeDone)
                        if(!i)allInitialized = false;
                    if(allInitialized) initializeListener.onInitializedAll();
                    if(allInitialized) internalInitializeListener.onInitializedAll();
                }
                @Override
                public void onError(String msg){
                    Log.e(TAG, "error while initializing: " + msg);
                    initialized.set(index, false);
                    initializeDone.set(index, true);
                    boolean allInitialized = true;
                    for(boolean i: initializeDone)
                        if(!i)allInitialized = false;
                    if(allInitialized) initializeListener.onInitializedAll();
                }
            });
        }
    }
    public void banner(Context context, ViewGroup container, AdSize adSize, Listener listener){
        final Context context_ = context;
        final ViewGroup container_ = container;
        final AdSize adSize_ = adSize;
        final Listener listener_ = listener;
        if(isInitialized())
            banner(context_, container, adSize, listener, 0);
        else{
            internalInitializeListener = new Listener(){
                @Override
                public void onInitialized(){
                    banner(context_, container_, adSize_, listener_, 0);
                }
            };
        }
    }
    private void banner(Context context, ViewGroup container, AdSize adSize, Listener listener, int index) {
        Log.e(TAG, "try to load banner at " + index);
        if(index >= channels.size())
            listener.onError("banner: all channel fail");
        else if(!initialized.get(index)) 
            banner(context, container, adSize, listener, index+1);
        else 
            channels.get(index).banner(context, container, adSize, adParams[index], new Listener(){ 
                @Override
                public void onSuccess(){
                    updateStatsAfterShown();
                    listener.onSuccess();
                }
                @Override
                public void onError(String msg){
                    Log.e(TAG, "fail with " + index + "th channel, retry with next channel. error msg: " + msg);
                    banner(context, container, adSize, listener, index+1);
                }
            });
    }
    public void interstitial(Context context, Listener listener) {
        final Context context_ = context;
        final Listener listener_ = listener;
        if(isInitialized())
            interstitial(context_, listener, 0);
        else{
            internalInitializeListener = new Listener(){
                @Override
                public void onInitialized(){
                    interstitial(context_, listener_, 0);
                }
            };
        }
    }
    private void interstitial(Context context, Listener listener, int index) {
        Log.e(TAG, "try to load interstitial at " + index);
        if(index >= channels.size())
            listener.onError("interstital: all channel fail");
        else if(!initialized.get(index)) 
            interstitial(context, listener, index+1);
        else 
            channels.get(index).interstitial(context, adParams[index], new Listener(){ 
                @Override
                public void onSuccess(){
                    updateStatsAfterShown();
                    listener.onSuccess();
                }
                @Override
                public void onError(String msg){
                    Log.e(TAG, "fail with " + index + "th channel, retry with next channel. error msg: " + msg);
                    interstitial(context, listener, index+1);
                }
            });
    }
}
