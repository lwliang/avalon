/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file.util;

import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.FileUtils;
import com.avalon.core.util.StringUtils;
import com.avalon.file.enums.PathModeEnums;

import java.io.File;

public class PathUtil {

    public static final Integer FIRST_SIZE = 255;
    public static final Integer SECOND_SIZE = 255;

    public static Integer FirstRandom = 0;
    public static Integer SecondRandom = 0;

    public static String getPath(String prefix, PathModeEnums mode) {
        prefix += getFirst(mode) + File.separator + getSecond(mode);
        if (mode == PathModeEnums.random) {
            randomFileCount();
        }
        return prefix;
    }

    public static String getFirst(PathModeEnums mode) {
        if (mode == PathModeEnums.date) {
            return DateTimeUtils.getDateTimeFormat("YYYY", DateTimeUtils.getCurrentDate());
        }
        return StringUtils.prefixPadZero(Integer.toHexString(FirstRandom), 2);
    }

    public static String getSecond(PathModeEnums mode) {
        if (mode == PathModeEnums.date) {
            return DateTimeUtils.getDateTimeFormat("MM", DateTimeUtils.getCurrentDate());
        }
        return StringUtils.prefixPadZero(Integer.toHexString(SecondRandom), 2);
    }

    /**
     * 自动产生新的路径
     */
    private static void randomFileCount() {
        SecondRandom++;
        int first = SecondRandom / SECOND_SIZE;
        SecondRandom = SecondRandom % SECOND_SIZE;

        FirstRandom += first;
        FirstRandom = FirstRandom % FIRST_SIZE;
    }

    public static String getPath(PathModeEnums mode) {
        return getPath("", mode);
    }

    /**
     * 获取新的文件名称
     *
     * @param token
     * @param originName
     * @return
     */
    public static String getUUIDFileName(String token, String originName) {
        return token + "." + FileUtils.getFileExt(originName);
    }

    /**
     * 获取完整路径
     *
     * @param path
     * @param token
     * @param originName
     * @return
     */
    public static String getFullPath(String path, String token, String originName) {
        String uuidFileName = getUUIDFileName(token, originName);

        return path + File.separator + uuidFileName;
    }
}
