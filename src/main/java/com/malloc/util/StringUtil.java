package com.malloc.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;


/**
 * 字符串工具类
 *
 * @author tuyh3(tuyh3 @ asiainfo.com)
 * @desc
 * @date 2020/11/13 11:40
 * @Version
 */
public class StringUtil {
    /**
     * log4j 日志
     */
    private static Logger logger = (Logger) LogManager.getLogger(StringUtil.class);

    /**
     * 默认时间格式字符串
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式字符串2
     */
    public static final String DATE_TIME_FORMAT2 = "yyyy-MM-dd+HHmmss";

    //private StringUtil(){}

    /**
     * IllegalAccessError:
     *      如果应用程序尝试访问或修改字段，或调用其无权访问的方法，则抛出该异常。
     *      通常，编译器会捕获此错误; 如果类的定义不兼容地更改，则此错误只能在运行时发生。
     */
    private StringUtil() {
        throw new IllegalAccessError("Utility class");
    }

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

    /**
     * 生成32位全局唯一ID
     *
     * @return ID字符串
     */
    public static String createUUID() {
        String uuid = java.util.UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23)
                + uuid.substring(24);
    }

    /**
     * 转义 html 字符串
     *
     * @param str 原始字符串
     * @return 转以后的字符串
     */
    public static String escapeHtml(String str) {
        if (str == null) {
            return "";
        }
        int len = str.length();
        if (len == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (c == '"') {
                sb.append("&quot;");
            } else if (c == '&') {
                sb.append("&amp;");
            } else if (c == '<') {
                sb.append("&lt;");
            } else if (c == '>') {
                sb.append("&gt;");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 转义XML字符串
     *
     * @param str 原始字符串
     * @return 转义后的字符串
     */
    public static String escapeXml(String str) {
        if (isBlank(str)) {
            return "";
        }

        int len  = str.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (c == '\'') {
                sb.append("&apos;");
            } else if (c == '"') {
                sb.append("&quot;");
            } else if (c == '&') {
                sb.append("&amp;");
            } else if (c == '<') {
                sb.append("&lt;");
            } else if (c == '>') {
                sb.append("&gt;");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }

    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null"); //$NON-NLS-1$
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.write("\\u" + hex(ch)); //$NON-NLS-1$
            } else if (ch > 0xff) {
                out.write("\\u0" + hex(ch)); //$NON-NLS-1$
            } else if (ch > 0x7f) {
                out.write("\\u00" + hex(ch)); //$NON-NLS-1$
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    default:
                        out.write(ch > 0xf ? ("\\u00" + hex(ch)) : ("\\u000" + hex(ch))); //$NON-NLS-1$
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) {
                            out.write('\\');
                        }
                        out.write('\'');
                        break;
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    default:
                        out.write(ch);
                        break;
                }
            }
        }
    }

    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            logger.error("", ioe);
            return null;
        }
    }

    /**
     * 转义JavaScript字符串
     *
     * @param str 原始字符串
     * @return 转义后的字符串
     */
    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true);
    }

    /**
     * 将XML格式化输出成字符串
     *
     * @param doc   XML对象
     * @param format 输入格式
     * @return 字符串
     */
    public static String xml2String(Document doc, OutputFormat format) {
        StringWriter out = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(out, format);
        try {
            xmlWriter.write(doc);
            xmlWriter.flush();
        } catch (IOException e) {
            logger.error("", e);
            return "";
        }

        return out.toString();
    }

    /**
     * 按默认格式将XML输入成字符串：新行缩进两个空格
     *
     * @param doc XML对象
     * @return 字符串
     */
    public static String xml2String(Document doc) {
        OutputFormat format = new OutputFormat("  ", true);
        return  xml2String(doc, format);
    }

    /**
     * 将JSONObject对象转换成Map
     *
     * @param jo JSONObject对象
     * @return Map
     */
    public static Map<String, String> jsonObject2Map(JSONObject jo) {
        Map<String, String> map = new HashMap<>();
        if (jo == null) {
            return map;
        }

        Set<String> keySet = jo.keySet();
        for (String key : keySet) {
            map.put(key.trim(), jo.getString(key).trim());
        }

        return map;
    }

    /**
     * 将JSON数组转化为List
     *
     * @param ja JSON数组
     * @return List
     */
    public static List<Map<String, String>> jsonArray2List(JSONArray ja) {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < ja.size(); i++) {
            list.add(StringUtil.jsonObject2Map((JSONObject) ja.get(i)));
        }

        return list;
    }

    /**
     * 将字符串转换成Map
     *
     * @param jsonStr 字符串
     * @return Map
     */
    public static Map<String, String> jsonString2Map(String jsonStr) {
        try {
            JSONObject jo = JSONObject.parseObject(jsonStr);
            return jsonObject2Map(jo);
        } catch (JSONException e) {
            logger.error("解析json字符串出错", e);
            return new HashMap<String, String>();
        }
    }

    /**
     * 判断字符串是否为空(str == null || str.trim().length() == 0)
     *
     * @param str 字符串
     * @return true=空，false=不为空
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 将null对象转为空串
     *
     * @param str 字符串
     * @return 装换后字符串
     */
    public static String null2Blank(String str) {
        return str == null ? "" : str;
    }

    /**
     * 检查字符串是否为合法的 xml 字符
     * XML规范中规定了允许的字符范围(http://www.w3.org/TR/REC-xml#dt-character): Char ::= #x9| #xA | #xD |
     * [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
     *
     * @param ch 字符
     * @return 是否合法
     */
    public static boolean isLegalXMLCharacter(int ch) {
        if (ch <= 0xD7FF) {
            if (ch <= 0xD7FF) {
                return true;
            } else {
                return ch == '\n' || ch == '\r' || ch == '\t';
            }
        }

        return (ch >= 0xE00 && ch <= 0xFFFD) || (ch >= 0x10000 && ch <= 0x10FFFF);
    }

}
