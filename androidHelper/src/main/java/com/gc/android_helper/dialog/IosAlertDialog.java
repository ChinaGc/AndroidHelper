package com.gc.android_helper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gc.android_helper.util.LengthUtil;
import com.gc.androidhelper.R;

public class IosAlertDialog {
    private Context context;

    private Dialog dialog;

    private TextView txt_title;

    private TextView txt_msg;

    private TextView btn_neg;

    private Display display;

    public IosAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public IosAlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_ios, null);
        // 获取自定义Dialog布局中的控件
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        btn_neg = (TextView) view.findViewById(R.id.btn_neg);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.customerDialogStyle);
        dialog.setContentView(view);
        // 调整dialog背景大小
        /*
         * lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int)
         * (display.getWidth() * 0.8) , LayoutParams.WRAP_CONTENT));
         */
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (LengthUtil.getScreenWidth(this.context) * 0.7);
        return this;
    }

    public IosAlertDialog setTitle(String title) {
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public IosAlertDialog setMsg(String msg) {
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    public IosAlertDialog setButton(String text, final OnClickListener listener) {
        if ("".equals(text)) {
            btn_neg.setText("确定");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        dialog.setOnDismissListener(dismissListener);
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        dialog.setOnKeyListener(onKeyListener);
    }

    public void show() {
        dialog.show();
    }
}
