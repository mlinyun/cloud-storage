package com.mlinyun.cloudstorage.constant;

/**
 * 该类用来存放常量，初始化好文件分类的常量
 */
public class FileConstant {
    /**
     * 图像类
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
}
