package com.gc.android_helper.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.gc.android_helper.core.Api;
import com.gc.android_helper.core.BasePager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gc on 2017/6/7/007.
 */

public class ImageUtil {
    private Context context;
    public ImageUtil(Context context){
        this.context = context;
    }
    /**
     * 读取图片的旋转的角度
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 从Uri获取路径
     * @param contentUri
     * @return 绝对路径
     */
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String res = null;
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if(cursor.moveToFirst()){;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将图片按照某个角度进行旋转
     * @param bm 需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            //将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 修正图片旋转角度问题
     * @param filePath
     * @return 修正后图片的绝对路径
     */
    public String  fixDedegreeImage(BasePager pager,String filePath){
        Api.getInstance().toast("正在修正...");
        int degree  = getBitmapDegree(filePath);
        if(degree==0){
            return filePath;
        }
        String destpath = Environment.getExternalStorageDirectory()+"/"+context.getPackageName()+"/fix";
        Bitmap imgBit = rotateBitmapByDegree(BitmapFactory.decodeFile(filePath),degree);
        return saveImage(imgBit,destpath);
    }


    /**
     * 保存bitmap到本地文件 名称为当前毫秒数
     * @param bmp
     * @param destPath 保存路径
     * @return 保存后图片的绝对路径
     */
    public String saveImage(Bitmap bmp,String destPath) {
        File dir = new File(destPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir,System.currentTimeMillis()+".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);//图片质量
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}
