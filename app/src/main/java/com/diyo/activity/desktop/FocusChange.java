package com.diyo.activity.desktop;

import android.view.View;

/**
 * Created by tingbaby on 2015/10/28.
 */
public interface FocusChange {
    public boolean onGetFocus(View view,int position);
    public boolean onLostFocus(View view);
}
