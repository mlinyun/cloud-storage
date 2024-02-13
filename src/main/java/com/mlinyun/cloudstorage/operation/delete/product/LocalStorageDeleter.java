package com.mlinyun.cloudstorage.operation.delete.product;

import com.mlinyun.cloudstorage.operation.delete.Deleter;
import com.mlinyun.cloudstorage.operation.delete.domain.DeleteFile;
import com.mlinyun.cloudstorage.util.FileUtil;
import com.mlinyun.cloudstorage.util.PathUtil;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 本地删除文件实现类
 */
@Component
public class LocalStorageDeleter extends Deleter {
    /**
     * 文件删除抽象方法实现
     *
     * @param deleteFile 删除文件实体类
     */
    @Override
    public void delete(DeleteFile deleteFile) {
        File file = new File(PathUtil.getStaticPath() + deleteFile.getFileUrl());
        if (file.exists()) {
            file.delete();
        }

        if (FileUtil.isImageFile(FileUtil.getFileExtendName(deleteFile.getFileUrl()))) {
            File minFile = new File(PathUtil.getStaticPath() + deleteFile.getFileUrl().replace(deleteFile.getTimeStampName(), deleteFile.getTimeStampName() + "_min"));
            if (minFile.exists()) {
                minFile.delete();
            }
        }
    }

}
