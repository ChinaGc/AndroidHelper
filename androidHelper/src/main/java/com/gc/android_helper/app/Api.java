package com.gc.android_helper.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.gc.android_helper.bean.ActionSheetParams;
import com.gc.android_helper.bean.AlertDialogParams;
import com.gc.android_helper.bean.ConfirmDialogParams;
import com.gc.android_helper.bean.ProgressDialogParams;
import com.gc.android_helper.dialog.IosAlertDialog;
import com.gc.android_helper.dialog.IosConfirmDialog;
import com.gc.android_helper.listener.OnPermissionsResultListener;
import com.gc.android_helper.dialog.ActionSheet;
import com.gc.android_helper.util.DownLoadMannger;
import com.gc.android_helper.util.ImageLoader;
import com.gc.android_helper.util.ImageUtil;
import com.gc.android_helper.view.picker.Picker;
import com.gc.android_helper.view.picker.PickerParams;
import com.guocan.file.FileHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * api核心类
 * 
 * @author guocan
 *
 */
@SuppressLint("ResourceAsColor")
public class Api {

    private static Api i = null;

    /**
     * UI
     */
    private ActionSheet actionSheet;

    private Map<Activity, ActionSheet> actionSheetActivityMap = new HashMap<Activity, ActionSheet>();

    private IosConfirmDialog confirmDialog;

    private Map<Activity, IosConfirmDialog> confirmDialogActivityMap = new HashMap<Activity, IosConfirmDialog>();

    private IosAlertDialog alertDialog;

    private Map<Activity, IosAlertDialog> alertDialogActivityMap = new HashMap<Activity, IosAlertDialog>();

    private Picker picker;

    private Map<Activity, Picker> pickerMActivityMap = new HashMap<Activity, Picker>();

    private AsyncHttpClient asyncHttpClient;

    private ImageUtil imageUtil;

    private ExecutorService fixedThreadPool;// 线程池

    private ImageLoader imageLoader;

    private Api() {
        imageUtil = new ImageUtil(getContext());
        fixedThreadPool = Executors.newFixedThreadPool(5);
        imageLoader = getImageLoader();
        asyncHttpClient = new AsyncHttpClient();
    }

    public Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * 初始化一次即可
     * 
     * @param
     */
    public static Api getInstance() {
        if (i == null) {
            i = new Api();
        }
        return i;
    }

    public DownLoadMannger getDownLoadMannger() {
        return DownLoadMannger.getDownLoadMannger();
    }

    /**
     * 文件下载
     * 
     * @param url
     * @param destPath
     */
    public void downLoad(String url, String destPath) {
        DownLoadMannger.getDownLoadMannger().downLoad(url, destPath);
    }

    /**
     * 从线程池中获取线程执行异步任务
     * 
     * @param runnable
     */
    public void execute(Runnable runnable) {
        fixedThreadPool.execute(runnable);
    }

    public void actionSheet(Activity activity, ActionSheetParams actionSheetParams, ActionSheet.ActionSheetClickListener actionSheetClickListener) {
        Log.i("API", "actionSheetActivityMap.size = " + actionSheetActivityMap.size());
        if (actionSheetActivityMap.get(activity) != null) {
            actionSheet = actionSheetActivityMap.get(activity);
        } else {
            actionSheet = new ActionSheet(activity);
            actionSheetActivityMap.put(activity, actionSheet);
        }
        actionSheet.actionSheet(actionSheetParams, actionSheetClickListener);
    }

    public void comfirm(Activity activity, ConfirmDialogParams confirmDialogParams) {
        Log.i("API", "confirmDialogActivityMap.size = " + confirmDialogActivityMap.size());
        if (confirmDialogParams == null) {
            confirmDialogParams = new ConfirmDialogParams();
        }
        if (confirmDialogActivityMap.get(activity) != null) {
            confirmDialog = confirmDialogActivityMap.get(activity);
        } else {
            confirmDialog = new IosConfirmDialog(activity).builder();
            confirmDialogActivityMap.put(activity, confirmDialog);
        }
        confirmDialog.setTitle(confirmDialogParams.getTitle());
        confirmDialog.setMsg(confirmDialogParams.getMsg());
        confirmDialog.setNegativeButton(confirmDialogParams.getLeftBtnText(), confirmDialogParams.getLeftOnClickListener());
        confirmDialog.setPositiveButton(confirmDialogParams.getRightBtnText(), confirmDialogParams.getRightOnClickListener());
        confirmDialog.show();
    }

    public void alert(Activity activity, AlertDialogParams alertDialogParams) {
        if (alertDialogParams == null) {
            alertDialogParams = new AlertDialogParams();
        }
        Log.i("API", "alertDialogActivityMap.size = " + alertDialogActivityMap.size());
        if (alertDialogActivityMap.get(activity) != null) {
            alertDialog = alertDialogActivityMap.get(activity);
        } else {
            alertDialog = new IosAlertDialog(activity).builder();
            alertDialogActivityMap.put(activity, alertDialog);
        }
        alertDialog.setTitle(alertDialogParams.getTitle());
        alertDialog.setMsg(alertDialogParams.getMsg());
        alertDialog.setButton(alertDialogParams.getBtnText(), alertDialogParams.getBtnnClickListener());
        alertDialog.show();
    }

    public void picker(Activity activity, PickerParams pickerParams) {
        Log.i("API", "PickerActivityMap.size = " + pickerMActivityMap.size());
        if (pickerMActivityMap.get(activity) != null) {
            picker = pickerMActivityMap.get(activity);
        } else {
            picker = new Picker(activity);
            pickerMActivityMap.put(activity, picker);
        }
        picker.picker(pickerParams);
    }

    /**
     * toast
     * 
     * @param msg
     */
    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(int stringId) {
        Toast.makeText(getContext(), stringId, Toast.LENGTH_SHORT).show();
    }

    /**
     * http get请求
     * 
     * @param url
     * @param requestParams
     * @param handlerInterface
     */
    public void httpGet(String url, RequestParams requestParams, ResponseHandlerInterface handlerInterface) {
        asyncHttpClient.get(url, requestParams, handlerInterface);
    }

    /**
     * http post请求
     * 
     * @param url
     * @param requestParams
     * @param handlerInterface
     */
    public void httpPost(String url, RequestParams requestParams, ResponseHandlerInterface handlerInterface) {
        asyncHttpClient.post(url, requestParams, handlerInterface);
    }

    /**
     * https双向加密连接器
     * 
     * @param sslSocketFactory
     */
    public void setSSl(SSLSocketFactory sslSocketFactory) {
        asyncHttpClient.setSSLSocketFactory(sslSocketFactory);
    }

	/**
	 * @param fileName
	 * @param json
	 */
	public void jsonCache(String fileName, String json) {
        File dir = new File(getContext().getCacheDir().getPath() + File.separator + "JsonCache");
        if (!dir.exists())
            dir.mkdir();
        File josnFile = new File(dir, fileName);
        FileHelper.writeText(json, josnFile, "UTF-8");
    }

    /**
     * 从内部存储cache目录下读取JSON
     * 
     * @param fileName
     * @return
     */
    public String getJsonCache(String fileName) {
        File josnFile = new File(getContext().getCacheDir() + File.separator + "JsonCache", fileName);
        return FileHelper.readBigText(josnFile, "UTF-8");
    }

    /**
     * 获取新实例
     * 
     * @return
     */
    public ImageLoader getImageLoader() {
        return new ImageLoader(getContext());
    }

    /**
     * 获取单实例
     * 
     * @return
     */

    public ImageLoader getSingleImageLoader() {
        return imageLoader;
    }

    /**
     * @param pager
     *            页面对象
     * @param requestCode
     *            请求标志
     */
    public void getPicFromAlbum(BasePager pager, int requestCode) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 请求码
        pager.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity
     * @param requestCode
     *            请求标志
     */
    public void getPicFromAlbum(Activity activity, int requestCode) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 请求码
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param pager
     *            页面对象
     * @param requestCode
     *            请求标志
     * @return 照片绝对路径
     */
    public String getPicFromCamera(BasePager pager, int requestCode) {
        if (hasSdcard()) {
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + pager.getActivity().getPackageName() + File.separator + "pic" + File.separator + System.currentTimeMillis() + ".jpg";
            if (hasPermission(pager.getActivity(), Manifest.permission.CAMERA, Permissions.CAMERA)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri mPhotoUri = getUriForFile(filePath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                pager.startActivityForResult(intent, requestCode);
            } else {
                pager.getActivity().setOnPermissionsResultListener(new OnPermissionsResultListener() {
                    @Override
                    public void onResult(int grantResult) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {// 用户同意获取权限
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri mPhotoUri = getUriForFile(filePath);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                            pager.startActivityForResult(intent, requestCode);
                        } else {
                            pager.userRejectPermissionRequest(requestCode);
                        }
                    }
                });
            }
            return filePath;
        } else {
            i.toast("外部存储不可用");
            return null;
        }
    }

    /**
     * 判断是否具有权限 若无则进行申请
     * @param activity
     * @param permission
     * @return
     */
    public boolean hasPermission(Activity activity, String permission, int requestCode) {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, permission);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
            return false;
        }
        return true;
    }

    /**
     * @param activity
     * @param requestCode
     *            请求标志
     * @return 照片绝对路径
     */
    public String getPicFromCamera(BaseActivity activity, int requestCode) {
        if (hasSdcard()) {
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + activity.getPackageName() + File.separator + "pic" + File.separator + System.currentTimeMillis() + ".jpg";
            if (hasPermission(activity, Manifest.permission.CAMERA, Permissions.CAMERA)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri mPhotoUri = getUriForFile(filePath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                activity.startActivityForResult(intent, requestCode);
            } else {
                activity.setOnPermissionsResultListener(new OnPermissionsResultListener() {
                    @Override
                    public void onResult(int grantResult) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {// 用户同意获取权限
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri mPhotoUri = getUriForFile(filePath);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                            activity.startActivityForResult(intent, requestCode);
                        } else {
                            // 用户不同意获取权限
                            activity.userRejectPermissionRequest(requestCode);
                        }
                    }
                });
            }
            return filePath;
        } else {
            i.toast("外部存储不可用");
            return null;
        }
    }

    /**
     * 安卓N+,file-->Uri
     * 
     * @param file
     * @return
     */
    public Uri getUriForFile(File file) {
        if (file.isFile()) {// 文件需要确定文件路径，不包含文件
            String fileDir = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/"));
            File dir = new File(fileDir);
            if (!dir.exists())
                dir.mkdirs();// 创建路径
        } else {// 本身就是路径
            if (!file.exists())
                file.mkdirs();// 创建路径
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".Fileprovider", file);
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw new RuntimeException("application must be provide a FileProvider and Xml for FileProvider.getUriForFile()");
            }
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 安卓N+,file-->Uri
     * 
     * @param filePath
     * @return
     */
    public Uri getUriForFile(String filePath) {
        File file = new File(filePath);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        if (fileName.contains(".")) {// 文件需要确定文件路径，不包含文件
            String fileDir = filePath.substring(0, filePath.lastIndexOf("/"));
            File dir = new File(fileDir);
            if (!dir.exists())
                dir.mkdirs();// 创建路径
        } else {// 本身就是路径
            if (!file.exists())
                file.mkdirs();// 创建路径
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".Fileprovider", file);
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw new RuntimeException("application must be provide a FileProvider and Xml for FileProvider.getUriForFile()");
            }
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 外部存储是否可用
     * 
     * @return
     */
    public boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public ImageUtil getImageUtil() {
        return imageUtil;
    }

    /**
     * 销毁
     */
    public void destory() {
        i = null;
        BaseApplication application = (BaseApplication) getContext();
        application.clearAllActivity();
        this.confirmDialogActivityMap.clear();
        this.actionSheetActivityMap.clear();
        this.alertDialogActivityMap.clear();
    }

    public void exitApp() {
        destory();
        System.exit(0);
    }

}
