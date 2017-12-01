package com.gc.android_helper.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.android_helper.util.LengthUtil;
import com.gc.androidhelper.R;

import static com.gc.androidhelper.R.id.action_listview;

/**
 * @author 郭灿
 * @ClassName:
 * @Description: 仿IOS底部弹出选择器 基于Popwindow
 * @date 2017/11/30
 */
public class ActionSheet extends implements View.OnClickListener{

    private static ActionSheet sheet = null;

    private View actionView = null;

    private TextView titleView,cancelView;
    private LinearLayout buttonsView;


    private Context context;
    private ActionSheet(Window window) {
        this.context = window.getContext();
        popupWindowHelper = PopupWindowHelper.getInstance(window);
        this.actionView = View.inflate(context, R.layout.action_sheet,null);
        titleView = (TextView) actionView.findViewById(R.id.title);
        cancelView = (TextView) actionView.findViewById(R.id.tv_cancel);
        buttonsView = (LinearLayout) actionView.findViewById(action_listview);
    }
    //获取单实例
    public static ActionSheet getInstance(Window window) {
        if(sheet==null){
            sheet = new ActionSheet(window);
        }
        return sheet;
    }

    public void show(String title,String [] buttons,View parentView,OnItemClickListener itemClickListener) {
        title = TextUtils.isEmpty(title)?"提示":title;
        titleView.setText(title);
        cancelView.setText("取消");
        cancelView.setOnClickListener(this);
        //动态添加按钮
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        for (int i = 1; i <= buttons.length; i++) {
            TextView textView = new TextView(this.context);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(LengthUtil.dip2px(context,13), LengthUtil.dip2px(context, 13), LengthUtil.dip2px(context, 13), LengthUtil.dip2px(context,13));
            textView.setText(buttons[i - 1]);
            textView.setTextColor(context.getResources().getColor(R.color.ios_text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimensionPixelSize(R.dimen.font_normal));
            textView.setOnClickListener(new ButtonsOnClickListener(i,itemClickListener));
            buttonsView.addView(textView, textViewParams);
            if (i != buttons.length) {
                View line = new View(context);
                line.setBackgroundColor(context.getResources().getColor(R.color.dialog_line));
                buttonsView.addView(line, viewParams);
                textView.setBackgroundResource(R.drawable.actionsheet_text_fang_bg);
            } else {
                textView.setBackgroundResource(R.drawable.actionsheet_text_halfang_bg);
            }
        }
        this.popupWindowHelper.showPopupWindow(actionView,parentView);
    }
    //取消按钮被点击
    @Override
    public void onClick(View v) {
        popupWindowHelper.hide();
    }

    //用户点击回调
    public interface OnItemClickListener{
        public void onClick(int position);
    }

    private class ButtonsOnClickListener implements View.OnClickListener{
        private int position;
        private OnItemClickListener itemClickListener;
        public ButtonsOnClickListener(int position,OnItemClickListener itemClickListener){
            this.position = position;
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(position);
        }
    }
}
