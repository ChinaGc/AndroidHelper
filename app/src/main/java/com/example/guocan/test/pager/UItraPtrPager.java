package com.example.guocan.test.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.guocan.test.DragLayoutActivity;
import com.example.guocan.test.R;
import com.gc.android_helper.core.Api;
import com.gc.android_helper.core.BaseListViewAdapter;
import com.gc.android_helper.core.BasePager;
import com.gc.android_helper.core.LoadMoreHolder;
import com.gc.android_helper.bean.Banner;
import com.gc.android_helper.util.LengthUtil;
import com.gc.android_helper.view.customer.AutoScrollViewPager;
import com.gc.android_helper.view.customer.BannerView;
import com.gc.android_helper.view.customer.MPtrClassicFrameLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.TestAdapter;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class UItraPtrPager extends BasePager<String> {
    private View view;

    private ListView refresh_listview;

    private BannerView bannerView;

    private List<Banner> bannerList = new ArrayList<Banner>();

    private List<String> data = new ArrayList<>();

    private TestAdapter testAdapter;

    private MPtrClassicFrameLayout ptrFrame;

    public UItraPtrPager(Context context) {
        super(context);
    }

    @Override
    protected View createView() {
        view = View.inflate(context, R.layout.uitra_ptr_test, null);
        bannerView = new BannerView(context);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LengthUtil.dip2px(context, 240));
        bannerView.setLayoutParams(params);
        refresh_listview = (ListView) view.findViewById(R.id.refresh_listview);
        ptrFrame = (MPtrClassicFrameLayout) view.findViewById(R.id.ultra_ptr_frame);
        ptrFrame.setLastUpdateTimeRelateObject(this);
        // 下拉刷新的阻力，下拉时，下拉距离和显示头部的距离比例，值越大，则越不容易滑动
        // ptrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        // ptrFrame.setDurationToClose(200);//返回到刷新的位置（暂未找到）
        // ptrFrame.setDurationToCloseHeader(1000);//关闭头部的时间 // default is false
        // ptrFrame.setPullToRefresh(false);//当下拉到一定距离时，自动刷新（true），显示释放以刷新（false）
        // ptrFrame.setKeepHeaderWhenRefresh(true);//见名只意

        // ptrFrame.setHeaderView(header);//自定义头部View
        // ptrFrame.addPtrUIHandler(header);//头部状态监听View
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                // 数据刷新的回调
                ptrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrame.refreshComplete();
                    }
                }, 1500);
            }
        });
        refresh_listview.addHeaderView(bannerView);
        bannerView.setOnBannerViewItemClickListener(new AutoScrollViewPager.OnPageClickListener() {
            @Override
            public void onPageClick(AutoScrollViewPager pager, int position) {
                // Api.getInstance().toast(Api.getInstance().getJSONCache("gc"));
                Intent intent = new Intent((Activity) context, DragLayoutActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    protected void bindData(String a) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                Activity ac = (Activity) context;
                Api.getInstance().jsonCache("gc", "哈哈哈哈哈");
                ac.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bannerList.add(new Banner("http://photocdn.sohu.com/20170317/Img483713293.jpeg", "美14岁少年捡到7.44克拉钻石"));
                        bannerList.add(new Banner("http://img1.gtimg.com/news/pics/hv1/197/225/2194/142722422.jpg", "叙军方称4架以色"));
                        bannerList.add(new Banner("http://img.jiankang.com/temp/2017/03/17/14897156569301.png", "2米1农村傻大个成"));
                        bannerList.add(new Banner("http://i3.hexun.com/2017-03-17/188530586.jpg", "新一代CPU不再支持Win10以下系统升"));
                        bannerList.add(new Banner("http://www.sd.xinhuanet.com/news/yule/2017-03/17/1120641924_14896565682171n.jpg", "爱情公寓5终于开拍：故事还在继续"));
                        for (int i = 0; i < 30; i++) {
                            data.add("hellow" + i);
                        }
                        processData();
                        onLoadingComplete(BasePager.LOADING_SUCCESS);
                    }
                });
            }
        }).start();
    }

    private void processData() {
        bannerView.setBannerViewData(bannerList);
        if (testAdapter == null) {
            testAdapter = new TestAdapter(data);
            refresh_listview.setAdapter(testAdapter);
            testAdapter.setOnLoadMoreListener(new BaseListViewAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore(final LoadMoreHolder holder) {
                    api.execute(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(3000);
                            Activity ac = (Activity) context;
                            ac.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // testAdapter.notifyDataSetChanged();
                                    holder.bindData(LoadMoreHolder.FAILED, 0);
                                }
                            });
                        }
                    });
                }
            });
        } else {
            testAdapter.notifyDataSetChanged();
        }
    }

}
