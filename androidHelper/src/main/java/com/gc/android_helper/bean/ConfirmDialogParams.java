package com.gc.android_helper.bean;

import android.view.View;


public class ConfirmDialogParams {
    private String title = "提示";

    private String msg = "这是一条信息";

    private String leftBtnText = "确定";

    private String rightBtnText = "取消";

    private View.OnClickListener leftOnClickListener;

    private View.OnClickListener rightOnClickListener;

    public View.OnClickListener getLeftOnClickListener() {
        return leftOnClickListener;
    }
    
    public void setLeftOnClickListener(View.OnClickListener leftOnClickListener) {
        this.leftOnClickListener = leftOnClickListener;
    }

    public View.OnClickListener getRightOnClickListener() {
        return rightOnClickListener;
    }

    public void setRightOnClickListener(View.OnClickListener rightOnClickListener) {
        this.rightOnClickListener = rightOnClickListener;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLeftBtnText() {
        return leftBtnText;
    }

    public void setLeftBtnText(String leftBtnText) {
        this.leftBtnText = leftBtnText;
    }

    public String getRightBtnText() {
        return rightBtnText;
    }

    public void setRightBtnText(String rightBtnText) {
        this.rightBtnText = rightBtnText;
    }
}
