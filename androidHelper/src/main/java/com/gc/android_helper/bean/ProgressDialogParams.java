package com.gc.android_helper.bean;

/**
 * Created by guocan on 2017/6/5/005.
 */

public class ProgressDialogParams {
    private String msg;
    private boolean cancel = true;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
