package com.gc.android_helper.util;

import com.gc.android_helper.app.Api;
import com.guocan.file.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 郭灿 on 2017/3/29.
 */

public class DownLoadMannger {
    public static final int NONE=0;//默认状态
    public static final int DOWNLOADING = 1;//下载中
    public static final int WAITING = 4;//等待中
    public static final int SUCCESS = 2;//成功
    public static final int FAILED = 3;//失败
    private DownLoadMannger(){}
    private mDownLoadCallBack callBack;
    public interface DownLoadObserver{
        public void notifyDownLoadstate(int state);
        public void notifyDownLoadProgress(int progress);
    }
    private Map<String,List<DownLoadObserver>> observers = new HashMap<String,List<DownLoadObserver>>(); //观察者集合
    private static DownLoadMannger mannger = new DownLoadMannger();
    public static DownLoadMannger getDownLoadMannger(){
        return mannger;
    }
    public void registObserver(String url,DownLoadObserver downLoadObserver){
        List<DownLoadObserver> observerList = observers.get(url);
        if(observerList==null){
            observerList = new ArrayList<DownLoadObserver>();
           // observerList.add(downLoadObserver);
            observers.put(url,observerList);
        }
        observerList.add(downLoadObserver);
    }

    public void downLoad(final String url, final String destPath){
        notifyDownLoadstate(DownLoadMannger.WAITING,url);
        Api.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                HttpClient.downLoanFile(url,destPath,new mDownLoadCallBack(url));
            }
        });
    }
    private void notifyDownLoadProgress(int progress,String url){
        for(DownLoadObserver observer:observers.get(url)){
            observer.notifyDownLoadProgress(progress);
        }
    }
    private void notifyDownLoadstate(int state,String url){
        for(DownLoadObserver observer:observers.get(url)){
            observer.notifyDownLoadstate(state);
        }
    }
    private class mDownLoadCallBack implements HttpClient.DownLoadCallBack{
        private String url;
        public mDownLoadCallBack(String url){
            this.url = url;
        }
        @Override
        public void onStart() {
            notifyDownLoadstate(DownLoadMannger.DOWNLOADING,url);
        }
        @Override
        public void onDownLoading(int i) {
            notifyDownLoadProgress(i,url);
        }

        @Override
        public void onSuccess() {
            notifyDownLoadstate(DownLoadMannger.SUCCESS,url);
        }
    }
}
