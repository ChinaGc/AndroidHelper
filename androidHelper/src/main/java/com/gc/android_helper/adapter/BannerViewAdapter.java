package com.gc.android_helper.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gc.android_helper.core.Api;
import com.gc.android_helper.bean.Banner;
import com.gc.android_helper.core.ImageLoader;

import java.util.List;

public class BannerViewAdapter extends PagerAdapter{

	private List<Banner> bannerList;
	private Context context;
	private Api api;
	private ImageLoader imageLoader;
	public BannerViewAdapter(List<Banner> bannerList, Context context){
		this.bannerList = bannerList;
		this.context = context;
		api = Api.getInstance();
		imageLoader = api.getImageLoader();
	}
	@Override
	public int getCount() {
		return bannerList.size();
	}
	
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view==obj;
	}

	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imgView = new ImageView(container.getContext());
		imgView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageLoader.displayImage(bannerList.get(position).getImagesUrl(),imgView);
		//AndroidUtil.loadCacheImg(context,imgView,imgsUrl.get(position));
		container.addView(imgView);


		return imgView;
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
