package com.gc.android_helper.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

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

    private AlertDialog.Builder confirmDialog = null;

    private DialogManager(Context context) {
        confirmDialog = new AlertDialog.Builder(context);
    }

    /*
     * @setIcon 设置对话框图标
     * @setTitle 设置对话框标题
     * @setMessage 设置对话框消息提示 setXXX方法返回Dialog对象，因此可以链式设置属性
     */
    public void showConfirmDialog() {
        // normalDialog.setIcon(R.drawable.icon_dialog);
        confirmDialog.setTitle("我是一个普通Dialog");
        confirmDialog.setMessage("你要点击哪一个按钮呢?");
        confirmDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO
                Api.getInstance().toast(which+"");
            }
        });
        confirmDialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO
                Api.getInstance().toast(which+"");
            }
        });
        // 显示
        confirmDialog.show();
    }
}
