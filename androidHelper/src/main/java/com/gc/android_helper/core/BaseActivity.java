package com.gc.android_helper.core;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.gc.android_helper.listener.OnPermissionsResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gc on 2017/5/4/004.
 */

public abstract class BaseActivity extends MSwipeBackActivity {
    private List<BasePager> pagerList;

    protected final String TAG = this.getClass().getSimpleName();

    protected Api api;

    private OnPermissionsResultListener onPermissionsResultListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getApplication() instanceof BaseApplication)) {
            throw new RuntimeException("Application must be extends BaseApplication");
        }
        api = Api.getInstance();
        BaseApplication application = (BaseApplication) getApplication();
        application.addActivity(this);
        pagerList = new ArrayList<>();
        // 权限请求
        String[] permissions = ((BaseApplication) getApplication()).getPermissions();
        List<String> requestPermission = new ArrayList<>();
        for (String permission : permissions) {
            // 权限判断
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, permission);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermission.add(permission);
            }
        }
        // 权限申请
        if (requestPermission.size() > 0) {
            ActivityCompat.requestPermissions(this, requestPermission.toArray(new String[requestPermission.size()]), Permissions.CODE_FOR_ALLPERMISSION);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication application = (BaseApplication) getApplication();
        application.removeActivity(this);
        pagerList.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (BasePager pager : pagerList) {
            pager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 向Activity中注册Pager
     * 
     * @param pager
     */
    public void registPager(BasePager pager) {
        if (!pagerList.contains(pager)) {
            pagerList.add(pager);
        }
    }

    /**
     * 权限询问callback
     * 
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "requestCode=" + requestCode);
        if (onPermissionsResultListener != null) {
            onPermissionsResultListener.onResult(grantResults[0]);
        }
        // 这里不做判断处理 全部由回调去自己实现
        /*
         * switch (requestCode){//请求开启相机 case Permissions.CAMERA:
         * if(grantResults[0]!= PackageManager.PERMISSION_GRANTED){//用户不同意
         * confirmDialogParams.setMsg("启动相机要赋予访问相机权限，不开启将无法工作！");
         * confirmDialogParams.setRightBtnText("去设置");
         * confirmDialogParams.setRightOnClickListener(new
         * View.OnClickListener() {
         * 
         * @Override public void onClick(View v) { Intent intent = new Intent();
         * intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS); Uri
         * uri = Uri.fromParts("package", getPackageName(), null);
         * intent.setData(uri); startActivity(intent); } });
         * api.comfirm(this,confirmDialogParams); } break; }
         */
        // PackageManager.PERMISSION_GRANTED
    }

    /**
     * 用户权限拒绝或者并且不在显示callback
     * 
     * @param permission
     * @return
     */
    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    public void setOnPermissionsResultListener(OnPermissionsResultListener onPermissionsResultListener) {
        this.onPermissionsResultListener = onPermissionsResultListener;
    }

    /**
     * 权限被拒绝回调
     * 
     * @param requestCode
     */
    protected void userRejectPermissionRequest(int requestCode) {
        api.toast("permission has been reject : requestCode=" + requestCode);
    }
}
