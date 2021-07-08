package com.boyoi.core.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间操作的帮助类
 *
 * @author Chenj
 */

public class DateTimeUtil {

    private static final SimpleDateFormat yy = new SimpleDateFormat("yy");
    private static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat yyyyMMddNoSeparator = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat yyyyMMddHHmmssNoSeparator = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat yyMMddHHmmssNoSeparator = new SimpleDateFormat("yyMMddHHmmss");
    private static final SimpleDateFormat yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final SimpleDateFormat yyyyMMddHHmmssSSSNoSeparator = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
    private static final SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat MMddHH = new SimpleDateFormat("MM-dd HH");
    private static final SimpleDateFormat MM = new SimpleDateFormat("MM");
    private static final SimpleDateFormat dd = new SimpleDateFormat("dd");
    private static final SimpleDateFormat MMddHHNoSeparator = new SimpleDateFormat("MMddHH");
    private static final SimpleDateFormat yyMMdd = new SimpleDateFormat("yyMMdd");
    // 2286年11月21号01：46：39
    private static final long L9999999999999 = 9999999999999l;

    /**
     * 获取当前时间的后i天
     *
     * @param i
     * @return
     */
    public static String getAddDay(int i) {
        String currentTime = yyyyMMddhhmmss(new Date());
        GregorianCalendar gCal = new GregorianCalendar(
                Integer.parseInt(currentTime.substring(0, 4)),
                Integer.parseInt(currentTime.substring(5, 7)) - 1,
                Integer.parseInt(currentTime.substring(8, 10)));
        gCal.add(GregorianCalendar.DATE, i);
        return yyyyMMdd(gCal.getTime());
    }

    /**
     * 获取当前时间的后i天
     * 精确到秒
     *
     * @param i
     * @return
     */
    public static String getAddDayTime(int i) {
        Date date = new Date(System.currentTimeMillis() + i * 24 * 60 * 60 * 1000);
        return yyyyMMddhhmmss(date);
    }

    /**
     * 获取当前时间的+多少秒
     * 精确到秒
     *
     * @param i
     * @return
     */
    public static String getAddDaySecond(int i) {
        Date date = new Date(System.currentTimeMillis() + i * 1000);
        return yyyyMMddhhmmss(date);
    }


    /**
     * 倒序排列时间。以2289年11月21号01：46：39的时间减去给出的时间
     *
     * @return 倒序时间
     */
    public static long compareDate9999999999999(Date date) {
        return L9999999999999 - date.getTime();
    }

    /**
     * 比较二个日期，返回第多少位，两个日期不相同
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 日期相同的部份
     */
    public static String compareDate(Date date1, Date date2) {

        String d1 = yyyyMMddHHmmssNoSeparator(date1);
        String d2 = yyyyMMddHHmmssNoSeparator(date2);

        for (int i = 0; i < 14; i++) {
            if (d1.charAt(i) != d2.charAt(i)) {
                return d1.substring(0, i);
            }
        }
        return d1;
    }

    /**
     * 日期比较，如果s>=e 返回true 否则返回false
     *
     * @param s
     * @param e
     * @return
     */
    public static boolean compareDate2(String s, String e) {
        Date d = yyyyMMddhhmmss(s);
        Date l = yyyyMMddhhmmss(e);
        if (d == null || l == null) {
            return false;
        }
        return d.compareTo(l) > 0;
    }

    /**
     * 比较二个日期，返回第多少位，两个日期不相同
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 日期相同的部份
     */
    public static int compareDate(String date1, String date2) {
        if (null != date1 && null != date2 && date1.length() == date2.length()) {
            for (int i = 0; i < date1.length(); i++) {
                if (date1.charAt(i) != date2.charAt(i)) {
                    return date1.charAt(i) - date2.charAt(i);
                }
            }
        }
        return 0;
    }

    /**
     * 时间添加或相减
     * 格式:yyyy-MM-dd
     *
     * @param date    date对象
     * @param addType 添加的类型。如：Calendar.MINUTE
     * @param addNum  添加的值
     * @return yyyy-MM-dd
     */
    public static Date timeAdd(Date date, int addType, int addNum) {
        if (null != date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(addType, addNum);
            return calendar.getTime();

        }
        return null;
    }

    /**
     * 时间相加或相减 +-1
     * 只针对当前类型。总的时间不会改变。
     * 格式:yyyy-MM-dd
     *
     * @param date         date对象
     * @param subtractType 相减的类型。如：Calendar.MINUTE
     * @param up           相减或相减，true,相同，false相减
     * @return Date
     */
    public static Date timeRoll(Date date, int subtractType, boolean up) {
        if (null != date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.roll(subtractType, up);
            return calendar.getTime();

        }
        return null;
    }

    /**
     * 日期转字符串
     * 格式:yyyy-MM-dd
     *
     * @param date date对象
     * @return yyyy-MM-dd
     */
    public static String yyyyMMdd(Date date) {
        if (null != date)
            return yyyyMMdd.format(date);
        return null;
    }

    /**
     * 日期转字符串
     * 格式:yyyy-MM-dd
     *
     * @param date date对象
     * @return yyyy-MM-dd
     */
    public static String yy(Date date) {
        if (null != date)
            return yy.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyyy-MM-dd
     *
     * @param date yyyy-MM-dd
     * @return date对象
     */
    public static Date yyyyMMdd(String date) {
        try {
            return yyyyMMdd.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyyy-MM-dd
     *
     * @param date date对象
     * @return yyyy-MM-dd
     */
    public static String yyyyMMddNoSeparator(Date date) {
        if (null != date)
            return yyyyMMddNoSeparator.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyyy-MM-dd
     *
     * @param date yyyy-MM-dd
     * @return date对象
     */
    public static Date yyyyMMddNoSeparator(String date) {
        try {
            return yyyyMMddNoSeparator.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyMMdd
     *
     * @param date date对象
     * @return yyMMdd
     */
    public static String yyMMdd(Date date) {
        if (null != date)
            return yyMMdd.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyMMdd
     *
     * @param date yyMMdd
     * @return date对象
     */
    public static Date yyMMdd(String date) {
        try {
            return yyMMdd.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyyy-MM-dd hh:mm
     *
     * @param date date对象
     * @return yyyy-MM-dd hh:mm
     */
    public static String yyyyMMddhhmm(Date date) {
        if (null != date)
            return yyyyMMddHHmm.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyyy-MM-dd hh:mm
     *
     * @param date yyyy-MM-dd hh:mm
     * @return date对象
     */
    public static Date yyyyMMddhhmm(String date) {
        try {
            return yyyyMMddHHmm.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyyy-MM-dd hh:mm
     *
     * @param date date对象
     * @return yyyy-MM-dd hh:mm
     */
    public static String HHmm(Date date) {
        if (null != date)
            return HHmm.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyyy-MM-dd hh:mm
     *
     * @param date yyyy-MM-dd hh:mm
     * @return date对象
     */
    public static Date HHmm(String date) {
        try {
            return HHmm.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyyy-MM-dd hh:mm:ss
     *
     * @param date date对象
     * @return yyyy-MM-dd hh:mm:ss
     */
    public static String yyyyMMddhhmmss(Date date) {
        if (null != date)
            return yyyyMMddHHmmss.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyyy-MM-dd hh:mm:ss
     *
     * @param date yyyy-MM-dd hh:mm:ss
     * @return date对象
     */
    public static Date yyyyMMddhhmmss(String date) {
        try {
            return yyyyMMddHHmmss.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyyyMMddHHmmss
     *
     * @param date date对象
     * @return yyyyMMddHHmmss
     */
    public static String yyyyMMddHHmmssNoSeparator(Date date) {
        if (null != date)
            return yyyyMMddHHmmssNoSeparator.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyyyMMddHHmmss
     *
     * @param date yyyyMMddHHmmss
     * @return date对象
     */
    public static Date yyyyMMddHHmmssNoSeparator(String date) {
        try {
            return yyyyMMddHHmmssNoSeparator.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyyyMMddHHmmssSSS
     *
     * @param date date对象
     * @return yyyyMMddHHmmss
     */
    public static String yyyyMMddHHmmssSSS(Date date) {
        if (null != date)
            return yyyyMMddHHmmssSSS.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyyyMMddHHmmss
     *
     * @param date yyyyMMddHHmmss
     * @return date对象
     */
    public static Date yyyyMMddHHmmssSSS(String date) {
        try {
            return yyyyMMddHHmmssSSS.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyyyMMddHHmmssSSS
     *
     * @param date date对象
     * @return yyyyMMddHHmmss
     */
    public static String yyyyMMddHHmmssSSSNoSeparator(Date date) {
        if (null != date)
            return yyyyMMddHHmmssSSSNoSeparator.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyyyMMddHHmmss
     *
     * @param date yyyyMMddHHmmss
     * @return date对象
     */
    public static Date yyyyMMddHHmmssSSSNoSeparator(String date) {
        try {
            return yyyyMMddHHmmssSSSNoSeparator.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyyy-MM
     *
     * @param date date对象
     * @return yyyy-MM
     */
    public static String yyyyMM(Date date) {
        if (null != date)
            return yyyyMM.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:yyyy-MM
     *
     * @param date yyyy-MM
     * @return date对象
     */
    public static Date yyyyMM(String date) {
        try {
            return yyyyMM.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:yyyy
     *
     * @param date date对象
     * @return yyyy
     */
    public static String yyyy(Date date) {
        if (null != date)
            return yyyy.format(date);
        return null;
    }


    /**
     * 字符转日期
     * 格式:yyyy
     *
     * @param date yyyy
     * @return date对象
     */
    public static Date yyyy(String date) {
        try {
            return yyyy.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:MM
     *
     * @param date date对象
     * @return MM
     */
    public static String MM(Date date) {
        if (null != date)
            return MM.format(date);
        return null;
    }

    /**
     * 字符转日期
     * 格式:MM
     *
     * @param date MM
     * @return date对象
     */
    public static Date MM(String date) {
        try {
            return MM.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符转日期
     * 格式:dd
     *
     * @param date dd
     * @return date对象
     */
    public static Date dd(String date) {
        try {
            return dd.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转字符串
     * 格式:dd
     *
     * @param date date对象
     * @return dd
     */
    public static String dd(Date date) {
        if (null != date)
            return dd.format(date);
        return null;
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
     * 获取两个date相差小时数 3600000=一小时多少秒 （按半小时向上取整）
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差月数
     */
    public static Double getHourByHalfUp(Date date1, Date date2) {
        if (null != date1 && null != date2) {
            double getTimes = Math.abs(date1.getTime() - date2.getTime()) / 3600000D;
            BigDecimal bigDecimal = new BigDecimal(getTimes * 2);
            return bigDecimal.setScale(0, RoundingMode.UP).doubleValue() / 2;
        }
        return null;
    }

    public static Long getMinute(Date date1, Date date2) {
        if (null != date1 && null != date2) {
            return Math.abs(date1.getTime() - date2.getTime()) / 60000L;
        }
        return null;
    }

    /**
     * 获取两个date相差小时数 3600000=一小时多少秒（按小时向上取整）
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差月数
     */
    public static Double getHourByFullUp(Date date1, Date date2) {
        if (null != date1 && null != date2) {
            double getTimes = Math.abs(date1.getTime() - date2.getTime()) / 3600000D;
            BigDecimal bigDecimal = new BigDecimal(getTimes);
            return bigDecimal.setScale(0, RoundingMode.UP).doubleValue();
        }
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
     * 获取时间的年月日时的格式
     *
     * @param date 待转化时间
     * @return 年月日时的字符串
     */
    public static String MMddHH(Date date) {
        if (null != date) {
            return MMddHH.format(date);
        }
        return null;
    }

    /**
     * 获取时间的年月日时的无间隔的数据格式
     *
     * @param date
     * @return 年月日时的无间隔字符串
     */
    public static String MMddHHNoSeparator(Date date) {
        if (null != date) {
            return MMddHHNoSeparator.format(date);
        }
        return null;
    }

    /**
     * 获取时间的年月日时的无间隔的数据格式
     *
     * @param date
     * @return 年月日时的无间隔字符串
     */
    public static String yyMMddHHmmssNoSeparator(Date date) {
        if (null != date) {
            return yyMMddHHmmssNoSeparator.format(date);
        }
        return null;
    }

    /**
     * 获得自定义格式化内容
     *
     * @param dateFormat 自定义的格式化
     * @param date       日期
     * @return 自定义格式化内容
     */
    public static String custom(String dateFormat, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        if (null != date) {
            return simpleDateFormat.format(date);
        }
        return null;
    }


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
     * 近三个月的开始一天
     */
    public static String getThreeMonthsDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(calendar.MONTH, +3); //设置为前3月
        return yyyyMMdd.format(calendar.getTime());
    }

    /**
     * 近几年
     */
    public static List<String> getRecentYear(int year) {
        if (year < 1) return null;
        List years = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());//把当前时间赋给日历
        years.add(yyyy.format(calendar.getTime()));
        for (int i = 1; i < year; i++) {
            calendar.add(calendar.YEAR, -1);
            years.add(yyyy.format(calendar.getTime()));
        }
        return years;
    }

    /**
     * 给时间加上几个小时
     *
     * @param hour 需要加的时间
     * @return
     */
    public static Date addDateHours(Date date1, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date1 = cal.getTime();
        return date1;

    }

}

