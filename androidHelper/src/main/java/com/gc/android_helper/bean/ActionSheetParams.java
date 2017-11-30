package com.gc.android_helper.bean;

import android.view.View;

public class ActionSheetParams {

	private String title = "提示";
	private String cancelTitle = "取消";
	private String [] buttions;
	private View parentView;

	public ActionSheetParams(View parentView){
		this.parentView = parentView;
	};
	public ActionSheetParams(String [] buttons,View parentView){
		this.buttions = buttons;
		this.parentView = parentView;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCancelTitle() {
		return cancelTitle;
	}
	public void setCancelTitle(String cancelTitle) {
		this.cancelTitle = cancelTitle;
	}
	public String[] getButtions() {
		return buttions;
	}
	public void setButtions(String[] buttions) {
		this.buttions = buttions;
	}
	public View getParentView() {
		return parentView;
	}
	public void setParentView(View parentView) {
		this.parentView = parentView;
	}

}
