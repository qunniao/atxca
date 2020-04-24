package com.atxca.Util;

import java.math.BigDecimal;

public class DecimalCalcUtil {

    //float和double类型计算

    /**
     * double类型加法
     *
     * @return 计算结果
     * @param被加数
     * @param加数
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * float类型加法
     *
     * @param被加数
     * @param加数
     * @return计算结果
     */
    public static float add(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.add(b2).floatValue();
    }

    /**
     * double类型减法
     *
     * @param被减数
     * @param减数
     * @return计算结果
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * float类型减法
     *
     * @return 计算结果
     * @param被减数
     * @param减数
     */
    public static float sub(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.subtract(b2).floatValue();
    }

    /**
     * double类型乘法
     *
     * @param乘数1
     * @param乘数2
     * @return计算结果
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * float类型乘法
     *
     * @param乘数1
     * @param乘数2
     * @return计算结果
     */
    public static float mul(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.multiply(b2).floatValue();
    }

    /**
     * double类型除法
     *
     * @param被除数
     * @param除数
     * @return计算结果
     */
    public static double div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2).floatValue();
    }

    /**
     * float类型除法
     *
     * @param被除数
     * @param除数
     * @return计算结果
     */
    public static double div(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.divide(b2).floatValue();
    }


}
