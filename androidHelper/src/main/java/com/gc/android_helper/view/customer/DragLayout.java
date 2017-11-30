package com.gc.android_helper.view.customer;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by 郭灿 on 2017/4/1.
 * 侧滑面板
 */

public class DragLayout extends FrameLayout {
    private final static String TAG = DragLayout.class.getSimpleName();
    private ViewGroup mLeftContent;
    private ViewGroup mMainContent;
    private int dragRange;//可拖拽范围
    private int mWidth;//mMainContent 宽度
    private int mHeight;//mMainContent 高度

    private ViewDragHelper viewDragHelper;
    private OnDragStateChangeListener mOnDragStateChangeListener;
    public static final int OPEN = 0;
    public static final int CLOSE = 1;
    public static final int DRAGING = 2;
    private int currentState = CLOSE;
    public DragLayout(Context context) {
        this(context,null);
    }

    public DragLayout(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragLayout(Context context,AttributeSet attrs,int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * sensitivity:敏感度
         */
        viewDragHelper = ViewDragHelper.create(this,new MyViewDragHelperCallback());
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    public static interface OnDragStateChangeListener{
        public void open();
        public void close();
        public void draging(float percent);
    }
    private class MyViewDragHelperCallback extends ViewDragHelper.Callback{
        /**
         * 当前child是否可以拖动
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        /**
         * 返回水平方向的可拖拽范围
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return dragRange;
        }

        /**
         * 决定当前View变化后的位置
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if(child==mMainContent){//让左面板可以随意拖动
                return fixLeft(left);
            }
            return left;
        }

        /**
         * 当前View位置发生变化，回调(用作伴随动画)
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            int mainLeft = mMainContent.getLeft();
            //屏蔽左面板拖动
            if(changedView==mLeftContent){
                mLeftContent.layout(0,0,dragRange,mHeight);
                //同时有面板伴随移动
                mainLeft = mainLeft+dx;
                mainLeft = fixLeft(mainLeft);
                mMainContent.layout(mainLeft,0,mainLeft+mWidth,mHeight);
            }
            //伴随动画
            dispatchDragEvent(mainLeft);
            invalidate();//位置发生改变重绘View
        }

        /**
         * 拖拽松手后被回调
         * @param releasedChild 被释放的View
         * @param xvel x轴的滑动速度
         * @param yvel y轴的滑动速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel>50){
                open(true);
            }else if(xvel==0 && mMainContent.getLeft()>dragRange*0.5f){
                open(true);
            }else{
                close(true);
            }
        }

    }
    private float animPercent;
    /**
     * 伴随动画
     * @param mainLeft
     */
    private void dispatchDragEvent(int mainLeft) {
        animPercent = mainLeft*1.0f/dragRange;
        mLeftContent.setTranslationX(evaluate(animPercent,-dragRange/3,0));
        currentState = DRAGING;
        if(mOnDragStateChangeListener!=null){
            mOnDragStateChangeListener.draging(animPercent);
        }
    }

    /**
     * 估值器
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
    /**
     * 关闭侧滑菜单
     */
    public void close(boolean isSmooth) {
        int left = 0;
        if(isSmooth){
            //触发一个动画，平滑的滑动到指定位置
            if(viewDragHelper.smoothSlideViewTo(mMainContent,left,0)){
                //还没有滑动到指定位置
                ViewCompat.postInvalidateOnAnimation(this);//继续重绘此控件
            }
        }else {
            mMainContent.layout(left,0,left+mWidth,mHeight);
        }
        if(currentState!=CLOSE && mOnDragStateChangeListener!=null){
            mOnDragStateChangeListener.close();
        }
        currentState = CLOSE;
    }
    /**
     * 打开侧滑菜单
     */
    public void open(boolean isSmooth) {
        int left = dragRange;
        if(isSmooth){
            if(viewDragHelper.smoothSlideViewTo(mMainContent,left,0)){
                //还没有滑动到指定位置
                ViewCompat.postInvalidateOnAnimation(this);//继续重绘此控件
            }
        }else {
            mMainContent.layout(left,0,left+mWidth,mHeight);
        }
        if(currentState!=OPEN && mOnDragStateChangeListener!=null){
            mOnDragStateChangeListener.open();
        }
        currentState = OPEN;
    }

    /**
     * 重新绘制时回调
     * 高频率回调
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)){
            //还没有移动到位置
            ViewCompat.postInvalidateOnAnimation(this);//继续重绘此控件
        }
    }

    /**
     * 修正拖拽位置
     * @return
     */
    private int fixLeft(int left){
        if(left<0){
           return 0;
        }else if(left>dragRange){
            return dragRange;
        }
        return left;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()<2){
            throw new IllegalStateException("DragLayout has to be 2 child!");
        }
        if(!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("DragLayout childs has to be ViewGroup");
        }
        mLeftContent = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mespWidth = MeasureSpec.makeMeasureSpec(dragRange,MeasureSpec.EXACTLY);
        int mespHeight = MeasureSpec.makeMeasureSpec(mHeight,MeasureSpec.EXACTLY);
        mLeftContent.measure(mespWidth,mespHeight);
        mLeftContent.setTranslationX(-dragRange/3);
    }

    /**
     * onFinishInflate--》onMeas-->onSizeChanged
     * onFinishInflate之后会被回掉，测量中发现控件大小发生变化回调
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dragRange = (int) (0.8f*mMainContent.getMeasuredWidth());
        Log.i(TAG,"dragRange: "+dragRange);
        mWidth = mMainContent.getMeasuredWidth();
        mHeight = mMainContent.getMeasuredHeight();
        Log.i(TAG,"mMainContent.getMeasuredWidth(): "+mWidth);
        Log.i(TAG,"mMainContent.getMeasuredHeight(): "+mHeight);


    }

    /**
     * 是否拦截交给viewDragHelper处理
     * @param ev
     * @return
     */
    private float startY;
    private float startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsVpDragger;
    private final int mTouchSlop;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsVpDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if(mIsVpDragger) {
                    return false;
                }
                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果Y轴位移大于X轴位移，那么将事件交给ListView处理。
                if(distanceY > mTouchSlop && distanceY - distanceX>-50) {
                    mIsVpDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDragger = false;
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    /**
     * 触摸事件交给viewDragHelper
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public void setmOnDragStateChangeListener(OnDragStateChangeListener mOnDragStateChangeListener) {
        this.mOnDragStateChangeListener = mOnDragStateChangeListener;
    }
}
