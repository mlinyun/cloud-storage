package com.mlinyun.cloudstorage.exception;

/**
 * 文件 md5 校验失败异常
 */
public class NotSameFileException extends Exception {

    public NotSameFileException() {
        super("File MD5 Different");
    }

}
