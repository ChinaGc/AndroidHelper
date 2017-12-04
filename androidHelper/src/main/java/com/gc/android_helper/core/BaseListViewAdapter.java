package com.gc.android_helper.core;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 郭灿 on 2017/3/27.
 */

public abstract class BaseListViewAdapter<T> extends BaseAdapter {
    private List<T> datas;
    private final int LOADMORE_TYPE=0;
    private final int ITEM_TYPE=1;
    public BaseListViewAdapter(List<T> datas) {
        this.datas = datas;
    }
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading = false;
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }
    private LoadMoreHolder loadMoreHolder;

    public void onLoadMore() {
        if(this.onLoadMoreListener!=null && !isLoading){
            isLoading = true;
            onLoadMoreListener.onLoadMore(loadMoreHolder);
        }
    }
    public interface OnLoadMoreListener{
        public void onLoadMore(LoadMoreHolder holder);
    }

    @Override
    public int getCount() {
        return datas.size()+1;
    }
    @Override
    public Object getItem(int position) {
       if(position==datas.size()){
          return position;
        }
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * item类型数
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * 根据位置返回类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(position==datas.size()){//最后一条
            return LOADMORE_TYPE;
        }
        return ITEM_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = null;
        Log.i("BaseListViewAdapter","convertView = "+convertView);
        int type = getItemViewType(position);
        if(convertView==null){
            switch (type){
                case LOADMORE_TYPE:
                    holder = getLoadMoreHolder();
                    break;
                case ITEM_TYPE:
                    holder = getHolder();
                    break;
            }
        }else{
            switch (type){
                case LOADMORE_TYPE:
                    holder = (BaseViewHolder) convertView.getTag();
                    break;
                case ITEM_TYPE:
                    holder = (BaseViewHolder) convertView.getTag();
                    break;
            }
        }
        switch (type){
            case LOADMORE_TYPE:
                onLoadMore();
                break;
            case ITEM_TYPE:
                holder.bindData(datas.get(position),position);
                break;
        }
        return holder.getContentView();
    }
    protected abstract BaseViewHolder getHolder();

    public LoadMoreHolder getLoadMoreHolder() {
        if(loadMoreHolder==null){
            loadMoreHolder = new LoadMoreHolder(this);
        }
        return loadMoreHolder;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }
}
