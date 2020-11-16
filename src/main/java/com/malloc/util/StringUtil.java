package com.malloc.util;

/**
 * 字符串工具类
 *
 * @author tuyh3(tuyh3 @ asiainfo.com)
 * @desc
 * @date 2020/11/13 11:40
 * @Version
 */
public class StringUtil {

    private StringUtil(){}

    /**
     * 判断是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        if (null != obj && !"".equals(obj)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否为非空
     *
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj) {
        if (null != obj && !"".equals(obj.toString())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为空(忽略前导空白和尾部空白)
     *
     * @param obj
     * @return
     */
    public static boolean isTrimNull(Object obj) {
        if (null != obj) {
            return isNull(obj.toString().trim());
        } else {
            return true;
        }
    }

    /**
     * 判断是否为非空(忽略前导空白和尾部空白)
     *
     * @param obj
     * @return
     */
    public static boolean isTrimNotNull(Object obj) {
        if (null != obj) {
            return isNotNull(obj.toString().trim());
        } else {
            return false;
        }
    }

    /**
     * 得到不为null得字符串
     *
     * @param obj
     * @return
     */
    public static String toNotNullString(Object obj) {
        return null == obj ? "" : obj.toString();
    }

    /**
     * 将 null 转为0
     */
    public static String nullToZero(Object obj) {
        return toNotNullString(obj).equals("") ? "0" : obj.toString();
    }


    /**
     * 将 null 转为空串
     */
    public static String nullToSpace(String str) {
        if (null == str) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 将 null 转为HTML空格
     */
    public static String nullToHtmlSpace(Object obj) {
        return toNotNullString(obj).trim().equals("") ? "&nbsp;" : obj.toString();
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (isTrimNull(s)) {
            return "";
        }

        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (isTrimNull(s)) {
            return "";
        }

        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

}
