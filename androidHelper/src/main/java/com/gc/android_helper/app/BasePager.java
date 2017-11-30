package com.gc.android_helper.app;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import com.gc.androidhelper.R;

import static android.view.View.inflate;

/**
 * Created by guocan on 2017/3/13.
 */

public abstract class BasePager<T> {
    public FrameLayout frameLayout;
    public Context context;
    private final int TO_LOADING = 0;//等待加载
    private final int IS_LOADING = 1;//加载中
    public final static int LOADING_SUCCESS = 2;//加载成功
    public final static int LOADING_FAILED = 3;//加载失败
    public final static int LOADING_EMPTY = 4;//数据为空
    public final static int LOADING_NONET = 5;//没有网络
    public final static String PAGER_NAME = "PAGER_NAME";
    private int pageState = TO_LOADING;//当前页面状态
    protected Api api;
    private View loadingView;
    private View successView;
    private View failedView;
    private View emptyView;
    private View nonetView;
    private String pagerName;
    public BasePager(Context context){
        api = Api.getInstance();
        this.context = context;
        initView();
        showPageByState();

        pagerName = this.getClass().getSimpleName();
    }
    private void initView(){
        if(frameLayout==null){
            frameLayout = new FrameLayout(context);
        }else{
            frameLayout.removeAllViews();
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        loadingView = this.createLoadingView();
        frameLayout.addView(loadingView,layoutParams);
        successView = this.createView();
        frameLayout.addView(successView,layoutParams);
        failedView = this.createFailedView();
        frameLayout.addView(failedView,layoutParams);
        emptyView = this.createEmptyView();
        frameLayout.addView(emptyView,layoutParams);
        nonetView = this.createNoNetView();
    }


    /**
     * 通过状态改变页面的显示
     */
    private void showPageByState(){
        loadingView.setVisibility(pageState==TO_LOADING || pageState==IS_LOADING? View.VISIBLE: View.INVISIBLE);
        successView.setVisibility(pageState==LOADING_SUCCESS?View.VISIBLE: View.INVISIBLE);
        failedView.setVisibility(pageState==LOADING_FAILED?View.VISIBLE: View.INVISIBLE);
        emptyView.setVisibility(pageState==LOADING_EMPTY?View.VISIBLE: View.INVISIBLE);
        nonetView.setVisibility(pageState==LOADING_NONET?View.VISIBLE: View.INVISIBLE);
    }

    /**
     * 展示页面并加载数据
     */
    public void show(T data){
        synchronized (this){
            if(pageState==LOADING_FAILED){
                pageState=TO_LOADING;
            }
            showPageByState();
            if(pageState!=LOADING_SUCCESS && pageState!=IS_LOADING){
                if(pageState==TO_LOADING){
                    pageState = IS_LOADING;
                }
                bindData(data);
            }
        }
    }

    protected void onLoadingComplete(int pageState){
        this.pageState = pageState;
        showPageByState();
    }

    /**
     * 创建失败的View
     * @return
     */
    protected View createFailedView() {
        return inflate(context, R.layout.loading_failed,null);
    }

    /**
     * 创建没有数据的View
     * @return
     */
    protected View createEmptyView(){
        return View.inflate(context,R.layout.loading_empty,null);
    }
    /**
     * 创建成功的View
     * @return
     */
    protected abstract View createView();

    /**
     * 创建加载中的View
     * @return
     */
    protected View createLoadingView(){
        return View.inflate(context, R.layout.loading_view,null);
    }

    /**
     * 创建没有网络的View
     * @return
     */
    private View createNoNetView() {
        return View.inflate(context, R.layout.loading_nonet,null);
    }
    public View getView(){
        return frameLayout;
    }


    /**
     * 为成页面添加数据
     */
    protected abstract void bindData(T data);

    public BaseActivity getActivity(){
        return (BaseActivity) context;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }

    public void startActivity(Intent intent){
        context.startActivity(intent);
    }

    /**
     * 每个不同的pager的requestCode均不能相同
     * @param intent
     * @param requestCode
     */
    public void startActivityForResult(Intent intent,int requestCode){
        ((BaseActivity) context).registPager(this);
        ((BaseActivity)context).startActivityForResult(intent,requestCode);
    }

    public String getPagerName() {
        return pagerName;
    }

    /**
     * 权限被拒绝回调
     * @param requestCode
     */
    protected void userRejectPermissionRequest(int requestCode){
        api.toast("permission has been reject : requestCode="+requestCode);
    }
}
