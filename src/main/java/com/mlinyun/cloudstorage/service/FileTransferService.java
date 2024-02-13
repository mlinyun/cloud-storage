package com.mlinyun.cloudstorage.service;

import com.mlinyun.cloudstorage.dto.DownloadFileDTO;
import com.mlinyun.cloudstorage.dto.UploadFileDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 文件传输服务接口
 */
public interface FileTransferService {

    /**
     * 文件上传服务
     *
     * @param request       请求
     * @param uploadFileDto 前台上传文件接口请求参数
     * @param userId        用户ID
     */
    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, Long userId);

    /**
     * 文件下载服务
     *
     * @param httpServletResponse 请求
     * @param downloadFileDTO     前台下载文件接口请求参数
     */
    void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO);

}
