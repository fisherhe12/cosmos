package org.cosmos.common.io;

import org.cosmos.common.base.Emptys;
import org.cosmos.common.base.Platforms;

import java.io.File;

/**
 * 文件名工具类
 * 从Jodd部分移植
 */
public class FileNameUtil {

    /**
     * The extension separator character.
     */
    private static final char EXTENSION_SEPARATOR = '.';
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = Platforms.LINUX_FILE_PATH_SEPARATOR_CHAR;

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = Platforms.WINDOWS_FILE_PATH_SEPARATOR_CHAR;

    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = Platforms.FILE_PATH_SEPARATOR_CHAR;


    /**
     * Checks if the character is a separator.
     */
    private static boolean isSeparator(char ch) {
        return (ch == UNIX_SEPARATOR) || (ch == WINDOWS_SEPARATOR);
    }

    // ---------------------------------------------------------------- prefix

    /**
     * Returns the length of the filename prefix, such as <code>C:/</code> or <code>~/</code>.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * <p>
     * The prefix length includes the first slash in the full filename
     * if applicable. Thus, it is possible that the length returned is greater
     * than the length of the input string.
     * <pre>{@code
     * Windows:
     * a\b\c.txt           --> ""          --> relative
     * \a\b\c.txt          --> "\"         --> current drive absolute
     * C:a\b\c.txt         --> "C:"        --> drive relative
     * C:\a\b\c.txt        --> "C:\"       --> absolute
     * \\server\a\b\c.txt  --> "\\server\" --> UNC
     *
     * Unix:
     * a/b/c.txt           --> ""          --> relative
     * /a/b/c.txt          --> "/"         --> absolute
     * ~/a/b/c.txt         --> "~/"        --> current user
     * ~                   --> "~/"        --> current user (slash added)
     * ~user/a/b/c.txt     --> "~user/"    --> named user
     * ~user               --> "~user/"    --> named user (slash added)
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * ie. both Unix and Windows prefixes are matched regardless.
     *
     * @param filename the filename to find the prefix in, null returns -1
     * @return the length of the prefix, -1 if invalid or null
     */
    public static int getPrefixLength(String filename) {
        if (filename == null) {
            return -1;
        }
        int len = filename.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = filename.charAt(0);
        if (ch0 == ':') {
            return -1;
        }
        if (len == 1) {
            if (ch0 == '~') {
                return 2;  // return a length greater than the input
            }
            return (isSeparator(ch0) ? 1 : 0);
        } else {
            if (ch0 == '~') {
                int posUnix = filename.indexOf(UNIX_SEPARATOR, 1);
                int posWin = filename.indexOf(WINDOWS_SEPARATOR, 1);
                if (posUnix == -1 && posWin == -1) {
                    return len + 1;  // return a length greater than the input
                }
                posUnix = (posUnix == -1 ? posWin : posUnix);
                posWin = (posWin == -1 ? posUnix : posWin);
                return Math.min(posUnix, posWin) + 1;
            }
            char ch1 = filename.charAt(1);
            if (ch1 == ':') {
                ch0 = Character.toUpperCase(ch0);
                if (ch0 >= 'A' && ch0 <= 'Z') {
                    if (len == 2 || !isSeparator(filename.charAt(2))) {
                        return 2;
                    }
                    return 3;
                }
                return -1;

            } else if (isSeparator(ch0) && isSeparator(ch1)) {
                int posUnix = filename.indexOf(UNIX_SEPARATOR, 2);
                int posWin = filename.indexOf(WINDOWS_SEPARATOR, 2);
                if ((posUnix == -1 && posWin == -1) || posUnix == 2 || posWin == 2) {
                    return -1;
                }
                posUnix = (posUnix == -1 ? posWin : posUnix);
                posWin = (posWin == -1 ? posUnix : posWin);
                return Math.min(posUnix, posWin) + 1;
            } else {
                return (isSeparator(ch0) ? 1 : 0);
            }
        }
    }

    /**
     * Returns the index of the last directory separator character.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The position of the last forward or backslash is returned.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there is no such character
     */
    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * Returns the index of the last extension separator character, which is a dot.
     * <p>
     * This method also checks that there is no directory separator after the last dot.
     * To do this it uses {@link #indexOfLastSeparator(String)} which will
     * handle a file in either Unix or Windows format.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     * is no such character
     */
    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        int lastSeparator = indexOfLastSeparator(filename);
        return (lastSeparator > extensionPos ? -1 : extensionPos);
    }

    /**
     * Returns <code>true</code> if file has extension.
     */
    public static boolean hasExtension(String filename) {
        return indexOfExtension(filename) != -1;
    }

    // ---------------------------------------------------------------- get

    /**
     * Gets the prefix from a full filename, such as <code>C:/</code>
     * or <code>~/</code>.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The prefix includes the first slash in the full filename where applicable.
     * <pre>{@code
     * Windows:
     * a\b\c.txt           --> ""          --> relative
     * \a\b\c.txt          --> "\"         --> current drive absolute
     * C:a\b\c.txt         --> "C:"        --> drive relative
     * C:\a\b\c.txt        --> "C:\"       --> absolute
     * \\server\a\b\c.txt  --> "\\server\" --> UNC
     *
     * Unix:
     * a/b/c.txt           --> ""          --> relative
     * /a/b/c.txt          --> "/"         --> absolute
     * ~/a/b/c.txt         --> "~/"        --> current user
     * ~                   --> "~/"        --> current user (slash added)
     * ~user/a/b/c.txt     --> "~user/"    --> named user
     * ~user               --> "~user/"    --> named user (slash added)
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * ie. both Unix and Windows prefixes are matched regardless.
     *
     * @param filename the filename to query, null returns null
     * @return the prefix of the file, null if invalid
     */
    public static String getPrefix(String filename) {
        if (filename == null) {
            return null;
        }
        int len = getPrefixLength(filename);
        if (len < 0) {
            return null;
        }
        if (len > filename.length()) {
            return filename + UNIX_SEPARATOR;  // we know this only happens for unix
        }
        return filename.substring(0, len);
    }

    /**
     * Gets the path from a full filename, which excludes the prefix.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before and
     * including the last forward or backslash.
     * <pre>{@code
     * C:\a\b\c.txt --> a\b\
     * ~/a/b/c.txt  --> a/b/
     * a.txt        --> ""
     * a/b/c        --> a/b/
     * a/b/c/       --> a/b/c/
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * <p>
     * This method drops the prefix from the result.
     * See {@link #getFullPath(String)} for the method that retains the prefix.
     *
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getPath(String filename) {
        return doGetPath(filename, 1);
    }

    /**
     * Gets the path from a full filename, which excludes the prefix, and
     * also excluding the final directory separator.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before the
     * last forward or backslash.
     * <pre>{@code
     * C:\a\b\c.txt --> a\b
     * ~/a/b/c.txt  --> a/b
     * a.txt        --> ""
     * a/b/c        --> a/b
     * a/b/c/       --> a/b/c
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * <p>
     * This method drops the prefix from the result.
     * See {@link #getFullPathNoEndSeparator(String)} for the method that retains the prefix.
     *
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getPathNoEndSeparator(String filename) {
        return doGetPath(filename, 0);
    }

    /**
     * Does the work of getting the path.
     *
     * @param filename     the filename
     * @param separatorAdd 0 to omit the end separator, 1 to return it
     * @return the path
     */
    private static String doGetPath(String filename, int separatorAdd) {
        if (filename == null) {
            return null;
        }
        int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        int index = indexOfLastSeparator(filename);
        int endIndex = index + separatorAdd;
        if (prefix >= filename.length() || index < 0 || prefix >= endIndex) {
            return Emptys.EMPTY_STRING;
        }
        return filename.substring(prefix, endIndex);
    }

    /**
     * Gets the full path from a full filename, which is the prefix + path.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before and
     * including the last forward or backslash.
     * <pre>{@code
     * C:\a\b\c.txt --> C:\a\b\
     * ~/a/b/c.txt  --> ~/a/b/
     * a.txt        --> ""
     * a/b/c        --> a/b/
     * a/b/c/       --> a/b/c/
     * C:           --> C:
     * C:\          --> C:\
     * ~            --> ~/
     * ~/           --> ~/
     * ~user        --> ~user/
     * ~user/       --> ~user/
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getFullPath(String filename) {
        return doGetFullPath(filename, true);
    }

    /**
     * Gets the full path from a full filename, which is the prefix + path,
     * and also excluding the final directory separator.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before the
     * last forward or backslash.
     * <pre>{@code
     * C:\a\b\c.txt --> C:\a\b
     * ~/a/b/c.txt  --> ~/a/b
     * a.txt        --> ""
     * a/b/c        --> a/b
     * a/b/c/       --> a/b/c
     * C:           --> C:
     * C:\          --> C:\
     * ~            --> ~
     * ~/           --> ~
     * ~user        --> ~user
     * ~user/       --> ~user
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getFullPathNoEndSeparator(String filename) {
        return doGetFullPath(filename, false);
    }

    /**
     * Does the work of getting the path.
     *
     * @param filename         the filename
     * @param includeSeparator true to include the end separator
     * @return the path
     */
    private static String doGetFullPath(String filename, boolean includeSeparator) {
        if (filename == null) {
            return null;
        }
        int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        if (prefix >= filename.length()) {
            if (includeSeparator) {
                return getPrefix(filename);  // add end slash if necessary
            } else {
                return filename;
            }
        }
        int index = indexOfLastSeparator(filename);
        if (index < 0) {
            return filename.substring(0, prefix);
        }
        int end = index + (includeSeparator ? 1 : 0);
        if (end == 0) {
            end++;
        }
        return filename.substring(0, end);
    }

    /**
     * Gets the name minus the path from a full filename.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The text after the last forward or backslash is returned.
     * <pre>{@code
     * a/b/c.txt --> c.txt
     * a.txt     --> a.txt
     * a/b/c     --> c
     * a/b/c/    --> ""
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     */
    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    /**
     * Gets the base name, minus the full path and extension, from a full filename.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The text after the last forward or backslash and before the last dot is returned.
     * <pre>{@code
     * a/b/c.txt --> c
     * a.txt     --> a
     * a/b/c     --> c
     * a/b/c/    --> ""
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     */
    public static String getBaseName(String filename) {
        return removeExtension(getName(filename));
    }

    /**
     * Gets the extension of a filename.
     * <p>
     * This method returns the textual part of the filename after the last dot.
     * There must be no directory separator after the dot.
     * <pre>{@code
     * foo.txt      --> "txt"
     * a/b/c.jpg    --> "jpg"
     * a/b.txt/c    --> ""
     * a/b/c        --> ""
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists.
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return Emptys.EMPTY_STRING;
        } else {
            return filename.substring(index + 1);
        }
    }

    //----------------------------------------------------------------------- remove

    /**
     * Removes the extension from a filename.
     * <p>
     * This method returns the textual part of the filename before the last dot.
     * There must be no directory separator after the dot.
     * <pre>{@code
     * foo.txt    --> foo
     * a\b\c.jpg  --> a\b\c
     * a\b\c      --> a\b\c
     * a.b\c      --> a.b\c
     * }</pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the filename minus the extension
     */
    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }

    // ---------------------------------------------------------------- equals

    /**
     * Checks whether two filenames are equal exactly.
     */
    public static boolean equals(String filename1, String filename2) {
        return equals(filename1, filename2, false);
    }

    /**
     * Checks whether two filenames are equal using the case rules of the system.
     */
    public static boolean equalsOnSystem(String filename1, String filename2) {
        return equals(filename1, filename2, true);
    }

    /**
     * Checks whether two filenames are equal optionally using the case rules of the system.
     * <p>
     *
     * @param filename1 the first filename to query, may be null
     * @param filename2 the second filename to query, may be null
     * @param system    whether to use the system (windows or unix)
     * @return true if the filenames are equal, null equals null
     */
    private static boolean equals(String filename1, String filename2, boolean system) {
        //noinspection StringEquality
        if (filename1 == filename2) {
            return true;
        }
        if (filename1 == null || filename2 == null) {
            return false;
        }
        if (system && (SYSTEM_SEPARATOR == WINDOWS_SEPARATOR)) {
            return filename1.equalsIgnoreCase(filename2);
        } else {
            return filename1.equals(filename2);
        }
    }

    // ---------------------------------------------------------------- split

    /**
     * Splits filename into a array of four Strings containing prefix, path, basename and extension.
     * Path will contain ending separator.
     */
    public static String[] split(String filename) {
        String prefix = getPrefix(filename);
        if (prefix == null) {
            prefix = Emptys.EMPTY_STRING;
        }
        int lastSeparatorIndex = indexOfLastSeparator(filename);
        int lastExtensionIndex = indexOfExtension(filename);

        String path;
        String baseName;
        String extension;

        if (lastSeparatorIndex == -1) {
            path = Emptys.EMPTY_STRING;
            if (lastExtensionIndex == -1) {
                baseName = filename.substring(prefix.length());
                extension = Emptys.EMPTY_STRING;
            } else {
                baseName = filename.substring(prefix.length(), lastExtensionIndex);
                extension = filename.substring(lastExtensionIndex + 1);
            }
        } else {
            path = filename.substring(prefix.length(), lastSeparatorIndex + 1);
            if (lastExtensionIndex == -1) {
                baseName = filename.substring(prefix.length() + path.length());
                extension = Emptys.EMPTY_STRING;
            } else {
                baseName = filename.substring(prefix.length() + path.length(), lastExtensionIndex);
                extension = filename.substring(lastExtensionIndex + 1);
            }
        }
        return new String[] {prefix, path, baseName, extension};
    }

    // ---------------------------------------------------------------- home

    /**
     * Resolve <code>~</code> in the path.
     */
    public static String resolveHome(String path) {
        if (path.length() == 1) {
            if (path.charAt(0) == '~') {
                return Platforms.USER_HOME;
            }
            return path;
        }
        if (path.length() >= 2) {
            if ((path.charAt(0) == '~') && (path.charAt(1) == File.separatorChar)) {
                return Platforms.USER_HOME + path.substring(1);
            }
        }
        return path;
    }

    /**
     * Calculates relative path of target path on base path.
     */
    public static String relativePath(String targetPath, String basePath) {
        return new File(basePath).toPath().relativize(new File(targetPath).toPath()).toString();
    }

}
