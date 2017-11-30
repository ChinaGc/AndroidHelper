package com.gc.android_helper.view.swipeback;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.gc.android_helper.app.MSwipeBackActivity;
import com.gc.androidhelper.R;

import me.imid.swipebacklayout.lib.Utils;

/**
 * Created by gc on 2017/7/7/007.
 */

public class MSwipeBackActivityHelper {
    private MSwipeBackActivity mActivity;

    private MSwipeBackLayout mSwipeBackLayout;

    public MSwipeBackActivityHelper(MSwipeBackActivity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = (MSwipeBackLayout) LayoutInflater.from(mActivity).inflate(
             R.layout.mswipeback_layout, null);
        mSwipeBackLayout.addSwipeListener(new MSwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                Utils.convertActivityToTranslucent(mActivity);
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    public MSwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
