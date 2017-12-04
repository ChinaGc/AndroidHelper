package com.gc.android_helper.view.picker;

import android.graphics.Typeface;
import android.view.View;

import com.gc.android_helper.view.picker.lib.WheelView;

import java.util.List;


/**
 * Created by 郭灿 on 2017/5/29.
 */

public class PickerParams<A,B,C> {
    private Picker.OnOptionsSelectListener optionsSelectListener;

    private String Str_Submit;//确定按钮文字
    private String Str_Cancel;//取消按钮文字
    private String Str_Title;//标题文字

    /*private int Color_Submit;//确定按钮颜色
    private int Color_Cancel;//取消按钮颜色
    private int Color_Title;//标题颜色*/

   /* private int Color_Background_Wheel;//滚轮背景颜色
    private int Color_Background_Title;//标题背景颜色*/
    //文字大小单位为px
/*    private int Size_Submit_Cancel;//确定取消按钮大小
    private int Size_Title;//标题文字大小
    private int Size_Content;//内容文字大小*/

    private boolean cancelable = true;//是否能取消
    private boolean linkage = true;//是否联动
    private boolean isCenterLabel = true;//是否只显示中间的label

   /* private int textColorOut; //分割线以外的文字颜色
    private int textColorCenter; //分割线之间的文字颜色
    private int dividerColor; //分割线的颜色*/
    private float lineSpacingMultiplier = 1.6F;
    //单位
   /* private String label1;
    private String label2;
    private String label3;*/

    private boolean cyclic1 = false;//是否循环，默认否
    private boolean cyclic2 = false;
    private boolean cyclic3 = false;

    private Typeface font;

    private int option1;//默认选中项
    private int option2;
    private int option3;

    private WheelView.DividerType dividerType;//分隔线类型
    private View parentView;

    private List<A> options1Items;
    private List<B> options2Items;
    private List<C> options3Items;

    /**
     * 根布局
     * @param parentView
     */
    public PickerParams(View parentView,List<A> options1Items){
        this.parentView = parentView;
        this.options1Items = options1Items;
    }
    public PickerParams(View parentView,List<A> options1Items,List<B> options2Items){
        this.parentView = parentView;
        this.options1Items = options1Items;
        this.options2Items = options2Items;
    }
    public PickerParams(View parentView,List<A> options1Items,List<B> options2Items,List<C> options3Items){
        this.parentView = parentView;
        this.options1Items = options1Items;
        this.options2Items = options2Items;
        this.options3Items = options3Items;
    }

    public void setOptionsSelectListener(Picker.OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
    }

    public void setStr_Submit(String str_Submit) {
        Str_Submit = str_Submit;
    }

    public void setStr_Cancel(String str_Cancel) {
        Str_Cancel = str_Cancel;
    }

    public void setStr_Title(String str_Title) {
        Str_Title = str_Title;
    }

  /*  public void setColor_Submit(int color_Submit) {
        Color_Submit = color_Submit;
    }

    public void setColor_Cancel(int color_Cancel) {
        Color_Cancel = color_Cancel;
    }

    public void setColor_Title(int color_Title) {
        Color_Title = color_Title;
    }*/

    /*public void setColor_Background_Wheel(int color_Background_Wheel) {
        Color_Background_Wheel = color_Background_Wheel;
    }

    public void setColor_Background_Title(int color_Background_Title) {
        Color_Background_Title = color_Background_Title;
    }

    public void setSize_Submit_Cancel(int size_Submit_Cancel) {
        Size_Submit_Cancel = size_Submit_Cancel;
    }*/

/*    public void setSize_Title(int size_Title) {
        Size_Title = size_Title;
    }

    public void setSize_Content(int size_Content) {
        Size_Content = size_Content;
    }*/

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setLinkage(boolean linkage) {
        this.linkage = linkage;
    }

    public void setCenterLabel(boolean centerLabel) {
        isCenterLabel = centerLabel;
    }

  /*  public void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
    }

    public void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }*/

    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
    }
/*
    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }*/

    public void setCyclic1(boolean cyclic1) {
        this.cyclic1 = cyclic1;
    }

    public void setCyclic2(boolean cyclic2) {
        this.cyclic2 = cyclic2;
    }

    public void setCyclic3(boolean cyclic3) {
        this.cyclic3 = cyclic3;
    }

    public void setFont(Typeface font) {
        this.font = font;
    }

    public void setOption1(int option1) {
        this.option1 = option1;
    }

    public void setOption2(int option2) {
        this.option2 = option2;
    }

    public void setOption3(int option3) {
        this.option3 = option3;
    }

    public void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
    }

    public Picker.OnOptionsSelectListener getOptionsSelectListener() {
        return optionsSelectListener;
    }

    public String getStr_Submit() {
        return Str_Submit;
    }

    public String getStr_Cancel() {
        return Str_Cancel;
    }

    public String getStr_Title() {
        return Str_Title;
    }

   /* public int getColor_Submit() {
        return Color_Submit;
    }

    public int getColor_Cancel() {
        return Color_Cancel;
    }

    public int getColor_Title() {
        return Color_Title;
    }

    public int getColor_Background_Wheel() {
        return Color_Background_Wheel;
    }

    public int getColor_Background_Title() {
        return Color_Background_Title;
    }

    public int getSize_Submit_Cancel() {
        return Size_Submit_Cancel;
    }

    public int getSize_Title() {
        return Size_Title;
    }

    public int getSize_Content() {
        return Size_Content;
    }*/

    public boolean isCancelable() {
        return cancelable;
    }

    public boolean isLinkage() {
        return linkage;
    }

    public boolean isCenterLabel() {
        return isCenterLabel;
    }

   /* public int getTextColorOut() {
        return textColorOut;
    }

    public int getTextColorCenter() {
        return textColorCenter;
    }

    public int getDividerColor() {
        return dividerColor;
    }*/

    public float getLineSpacingMultiplier() {
        return lineSpacingMultiplier;
    }

/*    public String getLabel1() {
        return label1;
    }

    public String getLabel2() {
        return label2;
    }

    public String getLabel3() {
        return label3;
    }*/

    public boolean isCyclic1() {
        return cyclic1;
    }

    public boolean isCyclic2() {
        return cyclic2;
    }

    public boolean isCyclic3() {
        return cyclic3;
    }

    public Typeface getFont() {
        return font;
    }

    public int getOption1() {
        return option1;
    }

    public int getOption2() {
        return option2;
    }

    public int getOption3() {
        return option3;
    }

    public WheelView.DividerType getDividerType() {
        return dividerType;
    }

    public View getParentView() {
        return parentView;
    }

    public List<A> getOptions1Items() {
        return options1Items;
    }

    public List<B> getOptions2Items() {
        return options2Items;
    }

    public List<C> getOptions3Items() {
        return options3Items;
    }
}
