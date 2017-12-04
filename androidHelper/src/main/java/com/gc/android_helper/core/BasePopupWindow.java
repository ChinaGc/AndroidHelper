package com.gc.android_helper.core;

import android.animation.ValueAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.gc.androidhelper.R;

/**
 * BasePopupWindow 用于做从底部弹出的popupWindow Created by guocan on 2017/3/9.
 */

public abstract class BasePopupWindow implements PopupWindow.OnDismissListener {

    private PopupWindow popupWindow;

    private  Window window = null;

    protected BasePopupWindow(Window window) {
        this.window = window;
        popupWindow = new PopupWindow(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 设置动画
        popupWindow.setAnimationStyle(R.style.bottomInWindow);
        // 设置消失监听
        popupWindow.setOnDismissListener(this);
    }
    //显示
    public void showPopupWindow(View parentView) {
        popupWindow.setContentView(getContentView());
        // 防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        // 背景透明度
        setBackgroundAlpha(1, 0.5f);
        // 设置位置
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }
    //隐藏
    public void hide(){
        this.popupWindow.dismiss();
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(0.5f, 1);
    }

    // 设置屏幕背景透明效果
    private AlphaAnimatorUpdateListener alphaAnimatorUpdateListenerIn;

    private AlphaAnimatorUpdateListener alphaAnimatorUpdateListenerOut;

    private ValueAnimator valueAnimatorIn;

    private ValueAnimator valueAnimatorOut;
    //设置背景透明度
    private void setBackgroundAlpha(float alpha, float target) {
        if (window != null) {
            if (alpha > target) {// in
                if (valueAnimatorIn == null) {
                    valueAnimatorIn = ValueAnimator.ofFloat(alpha, target);
                }
                if (alphaAnimatorUpdateListenerIn == null) {
                    alphaAnimatorUpdateListenerIn = new AlphaAnimatorUpdateListener();
                    valueAnimatorIn.addUpdateListener(alphaAnimatorUpdateListenerIn);
                }
                valueAnimatorIn.setDuration(300);
                valueAnimatorIn.start();
            } else {// out
                if (valueAnimatorOut == null) {
                    valueAnimatorOut = ValueAnimator.ofFloat(alpha, target);
                }
                if (alphaAnimatorUpdateListenerOut == null) {
                    alphaAnimatorUpdateListenerOut = new AlphaAnimatorUpdateListener();
                    valueAnimatorOut.addUpdateListener(alphaAnimatorUpdateListenerOut);
                }
                valueAnimatorOut.setDuration(300);
                valueAnimatorOut.start();
            }
        }

    }

    private class AlphaAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float Value = (float) animation.getAnimatedValue();
            window.getAttributes().alpha = Value;
            window.setAttributes(window.getAttributes());
        }
    }

    /**
     * show方法被调用时会被回调
     * @return
     */
    protected abstract View getContentView();
}
