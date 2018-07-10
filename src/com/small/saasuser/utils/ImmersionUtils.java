package com.small.saasuser.utils;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;



/**
 * Created by tanyl on 2016/10/25 09:58
 */
public class ImmersionUtils {
    public static Activity mActivity;

    public ImmersionUtils(Activity mActivity) {
        this.mActivity = mActivity;

    }

    public void setTranslucentStatus(int id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager statusManager = new SystemStatusManager(mActivity);
            statusManager.setStatusBarTintEnabled(true);
            statusManager.setStatusBarTintResource(id);
            mActivity.getWindow().getDecorView().setFitsSystemWindows(true);

        }

    }


}
