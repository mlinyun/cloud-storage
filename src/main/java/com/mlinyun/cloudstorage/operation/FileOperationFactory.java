package com.mlinyun.cloudstorage.operation;

import com.mlinyun.cloudstorage.operation.delete.Deleter;
import com.mlinyun.cloudstorage.operation.download.Downloader;
import com.mlinyun.cloudstorage.operation.upload.Uploader;

/**
 * 文件操作抽象工厂
 */
public interface FileOperationFactory {

    /**
     * 文件上传抽象类接口
     */
    Uploader getUploader();

    /**
     * 文件下载抽象类接口
     */
    Downloader getDownloader();

    /**
     * 文件删除抽象类接口
     */
    Deleter getDeleter();

}
