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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.android_helper.util.LengthUtil;
import com.gc.androidhelper.R;

public class IosConfirmDialog {
    private Context context;

    private Dialog dialog;

    private LinearLayout lLayout_bg;

    private TextView txt_title;

    private TextView txt_msg;

    private TextView btn_neg;

    private TextView btn_pos;

    private View img_line;

    public IosConfirmDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    }

    public IosConfirmDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.confirm_dialog_ios, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        // txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        btn_neg = (TextView) view.findViewById(R.id.btn_neg);
        btn_pos = (TextView) view.findViewById(R.id.btn_pos);
        img_line = view.findViewById(R.id.img_line);
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

    public IosConfirmDialog setTitle(String title) {
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public IosConfirmDialog setMsg(String msg) {
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    public IosConfirmDialog setPositiveButton(String text, final OnClickListener listener) {
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
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

    public IosConfirmDialog setNegativeButton(String text, final OnClickListener listener) {
        if ("".equals(text)) {
            btn_neg.setText("取消");
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

    public void setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
    }

    public void show() {
        dialog.show();
    }
}
