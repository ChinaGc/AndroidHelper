package com.gc.android_helper.dialog;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.android_helper.bean.ActionSheetParams;
import com.gc.android_helper.util.LengthUtil;
import com.gc.androidhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guocan on 2017/3/9.
 */

public class ActionSheet implements PopupWindow.OnDismissListener,View.OnClickListener{

    private PopupWindow actionsheet;
    private ActionSheetParams ac;
    private List<ActionSheetClickListener> clickListenerList;
    private Activity activity;
    private WindowManager.LayoutParams layoutParams;
    public ActionSheet(Activity activity){
        this.activity = activity;
        clickListenerList = new ArrayList<ActionSheetClickListener>();
        layoutParams = activity.getWindow().getAttributes();
    }
    /**
     * 显示actlongionsheet
     * @param ac
     * @param
     */
    public void actionSheet(ActionSheetParams ac, ActionSheetClickListener actionSheetClickListener){
        //防止重复按按钮
        if (actionsheet!=null && actionsheet.isShowing()) {
            return;
        }
        //背景透明度
        setBackgroundAlpha(1,0.5f);
        if(this.ac==ac && activity!=null){
            actionsheet.showAtLocation(ac.getParentView(), Gravity.BOTTOM, 0, 0);
            return;
        }
        this.ac=ac;
        if(actionSheetClickListener!=null){
            this.clickListenerList.add(actionSheetClickListener);
        }
        View view = View.inflate(activity, R.layout.action_sheet, null);
        actionsheet = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        actionsheet.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        actionsheet.setFocusable(true);
        actionsheet.setOutsideTouchable(true);
        //设置动画
        actionsheet.setAnimationStyle(R.style.bottomInWindow);
        //设置位置
        actionsheet.showAtLocation(ac.getParentView(), Gravity.BOTTOM, 0, 0);
        //设置消失监听
        actionsheet.setOnDismissListener(this);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(ac.getTitle());
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this);
        LinearLayout action_listview = (LinearLayout) view.findViewById(R.id.action_listview);
        String[] buttons = ac.getButtions();
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,1);
        for(int i=1;i<=buttons.length;i++){
            TextView textView = new TextView(activity);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(LengthUtil.dip2px(activity, 13),LengthUtil.dip2px(activity, 13),LengthUtil.dip2px(activity, 13),LengthUtil.dip2px(activity, 13));
            textView.setText(buttons[i-1]);
            textView.setTextColor(activity.getResources().getColor(R.color.ios_text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.font_normal));
            textView.setOnClickListener(new OnItemClickListener(i));
            action_listview.addView(textView,textViewParams);
            if(i!=buttons.length){
                View line = new View(activity);
                line.setBackgroundColor(activity.getResources().getColor(R.color.dialog_line));
                action_listview.addView(line,viewParams);
                textView.setBackgroundResource(R.drawable.actionsheet_text_fang_bg);
            }else{
                textView.setBackgroundResource(R.drawable.actionsheet_text_halfang_bg);
            }
        }

    }
    private class OnItemClickListener implements View.OnClickListener{

        private int position;
        public OnItemClickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view){
            actionsheet.dismiss();
            for (ActionSheetClickListener actionSheetClickListener:clickListenerList) {
                actionSheetClickListener.onClick(position);
            }
        }

    }
    public interface ActionSheetClickListener{
        public void onClick(int position);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(0.5f,1);
    }
    //设置屏幕背景透明效果
    private AlphaAnimatorUpdateListener alphaAnimatorUpdateListenerIn;
    private AlphaAnimatorUpdateListener alphaAnimatorUpdateListenerOut;
    private ValueAnimator valueAnimatorIn;
    private ValueAnimator valueAnimatorOut;
    public void setBackgroundAlpha(float alpha,float target) {
        if(alpha>target){//in
            if(valueAnimatorIn==null){
                valueAnimatorIn = ValueAnimator.ofFloat(alpha,target);
            }
            if(alphaAnimatorUpdateListenerIn==null){
                alphaAnimatorUpdateListenerIn = new AlphaAnimatorUpdateListener();
                valueAnimatorIn.addUpdateListener(alphaAnimatorUpdateListenerIn);
            }
            valueAnimatorIn.setDuration(300);
            valueAnimatorIn.start();
        }else{//out
            if(valueAnimatorOut==null){
                valueAnimatorOut = ValueAnimator.ofFloat(alpha,target);
            }
            if(alphaAnimatorUpdateListenerOut==null){
                alphaAnimatorUpdateListenerOut = new AlphaAnimatorUpdateListener();
                valueAnimatorOut.addUpdateListener(alphaAnimatorUpdateListenerOut);
            }
            valueAnimatorOut.setDuration(300);
            valueAnimatorOut.start();
        }

    }
    private class AlphaAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener{
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float Value = (float) animation.getAnimatedValue();
            layoutParams.alpha = Value;
            activity.getWindow().setAttributes(layoutParams);
        }
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_cancel){
            actionsheet.dismiss();
        }
    }
}
