package com.mlinyun.cloudstorage.operation.upload.product;

import com.mlinyun.cloudstorage.exception.NotSameFileException;
import com.mlinyun.cloudstorage.exception.UploadException;
import com.mlinyun.cloudstorage.operation.upload.Uploader;
import com.mlinyun.cloudstorage.operation.upload.domain.UploadFile;
import com.mlinyun.cloudstorage.util.FileUtil;
import com.mlinyun.cloudstorage.util.PathUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * 本地上传文件实现类
 */
@Slf4j
@Component
public class LocalStorageUploader extends Uploader {

    @Resource
    private MultipartResolver multipartResolver;

    /**
     * 空参构造函数
     */
    public LocalStorageUploader() {
    }

    /**
     * 文件上传抽象方法实现
     *
     * @param httpServletRequest 请求
     * @param uploadFile         本地文件上传实体类
     * @return 上传的文件信息
     */
    @Override
    public List<UploadFile> upload(HttpServletRequest httpServletRequest, UploadFile uploadFile) {
        List<UploadFile> saveUploadFileList = new ArrayList<>();
        // StandardMultipartHttpServletRequest是 HttpServletRequest 的一个实现，专门用于处理多部分请求，例如文件上传
        StandardMultipartHttpServletRequest standardMultipartHttpServletRequest = (StandardMultipartHttpServletRequest) httpServletRequest;
        // 判断请求是否为多部分请求
        boolean isMultipart = multipartResolver.isMultipart(standardMultipartHttpServletRequest);
        // 如果不是不是不多部分请求，抛出异常
        if (!isMultipart) {
            throw new UploadException("未包含文件上传域");
        }
        // 获取文件保存路径
        String savePath = getSaveFilePath();
        try {
            Iterator<String> iter = standardMultipartHttpServletRequest.getFileNames();
            while (iter.hasNext()) {
                saveUploadFileList = doUpload(standardMultipartHttpServletRequest, savePath, iter, uploadFile);
            }
        } catch (IOException e) {
            throw new UploadException("未包含文件上传域");
        } catch (NotSameFileException notSameFileException) {
            log.error(notSameFileException.getMessage());
        }
        return saveUploadFileList;
    }

    /**
     * 文件上传方法
     *
     * @param standardMultipartHttpServletRequest 请求
     * @param savePath                            文件保存路径
     * @param iter                                迭代器
     * @param uploadFile                          文件保存路径
     * @return 文件上传列表
     * @throws IOException          IO 异常
     * @throws NotSameFileException 文件 md5 校验失败异常
     */
    private List<UploadFile> doUpload(StandardMultipartHttpServletRequest standardMultipartHttpServletRequest, String savePath, Iterator<String> iter, UploadFile uploadFile) throws IOException, NotSameFileException {
        List<UploadFile> saveUploadFileList = new ArrayList<>();
        MultipartFile multipartfile = standardMultipartHttpServletRequest.getFile(iter.next());

        String timeStampName = uploadFile.getIdentifier();

        String originalName = multipartfile.getOriginalFilename();

        String fileName = getFileName(originalName);
        String fileType = FileUtil.getFileExtendName(originalName);
        uploadFile.setFileName(fileName);
        uploadFile.setFileType(fileType);
        uploadFile.setTimeStampName(timeStampName);

        String saveFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + fileType;
        String tempFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + fileType + "_tmp";
        String minFilePath = savePath + FILE_SEPARATOR + timeStampName + "_min" + "." + fileType;
        String confFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + "conf";
        File file = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + saveFilePath);
        File tempFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + tempFilePath);
        File minFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + minFilePath);
        File confFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + confFilePath);
        // uploadFile.setIsOSS(0);
        // uploadFile.setStorageType(0);
        uploadFile.setUrl(saveFilePath);

        if (StringUtils.isEmpty(uploadFile.getTaskId())) {
            uploadFile.setTaskId(UUID.randomUUID().toString());
        }

        // 第一步 打开将要写入的文件
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        // 第二步 打开通道
        FileChannel fileChannel = raf.getChannel();
        // 第三步 计算偏移量
        long position = (uploadFile.getChunkNumber() - 1) * uploadFile.getChunkSize();
        // 第四步 获取分片数据
        byte[] fileData = multipartfile.getBytes();
        // 第五步 写入数据
        fileChannel.position(position);
        fileChannel.write(ByteBuffer.wrap(fileData));
        fileChannel.force(true);
        fileChannel.close();
        raf.close();
        // 判断是否完成文件的传输并进行校验与重命名
        boolean isComplete = checkUploadStatus(uploadFile, confFile);
        if (isComplete) {
            FileInputStream fileInputStream = new FileInputStream(tempFile.getPath());
            String md5 = DigestUtils.md5DigestAsHex(fileInputStream);
            fileInputStream.close();
            if (StringUtils.isNotBlank(md5) && !md5.equals(uploadFile.getIdentifier())) {
                throw new NotSameFileException();
            }
            tempFile.renameTo(file);
            if (FileUtil.isImageFile(uploadFile.getFileType())) {
                Thumbnails.of(file).size(300, 300).toFile(minFile);
            }

            uploadFile.setSuccess(1);
            uploadFile.setMessage("上传成功");
        } else {
            uploadFile.setSuccess(0);
            uploadFile.setMessage("未完成");
        }
        uploadFile.setFileSize(uploadFile.getTotalSize());
        saveUploadFileList.add(uploadFile);

        return saveUploadFileList;
    }
}
