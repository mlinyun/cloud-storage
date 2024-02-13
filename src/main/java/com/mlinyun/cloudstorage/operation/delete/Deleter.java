package com.mlinyun.cloudstorage.operation.delete;

import com.mlinyun.cloudstorage.operation.delete.domain.DeleteFile;

/**
 * 文件删除抽象类接口
 */
public abstract class Deleter {

    /**
     * 文件删除抽象方法
     *
     * @param deleteFile 删除文件实体类
     */
    public abstract void delete(DeleteFile deleteFile);

}
