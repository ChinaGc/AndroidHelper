package com.gc.android_helper.core;

import android.Manifest;
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
import android.widget.Toast;

import com.gc.android_helper.listener.OnPermissionsResultListener;
import com.gc.android_helper.dialog.ActionSheet;
import com.gc.android_helper.util.ImageUtil;
import com.gc.android_helper.view.picker.Picker;
import com.gc.android_helper.view.picker.PickerParams;
import com.guocan.file.FileHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * api核心类
 * 
 * @author 郭灿
 *
 */
public class Api {

    private static Api i = null;

    private ActionSheet actionSheet;

    private Picker picker;

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

    public void actionSheet() {

    }

    public void comfirm(Activity activity) {

    }

    public void alert(Activity activity) {

    }

    public void picker(Activity activity, PickerParams pickerParams) {
//        if (pickerMActivityMap.get(activity) != null) {
//            picker = pickerMActivityMap.get(activity);
//        } else {
//            picker = new Picker(activity);
//            pickerMActivityMap.put(activity, picker);
//        }
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
    }

    public void exitApp() {
        destory();
        System.exit(0);
    }
}
