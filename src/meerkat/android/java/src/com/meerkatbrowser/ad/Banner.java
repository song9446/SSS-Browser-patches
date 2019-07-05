package com.meerkatbrowser.ad;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.util.Log;
import android.content.Context;
import android.os.Bundle;

import com.meerkatbrowser.ad.*;
import com.meerkatbrowser.ad.adapter.*;

public class Banner extends View {
    public String provider;
    public AdSize size;
    public Bundle params = new Bundle();
    public Banner(Context context){
        super(context);
    }
    public Banner(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Banner, 0,0);
        provider = ta.getString(R.styleable.Banner_provider);
        size = translateSize(ta.getString(R.styleable.Banner_size));
        params.putString("APP_ID", ta.getString(R.styleable.Banner_app_id));
        params.putString("PLACEMENT_ID", ta.getString(R.styleable.Banner_placement_id));
        params.putString("PLACEMENT_REFERENCE_ID", ta.getString(R.styleable.Banner_placement_reference_id));
        params.putString("ZONE_ID", ta.getString(R.styleable.Banner_placement_reference_id));
        params.putString("UNIT_ID", ta.getString(R.styleable.Banner_unit_id));
        ta.recycle();
    }
    private AdSize translateSize(String size){
        switch(size){
            case "SMALL": return AdSize.SMALL;
            case "LARGE": return AdSize.LARGE;
            case "RECTANGLE": return AdSize.RECTANGLE;
            default: return AdSize.SMALL;
        }
    }
}
