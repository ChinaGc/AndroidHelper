package com.gc.android_helper.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;

import com.gc.android_helper.core.Api;

/**
 * @author 郭灿
 * @ClassName: DialogManager
 * @Description: 对话框服务
 * @date 2017/11/30
 */
public class DialogManager {

    private static DialogManager dialogManager = null;
    private Context context;

    public static DialogManager getInstance(Context context) {
        if (dialogManager == null) {
            dialogManager = new DialogManager(context);
        }
        return dialogManager;
    }

    private AlertDialog.Builder alertDialog = null;
    private AlertDialog.Builder confirmDialog = null;
    private AlertDialog.Builder promptDialog = null;
    private AlertDialog.Builder listDialog = null;

    private DialogManager(Context context) {
        this.context = context;
    }

    public void alert(String title, String message, OnClickListener onClickListener) {
        // normalDialog.setIcon(R.drawable.icon_dialog);
        if(alertDialog==null){
            alertDialog = new AlertDialog.Builder(context);
        }
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("确定", new DialogOnClickListener(onClickListener));
        alertDialog.show();
    }

    public void confirm(String title, String message, OnClickListener onClickListener) {
        // normalDialog.setIcon(R.drawable.icon_dialog);
        if(confirmDialog==null){
            confirmDialog = new AlertDialog.Builder(context);
        }
        confirmDialog.setTitle("我是一个普通Dialog");
        confirmDialog.setMessage("你要点击哪一个按钮呢?");
        confirmDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        confirmDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        confirmDialog.show();
    }

    // DialogManager 对话框服务事件回调
    public interface OnClickListener {
        // 右上往下数1开始 or 由左往右数1开始
        public void onClick(int position);
    }

    private class DialogOnClickListener implements DialogInterface.OnClickListener {

        private OnClickListener onClickListener;

        public DialogOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (onClickListener != null) {
                onClickListener.onClick(which);
            }
        }
    }
}
