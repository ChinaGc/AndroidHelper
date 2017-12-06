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

    public static DialogManager getInstance(Context context) {
        if (dialogManager == null) {
            dialogManager = new DialogManager(context);
        }
        return dialogManager;
    }

    private AlertDialog.Builder dialog = null;

    private DialogManager(Context context) {
        dialog = new AlertDialog.Builder(context);
    }

    public void alert(String title, String message, OnClickListener onClickListener) {
        // normalDialog.setIcon(R.drawable.icon_dialog);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("确定", new DialogOnClickListener(onClickListener));
        dialog.show();
    }

    public void confirm(String title, String message, OnClickListener onClickListener) {
        // normalDialog.setIcon(R.drawable.icon_dialog);
        dialog.setTitle("我是一个普通Dialog");
        dialog.setMessage("你要点击哪一个按钮呢?");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        dialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        dialog.show();
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
