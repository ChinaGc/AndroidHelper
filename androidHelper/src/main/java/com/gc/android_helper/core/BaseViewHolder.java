package com.gc.android_helper.core;

import android.view.View;

/**
 * Created by 郭灿 on 2017/3/27.
 */

public abstract class BaseViewHolder<T> {
    protected Api api;
    public BaseViewHolder(){
        api = Api.getInstance();
        contentView = initContentView();
        contentView.setTag(this);
    }
    protected View contentView;
    /**
     * 返回ListView单个条目的View
     */
    protected abstract View initContentView();

    /**
     * 绑定ListView单个条目的数据
     */
    protected abstract void bindData(T data,int position);

    public View getContentView() {
        return contentView;
    }
}
