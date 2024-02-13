package com.mlinyun.cloudstorage.exception;

/**
 * 文件上传异常
 */
public class UploadException extends RuntimeException {

    public UploadException(Throwable throwable) {
        super("文件上传出现了异常", throwable);
    }

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
