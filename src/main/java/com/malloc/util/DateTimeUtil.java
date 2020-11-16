package com.malloc.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Calendar;
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
        long off = TimeZone.getDefault().getRawOffset();
        //此处 86400000 是把 day 换算成 ms
        return (l + off) / 86400000 * 86400000 - off;
    }

    /**
     * 指定日期的最后一秒，如2020.1.25 5:00，运行后会返回2020.1.25 23:59:59
     * @param l
     * @return
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
        java.util.GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(getDate(l));
        cal.set(Calendar.DAY_OF_MONTH,1);
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

}
