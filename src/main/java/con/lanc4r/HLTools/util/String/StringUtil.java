package con.lanc4r.HLTools.util.String;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import con.lanc4r.HLTools.consts.MainConsts;

public class StringUtil extends org.apache.commons.lang3.StringUtils {

    /********************** 默认字符 ***********************/
    public static final String REGEX_COMMA = ",";
    public static final String REGEX_VIRGULE = "|";
    public static final String JSON_START_CHAR1 = "{";
    public static final String JSON_START_CHAR2 = "[";
    public static final String JSON_END_CHAR1 = "}";
    public static final String JSON_END_CHAR2 = "]";
    public static final String UNDERSTOOD_STR = "...";

    /********************** 默认字符 ***********************/
    private static DecimalFormat NUMBER_FORMAT1 = new DecimalFormat("0.00");
    private static DecimalFormat NUMBER_FORMAT2 = new DecimalFormat("###,###,###,##0.00");
    private static DecimalFormat NUMBER_FORMAT3 = new DecimalFormat("###########0.00");

    /********************** 正则匹配 ***********************/
    // 匹配0
    private static Pattern pattern_zero = Pattern.compile("0+?$");
    // 匹配.
    private static Pattern pattern_dot = Pattern.compile("[.]$");
    // 匹配中文
    private static Pattern pattern_chinese = Pattern.compile("[\u4e00-\u9fa5\\w]+");
    // 匹配 <script> 标签
    private static Pattern pattern_script = Pattern
            .compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", Pattern.CASE_INSENSITIVE);
    // 匹配 <style> 标签
    private static Pattern pattern_style = Pattern
            .compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", Pattern.CASE_INSENSITIVE);
    // 匹配 HTML的 标签
    private static Pattern pattern_html = Pattern.compile("(<|＜)\\/?.+?\\/?(>|＞)", Pattern.CASE_INSENSITIVE);
    // 匹配特殊字符
    private static Pattern pattern_special = Pattern.compile("\\&[a-zA-Z]{1,10};", Pattern.CASE_INSENSITIVE);
    // 匹配\r\n\t\s*
    private static Pattern pattern_rnt = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * @描述：将内容中的大于号和小于号换成英文的
     */
    public static String replaceContent(String content) {
        if (StringUtils.isNotBlank(content)) {
            return content.replaceAll("＜", "<").replaceAll("＞", ">").replaceAll("＝", "=");
        }
        return content;
    }

    /**
     * 拆分字符串为字符列表
     */
    public static List<String> string2StringList(String str) {
        return string2StringList(str, REGEX_COMMA);
    }

    public static List<String> string2StringList(String str, String regex) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String[] split = str.split(regex);
        if (split == null || split.length == 0) {
            return null;
        }
        return Arrays.asList(split);
    }

    /**
     * 拆分字符串为数值(Long)列表
     */
    public static List<Long> string2LongList(String str) {
        return string2LongList(str, REGEX_COMMA);
    }

    public static List<Long> string2LongList(String str, String regex) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String[] split = str.split(regex);
        if (split == null || split.length == 0) {
            return null;
        }
        List<Long> list = new ArrayList<Long>(split.length);
        for (String val : split) {
            if (StringUtils.isBlank(val)) {
                continue;
            }
            list.add(Long.valueOf(val));
        }
        return list;
    }

    /**
     * 把一个集合内容拼接为一个字符串,默认逗号
     */
    public static String collection2String(Collection<?> list) {
        return collection2String(list, REGEX_COMMA);
    }

    public static String collection2String(Collection<?> list, String regex) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Object obj : list) {
            if (obj == null) {
                continue;
            }
            builder.append(obj.toString()).append(regex);
        }
        int len = builder.length();
        if (len > 0) { // 去除最后一个拼接符
            return builder.toString().substring(0, len - 1);
        } else {
            return builder.toString();
        }
    }

    /**
     * @描述：格式化手机号,隐藏后四位
     */
    public static String formatMobile(String mobile) {
        if (isBlank(mobile)) {
            return mobile;
        }
        if (mobile.length() < 11) {
            return mobile;
        }
        return mobile.substring(0, 4) + "****" + mobile.substring(8, 11);
    }

    /**
     * 对过长的字符串进行省略
     */
    public static String subString(String str, int beginIndex, int endIndex) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (str.length() > endIndex) {
            return str.substring(beginIndex, endIndex) + UNDERSTOOD_STR;
        }
        return str;
    }

    /**
     * 去掉多余的0
     */
    public static String removeZero(Double d) {
        String s = String.valueOf(d);
        if (s.indexOf(".") > 0) {
            s = pattern_zero.matcher(s).replaceAll(StringUtils.EMPTY);// 去掉多余的0
            s = pattern_dot.matcher(s).replaceAll(StringUtils.EMPTY);// 如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 提取中文字符
     */
    public static String pickUpSpecialChar(String str) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isBlank(str)) {
            return StringUtils.EMPTY;
        }
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            char s = str.charAt(i);
            Matcher mt = pattern_chinese.matcher(String.valueOf(s));
            if (mt.matches()) {
                sb.append(String.valueOf(s));
            }
        }
        return sb.toString();
    }

    /**
     * 数值格式化
     */
    public static String formatDouble(Double f) {
        if (f == null || f.equals(new Double(MainConsts.ZERO_STR))) {
            return MainConsts.ZERO_STR;
        }
        String retult = StringUtils.EMPTY;
        if (f.doubleValue() < 1000) {
            retult = NUMBER_FORMAT1.format(f);
        } else {
            retult = NUMBER_FORMAT2.format(f);
        }
        return retult;
    }

    /**
     * 数值格式化: 保留两位小数，如果小数点后面全部是0，则舍去
     */
    public static String formatDouble2(Double d) {
        if (d == null) {
            return StringUtils.EMPTY;
        }
        String s = NUMBER_FORMAT3.format(d);
        if (s.lastIndexOf(".") > 0) {
            String s1 = s.substring(s.lastIndexOf(".") + 1);
            if (s1.charAt(0) == '0' && s1.charAt(1) == '0') {
                s = s.substring(0, s.lastIndexOf("."));
            }
        }
        return s;
    }

    // TODO: ### 这个后面放到 MobileUtil 中
    public static String encryptByTelStill(String info) {
        try {
            if (StringUtils.isNotBlank(info)) {
                if (info.length() > 8) {
                    String start = info.substring(0, 3);
                    String end = info.substring(info.length() - 4, info.length());
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < info.length() - 7; i++) {
                        sb.append("*");
                    }
                    info = start + sb.toString() + end;
                    return info;

                } else if (info.length() >= 7) {
                    String start = info.substring(0, 3);
                    String end = info.substring(info.length() - 3, info.length());
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < info.length() - 6; i++) {
                        sb.append("*");
                    }
                    info = start + sb.toString() + end;
                    return info;
                }
            }
        } catch (Exception e) {
            return info;
        }
        return info;
    }

    /**
     * 秒转小时和分钟
     */
    public static String second2HourMinute(long second) {
        long h = 0, m = 0;
        second = second / 60;// -->取得分钟
        m = second % 60; // 剩余分钟
        h = second / 60;// -->取得小时
        StringBuilder builder = new StringBuilder();
        if (h > 0) {
            builder.append(h).append("小时");
        }
        if (m > 0) {
            builder.append(m).append("分钟");
        }
        return builder.toString();
    }

    /**
     * 删除Html标签
     */
    public static String removeHtmlTag(String htmlStr) {
        if (isBlank(htmlStr)) {
            return EMPTY;
        }
        Matcher matcher;
        matcher = pattern_script.matcher(htmlStr);
        htmlStr = matcher.replaceAll(EMPTY);
        matcher = pattern_style.matcher(htmlStr);
        htmlStr = matcher.replaceAll(EMPTY);
        matcher = pattern_html.matcher(htmlStr);
        htmlStr = matcher.replaceAll(EMPTY);
        matcher = pattern_special.matcher(htmlStr);
        htmlStr = matcher.replaceAll(EMPTY);
        matcher = pattern_rnt.matcher(htmlStr);
        htmlStr = matcher.replaceAll(EMPTY);
        return htmlStr;
    }

    /**
     * 删除所有Html的标签
     */
    public static String removeHtmlBrackets(String htmlStr) {
        if (isBlank(htmlStr)) {
            return EMPTY;
        }
        Matcher matcher = pattern_html.matcher(htmlStr);
        htmlStr = matcher.replaceAll(EMPTY);
        return htmlStr;
    }

    /**
     * 替换尖括号
     */
    public static String replaceHtmlTag(String str) {
        if (StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }
        str = str.replaceAll("＜", "<").replaceAll("＞", ">").replaceAll("＝", "=");
        return str;
    }

    /**
     * 简单判断字符串是否为 Json，注意这里只简单判断 是否在 {}、[] 中
     * 
     * @param str
     * @return
     */
    public static boolean isJsonStrSimplely(String str) {
        boolean result = false;
        if (isNotBlank(str)) {
            str = str.trim();
            if (str.startsWith(JSON_START_CHAR1) && str.endsWith(JSON_END_CHAR1)) {
                result = true;
            } else if (str.startsWith(JSON_START_CHAR2) && str.endsWith(JSON_END_CHAR2)) {
                result = true;
            }
        }
        return result;
    }

}
