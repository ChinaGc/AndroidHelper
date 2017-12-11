package com.gc.android_helper.view.customer;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.android_helper.adapter.BannerViewAdapter;
import com.gc.android_helper.bean.Banner;
import com.gc.android_helper.util.LengthUtil;
import com.gc.androidhelper.R;

import java.util.List;



/**
 * 自定义BannerView
 * @author guocan
 *
 */
public class BannerView extends RelativeLayout{
	
	private Context context;
	private View banner_view;
	private AutoScrollViewPager autoscroll_viewpager;
	private TextView title;
	private LinearLayout point_List;
	private LinearLayout botoom_layout;
	BannerViewAdapter adapter;
	//监听器
	private OnPageChangeListener listener;

	/**
	 * 在布局文件中被实例化时被调用
	 * @param context
	 * @param attrs
	 */
	public BannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
		initStyle(attrs);
	}

	/**
	 * 代码实例化
	 * @param context
     */
	public BannerView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public void initView(){
		banner_view = View.inflate(context,R.layout.banner_view, BannerView.this);
		autoscroll_viewpager = (AutoScrollViewPager) banner_view.findViewById(R.id.autoscroll_viewpager);
		title = (TextView) banner_view.findViewById(R.id.title);
		point_List = (LinearLayout) banner_view.findViewById(R.id.point_List);
		botoom_layout = (LinearLayout) banner_view.findViewById(R.id.botoom_layout);
	}
	
	public void initStyle(AttributeSet attrs){
		TypedArray styles = context.obtainStyledAttributes(attrs,R.styleable.BannerView);
		boolean title_show = styles.getBoolean(R.styleable.BannerView_title_show, true);
		int bottom_color = styles.getColor(R.styleable.BannerView_bottom_color,getResources().getColor(R.color.botoom_bg));
		if(title_show){
			title.setVisibility(View.VISIBLE);
		}else{
			title.setVisibility(View.GONE);
		}
		botoom_layout.setBackgroundColor(bottom_color);
	}
	private LinearLayout.LayoutParams params;
	public void setBannerViewData(final List<Banner> bannerList){
		if(adapter==null){
			adapter = new BannerViewAdapter(bannerList,context);
			autoscroll_viewpager.setAdapter(adapter);//利用适配器进行数据的绑定
			autoscroll_viewpager.setScrollFactgor(5);//滚动速度
			//autoscroll_viewpager.setOffscreenPageLimit(4);//缓存4个
		    //autoscroll_viewpager.startAutoScroll(10000);//5秒自动翻转
			//autoscroll_viewpager.stopAutoScroll();
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(LengthUtil.dip2px(context, 8), 0, 0, 0);
			autoscroll_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					if(listener!=null){listener.onPageSelected(position);}
					title.setText(bannerList.get(position).getTitle());
					for(int i=0;i<point_List.getChildCount();i++){
						if(i==position){
							point_List.getChildAt(i).setEnabled(true);
						}else{
							point_List.getChildAt(i).setEnabled(false);
						}
					}
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					if(listener!=null){listener.onPageScrolled(arg0, arg1, arg2);}
				}
				@Override
				public void onPageScrollStateChanged(int arg0) {
					if(listener!=null){listener.onPageScrollStateChanged(arg0);}
				}
			});
		}else{
			//adapter.notifyDataSetChanged();
			autoscroll_viewpager.setAdapter(adapter);
			autoscroll_viewpager.getAdapter().notifyDataSetChanged();

		}
		point_List.removeAllViews();
		for (int i=0;i<bannerList.size();i++) {
			ImageView image = new ImageView(context);
			image.setBackgroundResource(R.drawable.bannerview_point_bg);
			point_List.addView(image,params);
			image.setEnabled(false);
		}
	}
	
	public void setOnBannerViewItemClickListener(AutoScrollViewPager.OnPageClickListener clickListener){
		autoscroll_viewpager.setOnPageClickListener(clickListener);

	}
	public void setOnBannerViewChangeListener(OnPageChangeListener listener){
		this.listener = listener;
	}
	int downX = -1;
	int downY = -1;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				downX = (int) ev.getX();
				downY = (int) ev.getY();
				this.requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_MOVE:
				int moveX = (int) ev.getX();
				int moveY = (int) ev.getY();
				if(Math.abs(moveY-downY)>Math.abs(moveX-downX)){
					this.requestDisallowInterceptTouchEvent(false);
				}else{
					this.requestDisallowInterceptTouchEvent(true);
				}
				break;
			case MotionEvent.ACTION_UP:break;

		}
		return super.dispatchTouchEvent(ev);
	}

	public void startScroll(int m){
		autoscroll_viewpager.startAutoScroll(m);
	}

	public void stopScroll(){
		autoscroll_viewpager.stopAutoScroll();
	}
}
