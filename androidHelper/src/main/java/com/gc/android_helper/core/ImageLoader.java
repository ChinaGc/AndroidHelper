package com.gc.android_helper.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.guocan.file.FileHelper;
import com.guocan.file.HttpClient;
import com.guocan.file.MD5;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.guocan.file.MD5.md5;

/**
 * 图片加载服务
 * 一个Pager建议使用一个ImaeLoader
 * Created by guocan on 2017/3/9.
 */

public class ImageLoader {
    private Map<Integer, ImageView> cacheMaps;//显示图片的ImageView集合
    private LruCache<String, Bitmap> bitMaps;//维护了LRU算法的内存集合
    private File cacheDir;
    private ExecutorService fixedThreadPool;//线程池

    private final int SUCCESS = 1;
    private final int FAIL = 0;
    private Handler handler;
    private int defaultResId;//默认图片
    private int failedResId;//加载失败时加载的图片
    private boolean isCacheLru = true;//是否缓存到内存
    private boolean isCacheDisk = true;//是否缓存到内部存储
    public ImageLoader(Context context) {
        this.cacheDir = new File(context.getCacheDir().getPath() + File.separator + "imageCache");
        if (!cacheDir.exists()) cacheDir.mkdir();
        this.cacheMaps = new HashMap<Integer, ImageView>();
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);//内存1/8用缓存图片

        bitMaps = new LruCache<String, Bitmap>(maxSize) {
            //返回每存储一张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        fixedThreadPool = Executors.newFixedThreadPool(5);
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SUCCESS:
                        Log.i("ImageLoader","网络中获取图片");
                        cacheMaps.get(msg.arg1).setImageBitmap((Bitmap) msg.obj);
                        break;
                    case FAIL:
                        Log.i("ImageLoader", "图片加载失败");
                        if (failedResId != 0) {
                            cacheMaps.get(msg.arg1).setImageResource(failedResId);
                        }
                        break;
                }
            }
        };
    }

    public void displayImage(String url, ImageView imageView) {
        if (defaultResId != 0) {
            imageView.setImageResource(defaultResId);
        }
        //内存中获取图片
        Bitmap bitmap = this.bitMaps.get(url);//以图片地址作为key存储图片在内存中
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.i("ImageLoader","内存中获取图片");
            return;
        }
        //文件中获取图片
        if (!setImageFromLocal(url, imageView)) {
            //网络中获取图片
            setImageFromNet(url, imageView);
        }
    }

    private boolean setImageFromLocal(String url, ImageView imageView) {
        if (isCacheDisk) {//文件缓存
            String fileName = md5(url).substring(0, 10);//文件名
            File file = new File(cacheDir, fileName);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                //缓存进内存中
                if (isCacheLru) {
                    this.bitMaps.put(url, bitmap);
                }
                Log.i("ImageLoader", "文件中获取图片");
                return true;
            }
            return false;
        }
        return false;
    }

    private void setImageFromNet(final String url, final ImageView imageView) {
        int key = 0;
        if (imageView.getTag() != null) {
            key = (int) imageView.getTag();
        } else {
            key = getRandom6();
            imageView.setTag(key);
        }
        if (!cacheMaps.containsValue(imageView)) {
            this.cacheMaps.put(key, imageView);
        }
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] data = HttpClient.readBytefromUrl(url);
                    Message msg = new Message();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    msg.obj = bitmap;
                    msg.arg1 = (int) imageView.getTag();//imageViewId
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                    //缓存到内存中
                    if (isCacheLru) {
                        bitMaps.put(url, bitmap);
                    }
                    //写入文件中
                    if (isCacheDisk) {
                        String fileName = MD5.md5(url).substring(0, 10);//文件名
                        File file = new File(cacheDir, fileName);
                        FileHelper.writeByteToFile(data, file);
                    }
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = FAIL;
                    msg.arg1 = (int) imageView.getTag();//imageViewId
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        };
        fixedThreadPool.execute(task);
    }

    public void setDefaultResId(int defaultResId) {
        this.defaultResId = defaultResId;
    }

    public void setFailedResId(int failedResId) {
        this.failedResId = failedResId;
    }

    public void setCacheLru(boolean cacheLru) {
        isCacheLru = cacheLru;
    }

    public void setCacheDisk(boolean cacheDisk) {
        isCacheDisk = cacheDisk;
    }

    /**
     * 获取不重复整数
     *
     * @return
     */
    private int getRandom6() {
        int returnNum = 0;
        //自定义有序数
        int[] ra = new int[9];
        for (int i = 0; i < 9; i++) {
            ra[i] = i + 1;
        }
        //无序排列并取值
        for (int i = 0; i < 6; i++) {
            returnNum *= 10;
            Random rd = new Random();
            int temp1 = rd.nextInt(9 - i);
            int temp2 = ra[8 - i];//保存相对末尾的数据
            ra[8 - i] = ra[temp1];//交换
            ra[temp1] = temp2;
            returnNum += ra[8 - i];//取值
        }
        return returnNum;
    }
}
