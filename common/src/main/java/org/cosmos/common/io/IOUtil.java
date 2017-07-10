package org.cosmos.common.io;

import org.cosmos.common.io.type.StringBuilderWriter;
import org.cosmos.common.text.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * IO Stream/Reader相关工具集
 * <p>
 * Apache Commmons IO中化简移植,其他功能建议使用Apache Commons IO，固定encoding为UTF8.
 * <p>
 * 1. 安静关闭Closeable对象
 * <p>
 * 2. 读出InputStream/Reader内容到String 或 List<String>(from Commons IO)
 * <p>
 * 3. 将String写到OutputStream/Writer(from Commons IO)
 * <p>
 * 4. InputStream/Reader与OutputStream/Writer之间复制的copy(from Commons IO)
 *
 */
public class IOUtil {
    private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int EOF = -1;

    private static final String CLOSE_ERROR_MESSAGE = "IOException thrown while closing Closeable.";

    /**
     * 在final中安静的关闭, 不再往外抛出异常避免影响原有异常，最常用函数. 同时兼容Closeable为空未实际创建的情况.
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.warn(CLOSE_ERROR_MESSAGE, e);
            }
        }
    }

    /**
     * 简单读取InputStream到String.
     */
    public static String toString(InputStream input,Charset charset) throws IOException {
        InputStreamReader reader = new InputStreamReader(input, charset);
        return toString(reader);
    }

    /**
     * 简单读取Reader到String
     */
    public static String toString(Reader input) throws IOException {
        final BufferedReader reader = toBufferedReader(input);
        StringBuilderWriter sw = new StringBuilderWriter();
        copy(reader, sw);
        return sw.toString();
    }

    /**
     * 简单读取Reader的每行内容到List<String>
     */
    public static List<String> toLines(final InputStream input) throws IOException {
        return toLines(new InputStreamReader(input, Charsets.UTF_8));
    }

    /**
     * 简单读取Reader的每行内容到List<String>
     */
    public static List<String> toLines(final Reader input) throws IOException {
        final BufferedReader reader = toBufferedReader(input);
        final List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    /**
     * 简单写入String到OutputStream.
     */
    public static void write(final String data, final OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.getBytes(Charsets.UTF_8));
        }
    }

    /**
     * 简单写入String到Writer.
     */
    public static void write(final String data, final Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * 在Reader与Writer间复制内容
     */
    public static long copy(final Reader input, final Writer output) throws IOException {
        final char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 在InputStream与OutputStream间复制内容
     */
    public static long copy(final InputStream input, final OutputStream output) throws IOException {

        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static boolean compare(InputStream input1, InputStream input2) throws IOException {
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }
        int ch = input1.read();
        while (ch != -1) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }
        int ch2 = input2.read();
        return (ch2 == -1);
    }
}
