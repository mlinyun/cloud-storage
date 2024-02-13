package com.mlinyun.cloudstorage.operation.download.product;

import com.mlinyun.cloudstorage.operation.download.Downloader;
import com.mlinyun.cloudstorage.operation.download.domain.DownloadFile;
import com.mlinyun.cloudstorage.util.PathUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * 本地下载文件实现类
 */
@Slf4j
@Component
public class LocalStorageDownloader extends Downloader {
    /**
     * 文件下载抽象方法实现
     *
     * @param httpServletResponse 请求
     * @param downloadFile        本地下载文件实体类
     */
    @Override
    public void download(HttpServletResponse httpServletResponse, DownloadFile downloadFile) {
        BufferedInputStream bis = null;
        byte[] buffer = new byte[1024];
        // 设置文件路径
        File file = new File(PathUtil.getStaticPath() + downloadFile.getFileUrl());
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = httpServletResponse.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }

            } catch (Exception exception) {
                log.error(exception.getMessage());
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ioException) {
                        log.error(ioException.getMessage());
                    }
                }
            }
        }
    }
}
