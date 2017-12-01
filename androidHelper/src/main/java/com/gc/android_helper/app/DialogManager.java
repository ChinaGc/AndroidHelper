package com.gc.android_helper.app;

/**
 * @author 郭灿
 * @ClassName: DialogManager
 * @Description: 对话框服务
 * @date 2017/11/30
 */
public class DialogManager {

    private static DialogManager dialogManager = null;

    private DialogManager() {

    }
    //单例
    public static DialogManager getInstance() {
        if (dialogManager == null) {
            dialogManager = new DialogManager();
        }
        return dialogManager;
    }
}
