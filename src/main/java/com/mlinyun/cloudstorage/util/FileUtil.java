package com.mlinyun.cloudstorage.util;

/**
 * 文件工具类
 */
public class FileUtil {

    /**
     * 图片类
     */
    public static final String[] IMG_FILE = {"bmp", "jpg", "png", "tif", "gif", "jpeg"};

    /**
     * 文档类
     */
    public static final String[] DOC_FILE = {"doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "hlp", "wps", "rtf", "html", "pdf"};

    /**
     * 视频类
     */
    public static final String[] VIDEO_FILE = {"avi", "mp4", "mpg", "mov", "swf"};

    /**
     * 音乐类
     */
    public static final String[] MUSIC_FILE = {"wav", "aif", "au", "mp3", "ram", "wma", "mmf", "amr", "aac", "flac"};

    /**
     * 图片类型值为 1
     */
    public static final int IMAGE_TYPE = 1;

    /**
     * 文档类型值为 2
     */
    public static final int DOC_TYPE = 2;

    /**
     * 视频类型值为 3
     */
    public static final int VIDEO_TYPE = 3;

    /**
     * 音乐类型值为 4
     */
    public static final int MUSIC_TYPE = 4;

    /**
     * 其他类型值为 5
     */
    public static final int OTHER_TYPE = 5;

    /**
     * 分享文件值为 6
     */
    public static final int SHARE_FILE = 6;

    /**
     * 恢复文件值为 7
     */
    public static final int RECYCLE_FILE = 7;

    /**
     * 判断是否为图片文件
     *
     * @param extendName 文件扩展名
     * @return 是否为图片文件
     */
    public static boolean isImageFile(String extendName) {
        for (int i = 0; i < IMG_FILE.length; i++) {
            if (extendName.equalsIgnoreCase(IMG_FILE[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件扩展名，如果没有扩展名，则返回空串
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    public static String getFileExtendName(String fileName) {
        if (fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取不包含扩展名的文件名
     *
     * @param fileName 文件名
     * @return 文件名（不带扩展名）
     */
    public static String getFileNameNotExtend(String fileName) {
        // 获取文件的扩展名
        String fileType = getFileExtendName(fileName);
        // 将文件名中的 ("." + 文件扩展名) 替换为空 ""
        return fileName.replace("." + fileType, "");
    }

}
