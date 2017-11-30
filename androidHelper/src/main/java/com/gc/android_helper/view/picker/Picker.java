package com.gc.android_helper.view.picker;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.android_helper.view.picker.PickerParams;
import com.gc.android_helper.view.picker.view.WheelOptions;
import com.gc.androidhelper.R;

/**
 * Created by 郭灿 on 2017/5/29.
 */

public class Picker<T> implements View.OnClickListener, PopupWindow.OnDismissListener {

    private PopupWindow popupWindow;// picker载体

    private Activity activity;

    private View contentView;

    private Button btnSubmit, btnCancel; // 确定、取消按钮

    private TextView tvTitle;

    private RelativeLayout rv_top_bar;

    private WheelOptions wheelOptions;

    private LinearLayout optionsPicker;

    private WindowManager.LayoutParams layoutParams;

    private PickerParams pickerParams;

    // Required
    public Picker(Activity activity) {
        this.activity = activity;
        layoutParams = activity.getWindow().getAttributes();
        initView();
    }

    public void initView() {
        this.contentView = View.inflate(activity, R.layout.pickerview_options, null);
        btnSubmit = (Button) contentView.findViewById(R.id.btnSubmit);
        btnCancel = (Button) contentView.findViewById(R.id.btnCancel);
        tvTitle = (TextView) contentView.findViewById(R.id.tvTitle);
        rv_top_bar = (RelativeLayout) contentView.findViewById(R.id.rv_topbar);
        // 滚轮布局
        optionsPicker = (LinearLayout) contentView.findViewById(R.id.optionspicker);
        wheelOptions = new WheelOptions(optionsPicker, true);
        // 属性设置
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    /**
     * 设置并展示数据
     * 
     * @param pickerParams
     */
    public void picker(PickerParams pickerParams) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        // 背景透明度
        setBackgroundAlpha(1, 0.5f);
        if (pickerParams == this.pickerParams) {
            popupWindow.showAtLocation(pickerParams.getParentView(), Gravity.BOTTOM, 0, 0);
            setDefaultSelected(pickerParams);
            return;
        }
        if (pickerParams.isLinkage()) {
            wheelOptions.setPicker(pickerParams.getOptions1Items(), pickerParams.getOptions2Items(), pickerParams.getOptions3Items());
        } else {
            wheelOptions.setNPicker(pickerParams.getOptions1Items(), pickerParams.getOptions2Items(), pickerParams.getOptions3Items());
        }
        setDefaultSelected(pickerParams);
        this.pickerParams = pickerParams;
        // popupwindow
        popupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 设置动画
        popupWindow.setAnimationStyle(R.style.bottomInWindow);
        // 设置位置
        popupWindow.showAtLocation(pickerParams.getParentView(), Gravity.BOTTOM, 0, 0);
        // 设置消失监听
        popupWindow.setOnDismissListener(this);
        // 设置文字
        btnSubmit.setText(TextUtils.isEmpty(pickerParams.getStr_Submit()) ? activity.getResources().getString(R.string.pickerview_submit) : pickerParams.getStr_Submit());
        btnCancel.setText(TextUtils.isEmpty(pickerParams.getStr_Cancel()) ? activity.getResources().getString(R.string.pickerview_cancel) : pickerParams.getStr_Cancel());
        tvTitle.setText(TextUtils.isEmpty(pickerParams.getStr_Title()) ? "" : pickerParams.getStr_Title());// 默认为空
        // 设置color
        btnSubmit.setTextColor(pickerParams.getColor_Submit() == 0 ? activity.getResources().getColor(R.color.ios_text_color) : pickerParams.getColor_Submit());
        btnCancel.setTextColor(pickerParams.getColor_Cancel() == 0 ? activity.getResources().getColor(R.color.ios_text_color) : pickerParams.getColor_Cancel());
        tvTitle.setTextColor(pickerParams.getColor_Title() == 0 ? activity.getResources().getColor(R.color.dialog_msg) : pickerParams.getColor_Title());
        rv_top_bar.setBackgroundColor(pickerParams.getColor_Background_Title() == 0 ? activity.getResources().getColor(R.color.pickerview_bg_topbar) : pickerParams.getColor_Background_Title());

        // 设置文字大小
        btnSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX, pickerParams.getSize_Submit_Cancel() == 0 ? activity.getResources().getDimensionPixelSize(R.dimen.font_normal) : pickerParams.getSize_Submit_Cancel());
        btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, pickerParams.getSize_Submit_Cancel() == 0 ? activity.getResources().getDimensionPixelSize(R.dimen.font_normal) : pickerParams.getSize_Submit_Cancel());

        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, pickerParams.getSize_Title() == 0 ? activity.getResources().getDimensionPixelSize(R.dimen.font_large) : pickerParams.getSize_Title());

        optionsPicker.setBackgroundColor(pickerParams.getColor_Background_Wheel() == 0 ? activity.getResources().getColor(R.color.pickerview_bgColor_default) : pickerParams.getColor_Background_Wheel());

        // wheelOptions.setTextContentSizeSp(
        // pickerParams.getSize_Content()==0?(int)activity.getResources().getDimension(R.dimen.font_small):pickerParams.getSize_Content());
        wheelOptions.setLabels(pickerParams.getLabel1(), pickerParams.getLabel2(), pickerParams.getLabel3());
        wheelOptions.setCyclic(pickerParams.isCyclic1(), pickerParams.isCyclic2(), pickerParams.isCyclic3());
        wheelOptions.setTypeface(pickerParams.getFont());
        // 有默认值
        wheelOptions.setDividerColor(pickerParams.getDividerColor());
        wheelOptions.setDividerType(pickerParams.getDividerType());
        wheelOptions.setLineSpacingMultiplier(pickerParams.getLineSpacingMultiplier());
        wheelOptions.setTextColorOut(pickerParams.getTextColorOut());
        wheelOptions.setTextColorCenter(pickerParams.getTextColorCenter());
        wheelOptions.isCenterLabel(pickerParams.isCenterLabel());

    }

    /**
     * 设置默认选中项
     * 
     * @param pickerParams
     */
    public void setDefaultSelected(PickerParams pickerParams) {
        wheelOptions.setCurrentItems(pickerParams.getOption1(), pickerParams.getOption2(), pickerParams.getOption3());
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v
     *            The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            // callback
            if (pickerParams.getOptionsSelectListener() != null) {
                int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                pickerParams.getOptionsSelectListener().onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2], v);
            }
        }
        popupWindow.dismiss();
    }

    /**
     * 选中callback
     */
    public interface OnOptionsSelectListener {
        void onOptionsSelect(int options1, int options2, int options3, View v);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(0.5f, 1);
    }

    // 设置屏幕背景透明效果
    private AlphaAnimatorUpdateListener alphaAnimatorUpdateListenerIn;

    private AlphaAnimatorUpdateListener alphaAnimatorUpdateListenerOut;

    private ValueAnimator valueAnimatorIn;

    private ValueAnimator valueAnimatorOut;

    public void setBackgroundAlpha(float alpha, float target) {
        if (alpha > target) {// in
            if (valueAnimatorIn == null) {
                valueAnimatorIn = ValueAnimator.ofFloat(alpha, target);
            }
            if (alphaAnimatorUpdateListenerIn == null) {
                alphaAnimatorUpdateListenerIn = new AlphaAnimatorUpdateListener();
                valueAnimatorIn.addUpdateListener(alphaAnimatorUpdateListenerIn);
            }
            valueAnimatorIn.setDuration(300);
            valueAnimatorIn.start();
        } else {// out
            if (valueAnimatorOut == null) {
                valueAnimatorOut = ValueAnimator.ofFloat(alpha, target);
            }
            if (alphaAnimatorUpdateListenerOut == null) {
                alphaAnimatorUpdateListenerOut = new AlphaAnimatorUpdateListener();
                valueAnimatorOut.addUpdateListener(alphaAnimatorUpdateListenerOut);
            }
            valueAnimatorOut.setDuration(300);
            valueAnimatorOut.start();
        }

    }

    private class AlphaAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float Value = (float) animation.getAnimatedValue();
            layoutParams.alpha = Value;
            activity.getWindow().setAttributes(layoutParams);
        }
    }
}
