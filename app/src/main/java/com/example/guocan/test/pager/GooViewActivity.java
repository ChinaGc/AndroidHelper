package com.example.guocan.test.pager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gc.android_helper.view.customer.GooView;

public class GooViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GooView(this));
    }

}
