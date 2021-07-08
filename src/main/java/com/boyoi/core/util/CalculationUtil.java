package com.boyoi.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 计算方法工具类
 */
public class CalculationUtil {

    /**
     * Double 加法运算
     *
     * @param m1
     * @param m2
     * @return
     */
    public static Double addDouble(double m1, double m2) {
        BigDecimal p1 = BigDecimal.valueOf(m1);
        BigDecimal p2 = BigDecimal.valueOf(m2);
        return p1.add(p2).doubleValue();
    }

    /**
     * Double 减法运算
     *
     * @param m1
     * @param m2
     * @return
     */
    public static Double subDouble(double m1, double m2) {
        BigDecimal p1 = BigDecimal.valueOf(m1);
        BigDecimal p2 = BigDecimal.valueOf(m2);
        return p1.subtract(p2).doubleValue();
    }

    /**
     * Double 乘法运算
     *
     * @param m1
     * @param m2
     * @return
     */
    public static Double mul(double m1, double m2) {
        DecimalFormat df = new DecimalFormat("######0.00");
        BigDecimal p1 = BigDecimal.valueOf(m1);
        BigDecimal p2 = BigDecimal.valueOf(m2);
        return Double.valueOf(df.format(p1.multiply(p2).doubleValue()));
    }


    /**
     * Double 除法运算
     *
     * @param m1
     * @param m2
     * @param scale
     * @return
     */
    public static Double div(double m1, double m2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Parameter error");
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        BigDecimal p1 = BigDecimal.valueOf(m1);
        BigDecimal p2 = BigDecimal.valueOf(m2);
        return Double.valueOf(df.format(p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).doubleValue()));
    }

    /**
     * 比较大小
     *
     * @param val1
     * @param val2
     * @return
     */
    public static int compareWeight(BigDecimal val1, BigDecimal val2) {
        int result = -1;
        if (val1.compareTo(val2) <= 0) {
            result = 1;
//            result = "第二位数大！";
        }
//        if (val1.compareTo(val2) == 0) {
//            result = 0;
////            result = "两位数一样大！";
//        }
        if (val1.compareTo(val2) > 0) {
            result = 2;
//            result = "第一位数大！";
        }
        return result;
    }
}
