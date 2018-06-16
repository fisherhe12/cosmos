package org.cosmos.common.text;

import org.apache.commons.lang3.StringUtils;
import org.cosmos.common.base.Emptys;


/**
 * 拼音字母工具类
 *
 * @author fisher
 */
public abstract class PinyinUtil {

    /**
     * 除了A-Z之外的字符标志
     */
    private static final String otherType = "other_type";

    private static int compare(String str1, String str2) {
        String s1;
        String s2;

        try {
            s1 = new String(str1.getBytes(Charsets.GBK), Charsets.GBK);
            s2 = new String(str2.getBytes(Charsets.GBK), Charsets.GBK);
        } catch (Exception e) {
            return str1.compareTo(str2);
        }

        return chineseCompareTo(s1, s2);

    }

    public static int chineseCompareTo(String string1, String string2) {
        if (StringUtils.isAnyBlank(string1, string2)) {
            return 0;
        }

        if (StringUtils.isBlank(string1)) {
            return -1;
        }

        if (StringUtils.isBlank(string2)) {
            return 1;
        }

        int len1 = string1.length();
        int len2 = string2.length();
        int n = Math.min(len1, len2);

        for (int i = 0; i < n; i++) {
            int s1Code = getCharCode(Character.toString(string1.charAt(i)));
            int s2Code = getCharCode(Character.toString(string2.charAt(i)));

            if ((s1Code * s2Code) < 0) {
                return Math.min(s1Code, s2Code);
            }

            if (s1Code != s2Code) {
                return s1Code - s2Code;
            }
        }

        return len1 - len2;
    }

    private static int getCharCode(String string) {
        byte[] bytes = string.getBytes();
        int value = 0;

        for (int i = 0; (i < bytes.length) && (i <= 2); i++)
            value = (value * 100) + bytes[i];

        return value;
    }

    /**
     * 得到一个字符串所有汉字的首字母,
     * <p>
     * 比如: getBeginCharacter("我爱北京天安门") = "wabjtam"
     * </p>
     *
     * @param string 字符串
     * @return 汉字的首字母
     */
    public static String getBeginCharacter(String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        }

        StringBuilder result = new StringBuilder(Emptys.EMPTY_STRING);

        for (int i = 0; i < string.length(); i++) {
            String current = string.substring(i, i + 1);
            if (compare(current, "\u554A") < 0) {
                result.append(current);
            }
            // FIXME
            else if ((compare(current, "\u554A") >= 0) && (compare(current, "\u5EA7") <= 0)) {
                if (compare(current, "\u531D") >= 0) {
                    result.append("z");
                } else if (compare(current, "\u538B") >= 0) {
                    result.append("y");
                } else if (compare(current, "\u6614") >= 0) {
                    result.append("x");
                } else if (compare(current, "\u6316") >= 0) {
                    result.append("w");
                } else if (compare(current, "\u584C") >= 0) {
                    result.append("t");
                } else if (compare(current, "\u6492") >= 0) {
                    result.append("s");
                } else if (compare(current, "\u7136") >= 0) {
                    result.append("r");
                } else if (compare(current, "\u671F") >= 0) {
                    result.append("q");
                } else if (compare(current, "\u556A") >= 0) {
                    result.append("p");
                } else if (compare(current, "\u54E6") >= 0) {
                    result.append("o");
                } else if (compare(current, "\u62FF") >= 0) {
                    result.append("n");
                } else if (compare(current, "\u5988") >= 0) {
                    result.append("m");
                } else if (compare(current, "\u5783") >= 0) {
                    result.append("l");
                } else if (compare(current, "\u5580") >= 0) {
                    result.append("k");
                } else if (compare(current, "\u51FB") > 0) {
                    result.append("j");
                } else if (compare(current, "\u54C8") >= 0) {
                    result.append("h");
                } else if (compare(current, "\u5676") >= 0) {
                    result.append("g");
                } else if (compare(current, "\u53D1") >= 0) {
                    result.append("f");
                } else if (compare(current, "\u86FE") >= 0) {
                    result.append("e");
                } else if (compare(current, "\u642D") >= 0) {
                    result.append("d");
                } else if (compare(current, "\u64E6") >= 0) {
                    result.append("c");
                } else if (compare(current, "\u82AD") >= 0) {
                    result.append("b");
                } else if (compare(current, "\u554A") >= 0) {
                    result.append("a");
                }
            }
        }

        if (result.length() <= 0) {
            result = new StringBuilder(otherType);
        }

        return result.toString();
    }

    /**
     * 得到字符串首字符的拼音首字母 如果首字符是数字
     * <p>
     * 返回 OTHER_TYPE,如果英文字母，则返回字母的小写
     *
     * @param string 普通字符串
     * @return 拼音首字母或OTHER_TYPE
     */
    public static String getFirstSpell(String string) {
        String result = null;

        if (StringUtils.isNotBlank(string)) {
            char a = string.charAt(0);

            if (Character.isDigit(a)) {
                result = otherType;
            } else if (((a >= 'a') && (a <= 'z')) || ((a >= 'A') && (a <= 'Z'))) {
                result = Character.toString(a).toLowerCase();
            } else {
                result = getBeginCharacter(Character.toString(a));
            }
        }

        return result;
    }

}
