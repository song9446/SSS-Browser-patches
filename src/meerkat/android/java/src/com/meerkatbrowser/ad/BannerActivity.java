package com.meerkatbrowser.ad;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.view.ViewGroup;
import com.meerkatbrowser.ad.*;
import org.chromium.chrome.R;

public class BannerActivity extends Activity {
    static String TAG = "BannerActivity";
    Ad ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    void initialize(ViewGroup container){
        Log.e(TAG, "ntp head banner ad req");
        Bundle[] params = new Bundle[]{new Bundle(), new Bundle()};
        params[0].putString("ZONE_ID", "f7fc65056c193fd3");
        params[1].putString("PLACEMENT_ID", "700550223723047_700550750389661");
        ad = new Ad(new String[]{"AppLovin", "Facebook"}, params, params);
        ad.initialize(this);
        ad.banner(this, (ViewGroup)container, AdSize.LARGE, new Listener(){
            @Override
            public void onSuccess(){
                Log.e(TAG, "success..");
            }
            @Override
            public void onError(String msg){
                Log.e(TAG, "error..");
            }
        });
    }
}
