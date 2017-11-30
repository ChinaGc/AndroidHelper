package com.example.guocan.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gc.android_helper.app.Api;
import com.gc.android_helper.app.BaseActivity;
import com.gc.android_helper.bean.ActionSheetParams;
import com.gc.android_helper.bean.ConfirmDialogParams;
import com.gc.android_helper.dialog.ActionSheet;
import com.gc.android_helper.view.customer.DragLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.TestAdapter;

/**
 * Created by 郭灿 on 2017/4/1.
 */

public class DragLayoutActivity extends BaseActivity {
    private ListView left_listview;

    private ListView main_listview;

    private List<String> data = new ArrayList<>();

    private DragLayout drag_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.drag_layout, null);
        setContentView(view);
        left_listview = (ListView) findViewById(R.id.left_listview);
        main_listview = (ListView) findViewById(R.id.main_listview);
        drag_layout = (DragLayout) findViewById(R.id.drag_layout);
        drag_layout.setmOnDragStateChangeListener(new DragLayout.OnDragStateChangeListener() {
            @Override
            public void open() {
                // Api.getInstance().toast("open");
            }

            @Override
            public void close() {
                // Api.getInstance().toast("close");
            }

            @Override
            public void draging(float percent) {
                Log.i("DragLayout", "percent: " + percent);
            }
        });
        for (int i = 0; i < 30; i++) {
            data.add("hellow" + i);
        }
        TestAdapter testAdapter = new TestAdapter(data);
        left_listview.setAdapter(testAdapter);
        main_listview.setAdapter(testAdapter);

        left_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drag_layout.close(true);
                Api.getInstance().comfirm(DragLayoutActivity.this, new ConfirmDialogParams());
            }
        });
        final ActionSheetParams actionSheetParams = new ActionSheetParams(view);
        actionSheetParams.setTitle("分享到");
        actionSheetParams.setButtions(new String[] { "QQ空间", "朋友圈", "新浪微博" });
        main_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drag_layout.open(true);

                Api.getInstance().actionSheet(DragLayoutActivity.this, actionSheetParams, new ActionSheet.ActionSheetClickListener() {
                    @Override
                    public void onClick(int position) {
                        Api.getInstance().toast("点击了" + position);
                    }
                });
            }
        });
    }
}
