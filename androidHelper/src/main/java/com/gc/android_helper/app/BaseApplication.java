package com.gc.android_helper.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guocan on 2017/3/11.
 */

public class BaseApplication extends Application {


    private static BaseApplication application;
    private List<BaseActivity> mActivityList = new ArrayList<BaseActivity>();
    private String [] permissions;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        try {
            //权限收集
            permissions = getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_PERMISSIONS).requestedPermissions;
        }catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Context getContext(){
        return application;
    }
    //提供一个添加activity的方法
    public void addActivity(BaseActivity activity) {
        if (!mActivityList.contains(activity)) {
            mActivityList.add(activity);
        }
    }

    //提供一个移除activity的方法
    public void removeActivity(BaseActivity activity) {
        if (mActivityList.contains(activity)) {
            mActivityList.remove(activity);
        }
    }

    //提供一个清空集合的方法
    public void clearAllActivity() {
        for (int i = 0; i< mActivityList.size(); i++) {
            BaseActivity activity = mActivityList.get(i);
            activity.finish();
        }
        mActivityList.clear();
    }

    public String[] getPermissions() {
        return permissions;
    }
}
