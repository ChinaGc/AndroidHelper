package com.gc.android_helper.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guocan on 2017/3/7.
 */

public class CommonUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String formatTime(Date time){
        return format.format(time);
    }
}
