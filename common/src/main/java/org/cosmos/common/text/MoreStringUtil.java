package org.cosmos.common.text;

/**
 * 尽量使用Common Lang StringUtils,基本上能满足大部分的String处理场景
 * <p>
 * 补充额外的方法:
 * <p>
 * 1.起始位置的字符串替换
 * <p>
 * 2.判断起始位置是否包含某个字符
 *
 * @author fisher
 */
public class MoreStringUtil {

    /**
     * Replaces the very first occurrence of a substring with supplied string.
     *
     * @param s    source string
     * @param sub  substring to replace
     * @param with substring to replace with
     */
    public static String replaceFirst(String s, String sub, String with) {
        int i = s.indexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }

    /**
     * Replaces the very first occurrence of a character in a string.
     *
     * @param s    string
     * @param sub  char to replace
     * @param with char to replace with
     */
    public static String replaceFirst(String s, char sub, char with) {
        int index = s.indexOf(sub);
        if (index == -1) {
            return s;
        }
        char[] str = s.toCharArray();
        str[index] = with;
        return new String(str);
    }

    /**
     * Replaces the very last occurrence of a substring with supplied string.
     *
     * @param s    source string
     * @param sub  substring to replace
     * @param with substring to replace with
     */
    public static String replaceLast(String s, String sub, String with) {
        int i = s.lastIndexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }

    /**
     * Replaces the very last occurrence of a character in a string.
     *
     * @param s    string
     * @param sub  char to replace
     * @param with char to replace with
     */
    public static String replaceLast(String s, char sub, char with) {
        int index = s.lastIndexOf(sub);
        if (index == -1) {
            return s;
        }
        char[] str = s.toCharArray();
        str[index] = with;
        return new String(str);
    }

    /**
     * Returns if string starts with given character.
     */
    public static boolean startsWith(String s, char c) {
        return s.length() != 0 && s.charAt(0) == c;
    }

    /**
     * Returns if string ends with provided character.
     */
    public static boolean endWith(String s, char c) {
        return s.length() != 0 && s.charAt(s.length() - 1) == c;
    }

}
