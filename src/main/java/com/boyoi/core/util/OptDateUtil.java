package com.boyoi.core.util;

import cn.hutool.core.util.IdUtil;

import java.util.Date;

/**
 * @author ZhouJL
 * @date 2020/4/23 09:52
 */
public class OptDateUtil {


    public static String dateToStr(Date date) {

        long years = getYears(new Date(), date);
        if (0 != years) {
            if (years == 1) {
                return "大概 1 年前";
            } else {
                return "大概 " + years + " 年前";
            }

        } else {
            long months = getMonths(new Date(), date);
            if (0 != months) {
                if (months == 1) {
                    return "大概 1 周前";
                } else {
                    return "大概 " + months + " 周前";
                }
            } else {
                long weeks = getWeeks(new Date(), date);
                if (0 != weeks) {
                    if (weeks == 1) {
                        return "大概 1 周前";
                    } else {
                        return "大概 " + weeks + " 周前";
                    }
                } else {
                    long days = getDays(new Date(), date);
                    if (0 != days) {
                        if (days == 1) {
                            return "大概 1 天前";
                        } else {
                            return "大概 " + days + " 天前";
                        }
                    } else {
                        long hours = getHours(new Date(), date);
                        if (0 != hours) {
                            if (hours == 1) {
                                return "大概 1 个小时前";
                            } else {
                                return "大概 " + hours + " 个小时前";
                            }
                        } else {
                            long minute = getMinute(new Date(), date);
                            if (0 != minute) {
                                if (minute == 1) {
                                    return "大概 1 分钟前";
                                } else {
                                    return "大概 " + minute + " 分钟前";
                                }
                            } else {
                                return "刚刚";
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取两个date相差年份 31104000000 = 一天多少秒*30*12
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差月数
     */
    public static Long getYears(Date date1, Date date2) {
        if (null != date1 && null != date2)
            return Math.abs(date1.getTime() - date2.getTime()) / 31104000000L;
        return null;
    }

    /**
     * 获取两个date相差小时数 3600000=一小时多少秒
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差月数
     */
    public static Long getHours(Date date1, Date date2) {
        if (null != date1 && null != date2) {
            return Math.abs(date1.getTime() - date2.getTime()) / 3600000L;
        }
        return null;
    }

    /**
     * 获取两个date相差月数 259200000=一天多少秒*30
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差月数
     */
    public static Long getMonths(Date date1, Date date2) {
        if (null != date1 && null != date2)
            return Math.abs(date1.getTime() - date2.getTime()) / 2592000000L;
        return null;
    }

    /**
     * 获取两个date相差周数 604800000一周多少豪秒
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差周数
     */
    public static Long getWeeks(Date date1, Date date2) {
        if (null != date1 && null != date2)
            return Math.abs(date1.getTime() - date2.getTime()) / 604800000l;
        else
            return 0l;
    }

    /**
     * 获取两个date相差天数 86400000一天多少豪秒
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差天数
     */
    public static Long getDays(Date date1, Date date2) {
        if (null != date1 && null != date2)
            return Math.abs(date1.getTime() - date2.getTime()) / 86400000l;
        else
            return 0l;
    }

    public static Long getMinute(Date date1, Date date2) {
        if (null != date1 && null != date2) {
            return Math.abs(date1.getTime() - date2.getTime()) / 60000L;
        }
        return null;
    }
}
