package com.meerkatbrowser.ad;

import com.meerkatbrowser.ad.*;
import com.meerkatbrowser.ad.adapter.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.net.MalformedURLException;
import android.util.Log;
import android.os.Bundle;
import android.content.Context;
import android.view.ViewGroup;
import android.content.res.Resources;

import java.net.URL;

public class BlockChecker {
    public static String TAG = "BlockChecker";
    String[] blocked_websites;
    public BlockChecker(Context context){
        Resources res = context.getResources();
        blocked_websites = res.getStringArray(R.array.blocked_websites);
        for(int i=0; i<blocked_websites.length; ++i)
            blocked_websites[i] = normalizeURL(blocked_websites[i]);
    }
    private String normalizeURL(String target) {
        target = target.toLowerCase().trim();
        if(target.startsWith("http")){
            try{
                target = (new URL(target)).getHost();
            }catch(Exception e){
            }
        }
        target = target.startsWith("m.")? target.substring(2) : target;
        target = target.startsWith("www.")? target.substring(4) : target;
        return target;
    }
    public boolean isBlockedURL(String url) {
        String target = normalizeURL(url);
        Log.e(TAG, "target: " + target);
        for(int i=0; i<blocked_websites.length; ++i)
            if(blocked_websites[i].equals(target))
                return true;
        return false;
    }
}
