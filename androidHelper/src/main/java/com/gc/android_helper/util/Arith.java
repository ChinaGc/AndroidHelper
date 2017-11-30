package com.gc.android_helper.util;

import java.math.BigDecimal;

/**
 * Created by guocan on 2017/3/7.
 */

public class Arith {
    /**
     * 39      * 提供精确的除法运算方法div
     * 40      * @param value1 被除数
     * 41      * @param value2 除数
     * 42      * @param scale 精确范围
     * 43      * @return 两个参数的商
     * 44      * @throws IllegalAccessException
     * 45
     */
    public static double div(double value1, double value2, int scale){
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale).doubleValue();
    }
}
