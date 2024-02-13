package com.mlinyun.cloudstorage.operation.upload;

import com.mlinyun.cloudstorage.operation.upload.domain.UploadFile;
import com.mlinyun.cloudstorage.util.PathUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 文件上传抽象类接口
 */
@Slf4j
public abstract class Uploader {
    public static final String ROOT_PATH = "upload";
    public static final String FILE_SEPARATOR = File.separator;
    // 文件大小限制，单位 KB
    public final int maxSize = 10000000;

    /**
     * 文件上传抽象方法
     *
     * @param httpServletRequest 请求
     * @param uploadFile         本地文件上传实体类
     * @return 上传的文件信息
     */
    public abstract List<UploadFile> upload(HttpServletRequest httpServletRequest, UploadFile uploadFile);

    /**
     * 根据字符串创建本地目录 并按照日期建立子目录返回
     *
     * @return 文件保存路径
     */
    protected String getSaveFilePath() {
        String path = ROOT_PATH;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        path = FILE_SEPARATOR + path + FILE_SEPARATOR + simpleDateFormat.format(new Date());
        // 获取文件静态路径
        String staticPath = PathUtil.getStaticPath();
        String dirUrl = staticPath + path;
        if (PathUtil.createDirectory(dirUrl)) {
            return path;    // 如果文件创建成功，返回文件创建的路径
        } else {
            return ""; // 否则返回空
        }
    }

    /**
     * 依据原始文件名生成新文件名
     *
     * @return 生成的新文件名
     */
    protected String getTimeStampName() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            return "" + secureRandom.nextInt(10000) + System.currentTimeMillis();
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error("生成安全随机数失败: " + noSuchAlgorithmException);
        }
        return "" + System.currentTimeMillis();
    }

    /**
     * 检查文件上传状态
     *
     * @param param    本地上传文件实体类
     * @param confFile 文件
     * @return 上传结果 true-成功 false-失败
     * @throws IOException IO异常
     */
    public synchronized boolean checkUploadStatus(UploadFile param, File confFile) throws IOException {
        RandomAccessFile confAccessFile = new RandomAccessFile(confFile, "rw");
        // 设置文件长度
        confAccessFile.setLength(param.getTotalChunks());
        // 设置起始偏移量
        confAccessFile.seek(param.getChunkNumber() - 1);
        // 将指定的一个字节写入文件中 127
        confAccessFile.write(Byte.MAX_VALUE);
        byte[] completeStatusList = FileUtils.readFileToByteArray(confFile);
        confAccessFile.close();// 不关闭会造成无法占用
        // 创建 conf 文件文件长度为总分片数，每上传一个分块即向 conf 文件中写入一个 127，那么没上传的位置就是默认的 0，已上传的就是 127
        for (int i = 0; i < completeStatusList.length; i++) {
            if (completeStatusList[i] != Byte.MAX_VALUE) {
                return false;
            }
        }
        confFile.delete();
        return true;
    }

    /**
     * 获取不含扩展名的文件名
     *
     * @param fileName 文件名
     * @return 不含扩展名的文件名
     */
    protected String getFileName(String fileName) {
        if (!fileName.contains(".")) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

}
