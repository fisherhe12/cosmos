package org.cosmos.common.io;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.cosmos.common.base.StringPool;
import org.cosmos.common.base.annatation.NotNull;
import org.cosmos.common.base.annatation.Nullable;
import org.cosmos.common.security.EncodeUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

/**
 * 关于文件的工具集
 * <p>
 * 1.文件读写
 * <p>
 * 2.文件及目录操作
 * <p>
 * 3.文件消息摘要
 */
public class FileUtil {

    /**
     * 比较两个文件是否相等
     */
    public static boolean equals(String file1, String file2) {
        return equals(new File(file1), new File(file2));
    }

    /**
     * 比较两个文件是否相等
     */
    public static boolean equals(File file1, File file2) {
        try {
            file1 = file1.getCanonicalFile();
            file2 = file2.getCanonicalFile();
        } catch (IOException ignore) {
            return false;
        }
        return file1.equals(file2);
    }

    //////// 文件读写//////

    /**
     * 读取文件到byte[].
     */
    public static byte[] toByteArray(final File file) throws IOException {
        return Files.toByteArray(file);
    }

    /**
     * 读取文件到String.
     */
    public static String toString(final File file) throws IOException {
        return Files.toString(file, Charsets.UTF_8);
    }

    /**
     * 读取文件到String.
     */
    public static String toString(final File file, final Charset charset) throws IOException {
        return Files.toString(file, charset);
    }

    /**
     * 读取文件的每行内容到List<String>
     */
    public static List<String> toLines(final File file) throws IOException {
        return Files.readLines(file, Charsets.UTF_8);
    }

    /**
     * 简单写入String到File.
     */
    public static void write(final CharSequence data, final File file) throws IOException {
        Files.write(data, file, Charsets.UTF_8);
    }

    /**
     * 追加String到File.
     */
    public static void append(final CharSequence from, final File to) throws IOException {
        Files.append(from, to, Charsets.UTF_8);
    }

    /**
     * 打开文件为InputStream
     */
    public static InputStream asInputStream(String fileName) throws IOException {
        return new FileInputStream(getFileByPath(fileName));
    }

    /**
     * 打开文件为InputStream
     */
    public static InputStream asInputStream(File file) throws IOException {
        return new FileInputStream(file);
    }

    /**
     * 打开文件为OutputStream
     */
    public static OutputStream asOututStream(String fileName) throws IOException {
        return new FileOutputStream(getFileByPath(fileName));
    }

    /**
     * 打开文件为OutputStream
     */
    public static OutputStream asOututStream(File file) throws IOException {
        return new FileOutputStream(file);
    }

    /**
     * 获取File的BufferedReader
     */
    public static BufferedReader asBufferedReader(String fileName) throws FileNotFoundException {
        return Files.newReader(getFileByPath(fileName), Charsets.UTF_8);
    }

    /**
     * 获取File的BufferedWriter
     */
    public static BufferedWriter asBufferedWriter(String fileName) throws FileNotFoundException {
        return Files.newWriter(getFileByPath(fileName), Charsets.UTF_8);
    }


    // 对比文件内容是否相等
    public static boolean compare(String file1, String file2) throws IOException {
        return compare(new File(file1), new File(file2));
    }


    public static boolean compare(File file1, File file2) throws IOException {
        boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }

        if (!file1Exists) {
            return true;
        }

        if ((!file1.isFile()) || (!file2.isFile())) {
            throw new IOException("Only files can be compared");
        }

        if (file1.length() != file2.length()) {
            return false;
        }

        if (equals(file1, file2)) {
            return true;
        }

        InputStream input1 = null;
        InputStream input2 = null;
        try {
            input1 = new FileInputStream(file1);
            input2 = new FileInputStream(file2);
            return IOUtil.compare(input1, input2);
        } finally {
            IOUtil.closeQuietly(input1);
            IOUtil.closeQuietly(input2);
        }
    }

    ////// 判断文件创建的时间状态 ////

    public static boolean isNewer(String file, String reference) {
        return isNewer(new File(file), new File(reference));
    }


    public static boolean isNewer(File file, File reference) {
        if (!reference.exists()) {
            throw new IllegalArgumentException("Reference file not found: " + reference);
        }
        return isNewer(file, reference.lastModified());
    }


    public static boolean isOlder(String file, String reference) {
        return isOlder(new File(file), new File(reference));
    }

    public static boolean isOlder(File file, File reference) {
        if (!reference.exists()) {
            throw new IllegalArgumentException("Reference file not found: " + reference);
        }
        return isOlder(file, reference.lastModified());
    }


    public static boolean isNewer(File file, long timeMillis) {
        if (!file.exists()) {
            return false;
        }
        return file.lastModified() > timeMillis;
    }

    public static boolean isNewer(String file, long timeMillis) {
        return isNewer(new File(file), timeMillis);
    }


    public static boolean isOlder(File file, long timeMillis) {
        if (!file.exists()) {
            return false;
        }
        return file.lastModified() < timeMillis;
    }

    public static boolean isOlder(String file, long timeMillis) {
        return isOlder(new File(file), timeMillis);
    }


    ///// 文件操作 /////

    /**
     * 复制文件或目录
     *
     * @param from 如果为null，或者是不存在的文件或目录，抛出异常.
     * @param to   如果为null，或者from是目录而to是已存在文件，或相反
     */
    public static void copy(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);

        if (from.isDirectory()) {
            copyDir(from, to);
        } else {
            copyFile(from, to);
        }
    }

    /**
     * 文件复制.
     *
     * @param from 如果为nll，或文件不存在或者是目录，，抛出异常
     * @param to   如果to为null，或文件存在但是一个目录，抛出异常
     */
    public static void copyFile(@NotNull File from, @NotNull File to) throws IOException {
        Validate.isTrue(isFileExists(from), from + " is not exist or not a file");
        Validate.notNull(to);
        Validate.isTrue(!FileUtil.isDirExists(to), to + " is exist but it is a dir");
        Files.copy(from, to);
    }

    /**
     * 复制目录
     */
    public static void copyDir(@NotNull File from, @NotNull File to) throws IOException {
        Validate.isTrue(isDirExists(from), from + " is not exist or not a dir");
        Validate.notNull(to);

        if (to.exists()) {
            Validate.isTrue(!to.isFile(), to + " is exist but it is a file");
        } else {
            to.mkdirs();
        }

        File[] files = from.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (StringPool.DOT.equals(name) || StringPool.DOTDOT.equals(name)) {
                    continue;
                }
                copy(files[i], new File(to, name));
            }
        }
    }

    /**
     * 文件移动/重命名.
     */
    public static void moveFile(@NotNull File from, @NotNull File to) throws IOException {
        Validate.isTrue(isFileExists(from), from + " is not exist or not a file");
        Validate.notNull(to);
        Validate.isTrue(!isDirExists(to), to + " is  exist but it is a dir");

        Files.move(from, to);
    }

    /**
     * 目录移动/重命名
     */
    public static void moveDir(@NotNull File from, @NotNull File to) throws IOException {
        Validate.isTrue(isDirExists(from), from + " is not exist or not a dir");
        Validate.notNull(to);
        Validate.isTrue(!isFileExists(to), to + " is exist but it is a file");

        final boolean rename = from.renameTo(to);
        if (!rename) {
            if (to.getCanonicalPath().startsWith(from.getCanonicalPath() + File.separator)) {
                throw new IOException("Cannot move directory: " + from + " to a subdirectory of itself: " + to);
            }
            copyDir(from, to);
            deleteDir(from);
            if (from.exists()) {
                throw new IOException("Failed to delete original directory '" + from + "' after copy to '" + to + '\'');
            }
        }
    }

    /**
     * 创建文件或更新时间戳.
     */
    public static void touch(String filePath) throws IOException {
        Files.touch(getFileByPath(filePath));
    }

    /**
     * 创建文件或更新时间戳.
     */
    public static void touch(File file) throws IOException {
        Files.touch(file);
    }

    /**
     * 删除文件.
     * <p>
     * 如果文件存在进行删除
     */
    public static void deleteFile(@Nullable File file) {
        if (isFileExists(file)) {
            file.delete();
        }
    }

    /**
     * 删除目录及所有子目录/文件
     */
    public static void deleteDir(File dir) {
        if (isDirExists(dir)) {
            // 后序遍历，先删掉子目录中的文件/目录
            Iterator<File> iterator = Files.fileTreeTraverser().postOrderTraversal(dir).iterator();
            while (iterator.hasNext()) {
                iterator.next().delete();
            }
        }
    }

    // 文件及文件夹创建


    public static void mkdirs(String dirs) throws IOException {
        mkdirs(new File(dirs));
    }

    /**
     * Creates all folders at once.
     */
    public static void mkdirs(File dirs) throws IOException {
        if (dirs.exists()) {
            if (!dirs.isDirectory()) {
                throw new IOException("Not a directory: " + dirs);
            }
            return;
        }
        if (!dirs.mkdirs()) {
            throw new IOException("Can't create:" + dirs);
        }
    }


    public static void mkdir(String dir) throws IOException {
        mkdir(new File(dir));
    }


    public static void mkdir(File dir) throws IOException {
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new IOException("Not a directory:" + dir);
            }
            return;
        }
        if (!dir.mkdir()) {
            throw new IOException("Can't create:" + dir);
        }
    }

    /**
     * 确保父目录及其父目录直到根目录都已经创建.
     *
     * @see Files#createParentDirs(File)
     */
    public static void mkParentDirs(File file) throws IOException {
        Files.createParentDirs(file);
    }

    /**
     * 获取上级文件
     */
    public static File getParentFile(final File file) {
        int skipCount = 0;
        File parentFile = file;
        while (true) {
            parentFile = parentFile.getParentFile();
            if (parentFile == null) {
                return null;
            }
            if (StringPool.DOT.equals(parentFile.getName())) {
                continue;
            }
            if (StringPool.DOTDOT.equals(parentFile.getName())) {
                skipCount++;
                continue;
            }
            if (skipCount > 0) {
                skipCount--;
                continue;
            }
            return parentFile;
        }
    }

    public static boolean isFilePathAcceptable(File file, FileFilter fileFilter) {
        do {
            if (fileFilter != null && !fileFilter.accept(file)) {
                return false;
            }
            file = file.getParentFile();
        } while (file != null);
        return true;
    }

    /**
     * 判断目录是否存在
     */
    public static boolean isDirExists(String dirPath) {
        return isDirExists(getFileByPath(dirPath));
    }

    /**
     * 判断目录是否存在
     */
    public static boolean isDirExists(File dir) {
        return dir != null && dir.exists() && dir.isDirectory();
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isFileExists(String fileName) {
        return isFileExists(getFileByPath(fileName));
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 在临时目录创建临时目录，命名为${毫秒级时间戳}-${同一毫秒内的计数器}, from guava
     *
     * @see Files#createTempDir()
     */
    public static File createTempDir() {
        return Files.createTempDir();
    }

    /**
     * 在临时目录创建临时文件，命名为tmp-${random.nextLong()}.tmp
     */
    public static File createTempFile() throws IOException {
        return File.createTempFile("tmp-", ".tmp");
    }

    /**
     * 在临时目录创建临时文件，命名为${prefix}${random.nextLong()}${suffix}
     */
    public static File createTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix);
    }

    private static File getFileByPath(String filePath) {
        return StringUtils.isBlank(filePath) ? null : new File(filePath);
    }

    // ---------------------------------------------------------------- digests

    /**
     * 对文件进行不同算法的消息摘要
     */
    public static byte[] digest(final File file, MessageDigest algorithm) throws IOException {
        algorithm.reset();
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DigestInputStream dis = new DigestInputStream(bis, algorithm);

        try {
            while (dis.read() != -1) {
            }
        } finally {
            IOUtil.closeQuietly(fis);
        }

        return algorithm.digest();
    }

    /**
     * MD5
     */
    public static String md5(final File file) throws IOException {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignore) {
        }

        byte[] digest = digest(file, md5Digest);

        return EncodeUtil.encodeHex(digest);
    }

    /**
     * SHA1
     */
    public static String sha(final File file) throws IOException {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ignore) {
        }

        byte[] digest = digest(file, md5Digest);

        return EncodeUtil.encodeHex(digest);
    }

    /**
     * SHA256
     */
    public static String sha256(final File file) throws IOException {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ignore) {
        }

        byte[] digest = digest(file, md5Digest);

        return EncodeUtil.encodeHex(digest);
    }

}
