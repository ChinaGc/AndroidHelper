package com.gc.android_helper.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 郭灿文件下载服务
 */

public class DownLoadMannger {
    public static final int NONE = 0;// 默认状态

    public static final int DOWNLOADING = 1;// 下载中

    public static final int WAITING = 4;// 等待中

    public static final int SUCCESS = 2;// 成功

    public static final int FAILED = 3;// 失败

    private DownLoadMannger() {
    }

    public interface DownLoadObserver {
        public void notifyDownLoadstate(int state);

        public void notifyDownLoadProgress(int progress);
    }

    // 观察者集合
    private Map<String, List<DownLoadObserver>> observers = new HashMap<String, List<DownLoadObserver>>();

    private static DownLoadMannger mannger = null;

    // 单例
    public static DownLoadMannger getDownLoadMannger() {
        if (mannger == null) {
            mannger = new DownLoadMannger();
        }
        return mannger;
    }

    public void registObserver(String url, DownLoadObserver downLoadObserver) {
        List<DownLoadObserver> observerList = observers.get(url);
        if (observerList == null) {
            observerList = new ArrayList<DownLoadObserver>();
            observers.put(url, observerList);
        }
        observerList.add(downLoadObserver);
    }

    public void downLoad(final String url, final String destPath) {
        notifyDownLoadstate(DownLoadMannger.WAITING, url);
        Api.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                // TODO 下载操作
            }
        });
    }

    private void notifyDownLoadProgress(int progress, String url) {
        for (DownLoadObserver observer : observers.get(url)) {
            observer.notifyDownLoadProgress(progress);
        }
    }

    private void notifyDownLoadstate(int state, String url) {
        for (DownLoadObserver observer : observers.get(url)) {
            observer.notifyDownLoadstate(state);
        }
    }

}
