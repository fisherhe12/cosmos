package org.cosmos.common.base;


import org.apache.commons.lang3.BooleanUtils;

/**
 * <p>1.简单的字符串转布尔对象的工具类</p>
 * <p>2.如果字符串为空，则返回传入的默认值</p>
 * <p>3.支持ture/false,on/off,y/n,yes/no转换</p>
 */
public class BooleanUtil {

    /**
     * 使用标准JDK，只分析是否忽略大小写的"true", 为空时返回false
     */
    public static boolean toBoolean(String str) {
        return Boolean.parseBoolean(str);
    }

    /**
     * 使用标准JDK，只分析是否忽略大小写的"true", 为空时返回null
     */
    public static Boolean toBooleanObject(String str) {
        return str != null ? Boolean.valueOf(str) : null;
    }

    /**
     * 使用标准JDK，只分析是否忽略大小写的"true", 为空时返回defaultValue
     */
    public static Boolean toBooleanObject(String str, Boolean defaultValue) {
        return str != null ? Boolean.valueOf(str) : defaultValue;
    }

    /**
     * 支持true/false,on/off, y/n, yes/no的转换, str为空或无法分析时返回null
     */
    public static Boolean parseGeneralString(String str) {
        return BooleanUtils.toBooleanObject(str);
    }

    /**
     * 支持true/false,on/off, y/n, yes/no的转换, str为空或无法分析时返回defaultValue
     */
    public static Boolean parseGeneralString(String str, Boolean defaultValue) {
        return BooleanUtils.toBooleanDefaultIfNull(BooleanUtils.toBooleanObject(str), defaultValue);
    }

    /**
     * 取反
     */
    public static boolean negate(final boolean bool) {
        return !bool;
    }

    /**
     * 取反
     */
    public static Boolean negate(final Boolean bool) {
        return BooleanUtils.negate(bool);
    }

    /**
     * 多个值的and
     */
    public static boolean and(final boolean... array) {
        return BooleanUtils.and(array);
    }

    /**
     * 多个值的or
     */
    public static boolean or(final boolean... array) {
        return BooleanUtils.or(array);
    }

    public static void main(String[] args) {

    }

}
