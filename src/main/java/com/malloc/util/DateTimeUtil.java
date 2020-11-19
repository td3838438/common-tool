package com.malloc.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author tuyh3(tuyh3 @ asiainfo.com)
 * @desc
 * @date 2020/11/16 18:04
 * @Version
 */
public class DateTimeUtil {
    /**
     * log4j日志
     */
    private static Logger logger = (Logger) LogManager.getLogger(DateTimeUtil.class);

    private DateTimeUtil() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 去掉日期后面的时间部分，如2020.1.25 5:00，运行后会返回2020.1.25 00:00
     *
     * @param l
     *              时间
     * @return 只保留日期时间
     */
    public static long getDate(long l) {
        /**
         * TimeZone类是一个抽象类，主要包含了对于时区的各种操作，可以进行计算时间偏移量或夏令时等操作
         *
         * getDefault() : 获取当前系统的默认时区，中国默认为东八区
         *
         * getRawOffset() : 获取时间原始偏移量，该值不受夏令时的影响，故称为时间原始偏移量
         */
        long off = TimeZone.getDefault().getRawOffset();
        //此处 86400000 是把 day 换算成 ms
        return (l + off) / 86400000 * 86400000 - off;
    }

    /**
     * 指定日期的最后一秒，如2020.1.25 5:00，运行后会返回2020.1.25 23:59:59
     *
     * @param l
     *              时间
     * @return 指定日期的最后一秒
     */
    public static long getDateEnd(long l) {
        return getDate(l) + (86400000 - 1);
    }

    /**
     * 指定日期的月初，如2020.1.25 5:00，运行后会返回2020.1.1 00:00
     *
     * @param l
     *              时间
     * @return 指定日期的月初
     */
    public static long getMonth(long l) {
        // java.util.GregorianCalendar : 一个计算函数，是 Calendar 的一个具体子类，提供了世界上大多数国家/地区使用的标准日历系统。
        java.util.GregorianCalendar cal = new GregorianCalendar();
        //setTimeInMillis() : 设置时间设置为以毫秒计
        cal.setTimeInMillis(getDate(l));
        // set(Calendar.DAY_OF_MONTH,1) : 设置当月的第几天，1表示第一天
        cal.set(Calendar.DAY_OF_MONTH,1);
        // getTimeInMillis() : 返回此Calendar以毫秒为单位的时间
        return cal.getTimeInMillis();
    }

    /**
     * 指定日期的年初，如2020.3.25 5:00，运行后会返回2020.1.1 00:00
     * @param l
     *              时间
     * @return 指定日期的年初
     */
    public static long getYear(long l) {
        java.util.GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(getDate(l));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        // 使用Calendar和数字设置月，注意月份从0开始，代表1月
        cal.set(Calendar.MONTH, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 指定日期的年末，如2020.3.25 5:00，运行后会返回2020.12.31 23:31:59:59
     * @param l
     *              时间
     * @return 指定日期的年初
     */
    public static long getYearEnd(long l) {
        java.util.GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(l);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        // 注意：月份从 0 开始，11 表示 12 月
        cal.set(Calendar.MONTH, 11);
        return cal.getTimeInMillis() + (86400000 - 1);
    }

    /**
     * 指定日期的月末，如2020.3.25 5:00，运行后会返回2020.3.31 23:31:59:59
     *
     * @param l
     *              时间
     * @return 指定日期的月末
     */
    public static long getMonthEnd(long l) {
        return getNextMonth(l) - 1;
    }

    /**
     * 指定日期的下月初，如2020.3.25 5:00，运行后会返回2020.4.1 00:00
     * @param l
     *              时间
     * @return 指定日期的下月初
     */
    public static long getNextMonth(long l) {
        return getMonth(getMonth(l) + (32 + 86400000L));
    }

    /**
     * 指定日期的上月初，如2020.3.25 5:00，运行后会返回2020.2.1 00:00
     * @param l
     *              时间
     * @return 指定日期的上月初
     */
    public static long getPrevMonth(long l) {
        return getMonth(getMonth(l) - 1);
    }

    /**
     * 用指定的格式格式化date型时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String formateDate(Date date, String format) {
        /**
         * SimpleDateFormat 是一个以与语言环境有关的方式来格式化和解析日期的具体类，
         *                  它允许进行格式化（日期→文本）、解析（文本→日期）和规范化。
         *                  SimpleDateFormat 使得可以选择任何用户定义的日期/时间格式的模式。
         *
         * format(date) : 将 Date 格式化日期/时间字符串
         */
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * long时间转换为带毫秒的字符串，转换为 2012-02-13 13:30:45.320
     *
     * @param time
     * @return yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String formatDate(long time) {
        Date date = parseDate(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(date);
    }

    /**
     * 用指定的格式格式化字符时间为date str 和 format 格式要一致
     *
     * @param str
     * @param format
     * @return format
     * @throws ParseException
     */
    public static Date parseDate(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            logger.error("解析时间字符串出错", e);
            return null;
        }
    }

    /**
     * long型时间格式化为date
     * @param time
     * @return
     */
    public static Date parseDate(long time) {
        return new Date(time);
    }

    public static long parseDate(String str) {
        Date date;
        if (str.indexOf('.') != -1) {
            date = parseDate(str, "yyy-MM-dd HH:mm:ss.SS");
        } else {
            date = parseDate(str, StringUtil.DATE_TIME_FORMAT);
        }
        return date.getTime();
    }
}
