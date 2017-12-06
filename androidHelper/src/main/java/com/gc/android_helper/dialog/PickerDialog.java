package com.gc.android_helper.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.android_helper.core.BasePopupWindow;
import com.gc.android_helper.view.picker.PickerParams;
import com.gc.android_helper.view.picker.view.WheelOptions;
import com.gc.androidhelper.R;

/**
 * @author gc
 * @ClassName: PickerDiaLog
 * @Description: 仿IOS支持三级联动的选择器
 * @date 2017/11/30
 */
public class PickerDialog extends BasePopupWindow implements View.OnClickListener {

    private static PickerDialog pickerDialog = null;

    private View pickerView = null;

    private Button btnSubmit, btnCancel; // 确定、取消按钮

    private TextView tvTitle;

    private RelativeLayout topBar;

    private WheelOptions wheelOptions;

    private LinearLayout optionsPicker;

    private PickerParams pickerParams = null;

    private Context context;

    private PickerDialog(Window window) {
        super(window);
        this.context = window.getContext();
        initView();
    }
    @Override
    protected View getContentView() {
        return pickerView;
    }

    private void initView() {
        pickerView = View.inflate(context, R.layout.pickerview_options, null);
        btnSubmit = (Button) pickerView.findViewById(R.id.btnSubmit);
        btnCancel = (Button) pickerView.findViewById(R.id.btnCancel);
        tvTitle = (TextView) pickerView.findViewById(R.id.tvTitle);
        topBar = (RelativeLayout) pickerView.findViewById(R.id.rv_topbar);
        // 滚轮布局
        optionsPicker = (LinearLayout) pickerView.findViewById(R.id.optionspicker);
        wheelOptions = new WheelOptions(optionsPicker, true);
        // 属性设置
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public static PickerDialog getInstance(Window window) {
        if (pickerDialog == null) {
            pickerDialog = new PickerDialog(window);
        }
        return pickerDialog;
    }

    public void show(PickerParams pickerParams) {
        this.pickerParams = pickerParams;
        btnSubmit.setText("确定");
        btnCancel.setText("取消");
        tvTitle.setText(TextUtils.isEmpty(pickerParams.getStr_Title()) ? "" : pickerParams.getStr_Title());// 默认为空
        // 设置color
        btnSubmit.setTextColor(context.getResources().getColor(R.color.ios_text_color));
        btnCancel.setTextColor(context.getResources().getColor(R.color.ios_text_color));
        tvTitle.setTextColor(context.getResources().getColor(R.color.dialog_msg));
        topBar.setBackgroundColor(context.getResources().getColor(R.color.pickerview_bg_topbar));
        // 设置文字大小
        btnSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.font_normal));
        btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.font_normal));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.font_large));
        optionsPicker.setBackgroundColor(context.getResources().getColor(R.color.pickerview_bgColor_default));
         wheelOptions.setTextContentSizeSp((int)context.getResources().getDimension(R.dimen.font_small));
        if (pickerParams.isLinkage()) {
            wheelOptions.setPicker(pickerParams.getOptions1Items(), pickerParams.getOptions2Items(), pickerParams.getOptions3Items());
        } else {
            wheelOptions.setNPicker(pickerParams.getOptions1Items(), pickerParams.getOptions2Items(), pickerParams.getOptions3Items());
        }
        setDefaultSelected(pickerParams);
       // wheelOptions.setLabels(pickerParams.getLabel1(), pickerParams.getLabel2(), pickerParams.getLabel3());
        wheelOptions.setCyclic(pickerParams.isCyclic1(), pickerParams.isCyclic2(), pickerParams.isCyclic3());
        wheelOptions.setTypeface(pickerParams.getFont());
        // 有默认值
//        wheelOptions.setDividerColor(pickerParams.getDividerColor());
//        wheelOptions.setDividerType(pickerParams.getDividerType());
//        wheelOptions.setLineSpacingMultiplier(pickerParams.getLineSpacingMultiplier());
//        wheelOptions.setTextColorOut(pickerParams.getTextColorOut());
//        wheelOptions.setTextColorCenter(pickerParams.getTextColorCenter());
//        wheelOptions.isCenterLabel(pickerParams.isCenterLabel());
        showPopupWindow(pickerParams.getParentView());
    }

    //设置默认选中项
    private void setDefaultSelected(PickerParams pickerParams) {
        wheelOptions.setCurrentItems(pickerParams.getOption1(), pickerParams.getOption2(), pickerParams.getOption3());
    }
    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            // callback
            if (pickerParams.getOptionsSelectListener() != null) {
                int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                pickerParams.getOptionsSelectListener().onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2], v);
            }
        }
       hide();
    }
}
