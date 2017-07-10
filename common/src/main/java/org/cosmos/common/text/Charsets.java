package org.cosmos.common.text;

import java.nio.charset.Charset;

/**
 * 常用的几种编码集
 * JDK7可直接使用StandardCharsets，也可以直接使用Guava Charsets.
 *
 * @author fisher
 */
public class Charsets {
    public static final String UTF_8_NAME = "UTF-8";
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset GBK = Charset.forName("GBK");
    public static final Charset GB2312= Charset.forName("GB2312");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

}
