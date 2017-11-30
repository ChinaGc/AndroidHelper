package com.gc.android_helper.view.customer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 粘性控件
 * Created by 郭灿 on 2017/4/5.
 */

public class GooView extends View {
    private Paint paint;//画笔
    public GooView(Context context) {
        this(context,null);
    }
    public GooView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
    }

    /**
     * 绘制控件
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        //画不规则的封闭区域
        Path path = new Path();//路径



        //画固定圆
        //圆心x,圆心y 半径 画笔
        canvas.drawCircle(600f,600f,20f,paint);//在画布上画园
        //画拖拽圆
        canvas.drawCircle(400f,400f,40f,paint);//在画布上画园
    }
}
