package com.example.guocan.test.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.guocan.test.DragLayoutActivity;
import com.example.guocan.test.R;
import com.gc.android_helper.core.Api;
import com.gc.android_helper.core.BaseListViewAdapter;
import com.gc.android_helper.core.BasePager;
import com.gc.android_helper.core.LoadMoreHolder;
import com.gc.android_helper.bean.Banner;
import com.gc.android_helper.core.CropHelper;
import com.gc.android_helper.dialog.ActionSheet;
import com.gc.android_helper.dialog.DialogManager;
import com.gc.android_helper.dialog.PickerDialog;
import com.gc.android_helper.util.LengthUtil;
import com.gc.android_helper.view.customer.AutoScrollViewPager;
import com.gc.android_helper.view.customer.BannerView;
import com.gc.android_helper.view.customer.VpSwipeRefreshLayout;

import com.gc.android_helper.view.picker.PickerParams;
import com.gc.android_helper.view.picker.entity.City;
import com.gc.android_helper.view.picker.entity.Province;
import com.soundcloud.android.crop.Crop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import adapter.TestAdapter;

public class FirstPager extends BasePager<String> {
    private static final int PHOTO_REQUEST_GALLERY = 1;

    private static final int PHOTO_REQUEST_CAMEREA = 2;

    private View view;

    private ListView refresh_listview;

    private BannerView bannerView;

    private VpSwipeRefreshLayout swiperefreshlayout;

    private List<Banner> bannerList = new ArrayList<Banner>();

    private List<String> data = new ArrayList<>();

    private TestAdapter testAdapter;

    private List<Province> pickerItems;

    private String imagePath;

    private Uri imgUri;

    public FirstPager(Context context) {
        super(context);
    }

    @Override
    protected View createView() {
        view = View.inflate(context, R.layout.refresh_listview_test, null);
        bannerView = new BannerView(context);
        final AbsListView.LayoutParams params = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LengthUtil.dip2px(context, 240));
        bannerView.setLayoutParams(params);
        refresh_listview = (ListView) view.findViewById(R.id.refresh_listview);
        swiperefreshlayout = (VpSwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        swiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bannerList.add(new Banner("http://photocdn.sohu.com/20170317/Img483713293.jpeg", "美14岁少年捡到7.44克拉钻石 命名“超人之钻”"));
                        processData();
                        swiperefreshlayout.setRefreshing(false);
                    }
                }, 3000);
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
        String josn = getJson(context, "province.json");
        pickerItems = JSON.parseArray(josn, Province.class);// 省-市-区
        List<List<City>> cityList = new ArrayList<>();
        List<List<List<String>>> areaList = new ArrayList<>();
        for (Province province : pickerItems) {
            cityList.add(province.getDatas());
            List<City> list = province.getDatas();
            List<List<String>> cityListArea = new ArrayList<>();
            for (City city : list) {
                List<String> areaList1 = city.getDatas();
                cityListArea.add(areaList1);
            }
            areaList.add(cityListArea);
        }

        final PickerParams pickerParams = new PickerParams(view, pickerItems, cityList, areaList);
        pickerParams.setStr_Title("城市选择");
        pickerParams.setOption1(11);
        pickerParams.setOptionsSelectListener(new PickerParams.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                api.toast(pickerItems.get(options1).getName() + cityList.get(options1).get(options2).getName() + areaList.get(options1).get(options2).get(options3));
            }
        });
        refresh_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {// actioniSheet
                    ActionSheet.getInstance(getActivity().getWindow()).show(new String[]{"item1", "item2","item3"}, view, new ActionSheet.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            api.toast("点击了"+position);
                        }
                    });
                }
                if (position == 2) {// alert
                    DialogManager.getInstance(getActivity()).alert("hellow","hellow alert!",null);
                }
                if (position == 3) {// confirm
                    DialogManager.getInstance(getActivity()).confirm("hellow", "hellow confirm?", new String[]{"确定", "取消"}, new DialogManager.OnClickListener() {
                        @Override
                        public void onClick(int position) {
                            api.toast(position+"");
                        }
                    });
                }
                if (position == 4) {// pickerview
                    PickerDialog.getInstance(getActivity().getWindow()).show(pickerParams);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 裁剪后目标路径
        String destpath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/crop/" + System.currentTimeMillis() + ".jpg";
        // 裁剪后目标Uri
        Uri destUri = api.getUriForFile(destpath);
        Log.i("FirstPager", "requestCode=" + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
            case 1:// 相册
                Uri uri = data.getData();
                Log.i("FirstPager", "destpath=" + destpath);
                CropHelper.of(uri, destUri).asSquare().start(this);
                break;
            case 2:// 相机
                   // imagePath 图片路径
                String fixPath = api.getImageUtil().fixDedegreeImage(this, imagePath);// 进行图片角度修正
                // 裁剪
                Uri srcUri = api.getUriForFile(fixPath);
                CropHelper.of(srcUri, destUri).asSquare().start(this);
                break;
            case Crop.REQUEST_CROP:
                api.toast("裁剪成功");
                break;
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {// 取消裁剪删除
            api.toast("取消操作");
        }
    }

    public String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    protected void bindData(String a) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
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
                            /*
                             * for (int i = 0; i <20; i++) { data.add("hellow" +
                             * i); }
                             */
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
