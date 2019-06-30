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
    }
    public boolean isBlockedURL(String urlString) throws MalformedURLException {
        String host = (new URL(urlString)).getHost().toLowerCase().trim();
        host = host.startsWith("m.")? host.substring(2) : host;
        host = host.startsWith("www.")? host.substring(4) : host;
        Log.e(TAG, "host: " + host);
        for(int i=0; i<blocked_websites.length; ++i)
            if(blocked_websites[i].toLowerCase().trim().equals(host))
                return true;
        return false;
    }
}
