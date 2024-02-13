package com.mlinyun.cloudstorage.operation.download;

import com.mlinyun.cloudstorage.operation.download.domain.DownloadFile;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 文件下载抽象类接口
 */
public abstract class Downloader {
    /**
     * 文件下载抽象方法
     *
     * @param httpServletResponse 请求
     * @param downloadFile        本地下载文件实体类
     */
    public abstract void download(HttpServletResponse httpServletResponse, DownloadFile downloadFile);

}
