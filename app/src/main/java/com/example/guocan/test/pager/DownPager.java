package com.example.guocan.test.pager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gc.android_helper.app.BasePager;
import com.gc.android_helper.core.DownLoadMannger;

/**
 * Created by 郭灿 on 2017/3/29.
 */

public class DownPager extends BasePager<String> implements View.OnClickListener,DownLoadMannger.DownLoadObserver{
    private Button button;
    @Override
    public void notifyDownLoadstate(int state) {
        Activity ac = (Activity) context;
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(true);
            }
        });
    }

    @Override
    public void notifyDownLoadProgress(final int progress) {
        Log.i("DownPager","progress="+progress);
        Activity ac = (Activity) context;
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setText("progress="+progress+"%");
            }
        });
    }

    public DownPager(Context context) {
        super(context);
    }
    /**
     * 创建成功的View
     *
     * @return
     */
    @Override
    protected View createView() {
        button = new Button(context);
        button.setText("下载");
        button.setOnClickListener(this);
        return button;
    }
    private String url;

    /**
     * 为成页面添加数据
     */
    @Override
    protected void bindData(String url){
        DownLoadMannger.getDownLoadMannger().registObserver(url,this);
        this.url = url;
        onLoadingComplete(BasePager.LOADING_SUCCESS);
    }

    @Override
    public void onClick(View v) {
        button.setEnabled(false);
        api.downLoad(url,context.getExternalCacheDir().getPath());
    }
}
