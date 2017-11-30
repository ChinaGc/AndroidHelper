package com.gc.android_helper.bean;

import android.view.View;

/**
 * Created by 郭灿 on 2017/5/6.
 */

public class AlertDialogParams {
    private String title = "提示";

    private String msg = "这是一条信息";

    private String btnText = "好";

    private View.OnClickListener btnnClickListener;

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

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public View.OnClickListener getBtnnClickListener() {
        return btnnClickListener;
    }

    public void setBtnnClickListener(View.OnClickListener btnnClickListener) {
        this.btnnClickListener = btnnClickListener;
    }
}
