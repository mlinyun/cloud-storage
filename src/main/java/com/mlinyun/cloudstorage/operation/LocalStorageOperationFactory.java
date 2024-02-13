package com.mlinyun.cloudstorage.operation;

import com.mlinyun.cloudstorage.operation.delete.Deleter;
import com.mlinyun.cloudstorage.operation.delete.product.LocalStorageDeleter;
import com.mlinyun.cloudstorage.operation.download.Downloader;
import com.mlinyun.cloudstorage.operation.download.product.LocalStorageDownloader;
import com.mlinyun.cloudstorage.operation.upload.Uploader;
import com.mlinyun.cloudstorage.operation.upload.product.LocalStorageUploader;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 文件操作具体工厂
 */
@Component
public class LocalStorageOperationFactory implements FileOperationFactory {

    /**
     * 本地上传文件实现类
     */
    @Resource
    LocalStorageUploader localStorageUploader;

    /**
     * 本地下载文件实现类
     */
    @Resource
    LocalStorageDownloader localStorageDownloader;

    /**
     * 本地删除文件实现类
     */
    @Resource
    LocalStorageDeleter localStorageDeleter;

    @Override
    public Uploader getUploader() {
        return localStorageUploader;
    }

    @Override
    public Downloader getDownloader() {
        return localStorageDownloader;
    }

    @Override
    public Deleter getDeleter() {
        return localStorageDeleter;
    }

}
