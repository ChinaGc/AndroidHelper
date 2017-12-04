package com.example.guocan.test;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.guocan.test.pager.DownPager;
import com.example.guocan.test.pager.FirstPager;
import com.example.guocan.test.pager.UItraPtrPager;
import com.gc.android_helper.core.Api;
import com.gc.android_helper.core.BaseActivity;

public class MainActivity extends BaseActivity {

    private ViewPager tab_vp;

    private TabLayout tab_layout;

    private FirstPager fis1;

    private UItraPtrPager fis2;

    private DownPager fis3;

    private DownPager fis4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab_vp = (ViewPager) this.findViewById(R.id.tab_vp);
        tab_layout = (TabLayout) this.findViewById(R.id.tab_layout);
        tab_vp.setAdapter(new TestPagerAdapter());
        tab_vp.setOffscreenPageLimit(4);
        tab_layout.setupWithViewPager(tab_vp);
        fis1 = new FirstPager(MainActivity.this);
        fis2 = new UItraPtrPager(MainActivity.this);
        fis3 = new DownPager(MainActivity.this);
        fis4 = new DownPager(MainActivity.this);
        fis1.show("");
        tab_vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                System.out.println("onPageSelected第" + position + "页");
                switch (position) {
                case 0:
                    fis1.show(null);
                    break;
                case 1:
                    fis2.show(null);
                    break;
                case 2:
                    fis3.show("http://dl.360safe.com/360/inst.exe");
                    break;
                case 3:
                    fis4.show("http://sw.bos.baidu.com/sw-search-sp/software/a7cdbf39007de/QQ_8.9.1.20437_setup.exe");
                    break;

                }
            }
        });

    }

    private class TestPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("instantiateItem第" + position + "页");
            // 初始化数据
            View view = null;
            switch (position) {
            case 0:
                view = fis1.getView();
                break;
            case 1:
                view = fis2.getView();
                break;
            case 2:
                view = fis3.getView();
                break;
            case 3:
                view = fis4.getView();
                break;
            case 4:

            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "第" + position + "页";
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Api.getInstance().toast("返回");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        api.exitApp();
    }
}
