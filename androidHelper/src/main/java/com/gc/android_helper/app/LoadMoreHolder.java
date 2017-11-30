package com.gc.android_helper.app;

import android.view.View;
import android.widget.RelativeLayout;

import com.gc.androidhelper.R;

/**
 * Created by 郭灿 on 2017/3/27.
 */

public class LoadMoreHolder extends BaseViewHolder<Integer> implements View.OnClickListener{
    public static final int LOADING = 0;//加载中
    public static final int SUCCESS = 0;//加载成功
    public static final int FAILED = 0;//加载失败

    private RelativeLayout rl_loading;
    private RelativeLayout rl_error;
    private BaseListViewAdapter adapter;
    public LoadMoreHolder(BaseListViewAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 返回ListView单个条目的View
     */
    @Override
    protected View initContentView() {
        View view  = View.inflate(api.getContext(), R.layout.refresh_footer,null);
        rl_error = (RelativeLayout) view.findViewById(R.id.rl_error);
        rl_loading = (RelativeLayout) view.findViewById(R.id.rl_loading);
        rl_error.setOnClickListener(this);
        return view;
    }
    /**
     * 绑定ListView单个条目的数据
     *
     * @param state
     * @param position
     */
    @Override
    public void bindData(Integer state, int position) {
        rl_error.setVisibility(state==FAILED?View.VISIBLE:View.GONE);
        rl_loading.setVisibility(state==SUCCESS?View.GONE:View.GONE);
        adapter.setLoading(false);
    }

    @Override
    public View getContentView() {
        adapter.onLoadMore();
        rl_loading.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        return super.getContentView();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        rl_loading.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        adapter.onLoadMore();
    }
}
