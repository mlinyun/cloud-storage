package com.mlinyun.cloudstorage.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 路径工具类
 */
@Slf4j
public class PathUtil {
    /**
     * 获取文件路径
     *
     * @return 文件路径
     */
    public static String getFilePath() {
        String path = "upload";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        path = File.separator + path + File.separator + simpleDateFormat.format(new Date());
        String staticPath = PathUtil.getStaticPath();
        String dirUrl = staticPath + path;
        if (createDirectory(dirUrl)) {
            return path;    // 如果文件创建成功，返回文件创建的路径
        } else {
            return ""; // 否则返回空
        }
    }

    /**
     * 创建目录
     *
     * @param dirUrl 要创建目录的路径
     * @return 创建好的目录路径
     */
    public static boolean createDirectory(String dirUrl) {
        boolean flag = true;
        File dir = new File(dirUrl);
        if (!dir.exists()) {
            try {
                boolean isSuccessMakeDir = dir.mkdir();
                if (!isSuccessMakeDir) {
                    log.error("目录创建失败: " + dirUrl);
                    flag = false;
                }
            } catch (Exception e) {
                log.error("目录创建失败: " + dirUrl);
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 获取文件静态路径
     *
     * @return 文件静态路径
     */
    public static String getStaticPath() {
        String localStoragePath = PropertiesUtil.getProperty("file.local-storage-path");
        if (StringUtils.isNotEmpty(localStoragePath)) {
            return localStoragePath;
        } else {
            String projectRootAbsolutePath = getProjectRootPath();
            int index = projectRootAbsolutePath.indexOf("file:");
            if (index != -1) {
                projectRootAbsolutePath = projectRootAbsolutePath.substring(0, index);
            }
            return projectRootAbsolutePath + "static" + File.separator;
        }
    }

    /**
     * 获取项目所在的根目录路径 resources 路径
     *
     * @return 项目根目录路径
     */
    public static String getProjectRootPath() {
        String absolutePath = null;
        try {
            String url = ResourceUtils.getURL("classpath:").getPath();
            absolutePath = urlDecode(new File(url).getAbsolutePath()) + File.separator;
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("获取项目所在的根目录路径 resources 路径失败: " + fileNotFoundException);
        }
        return absolutePath;
    }

    /**
     * 路径解码
     *
     * @param url 要解码的路径
     * @return 解码后的路径
     */
    public static String urlDecode(String url) {
        String decodeUrl = null;
        try {
            decodeUrl = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            log.error("路径解码出错: " + unsupportedEncodingException);
        }
        return decodeUrl;
    }

    /**
     * 获取系统文件名分割符
     *
     * @return 文件名分割符
     */
    public static String getSystemSeparator() {
        String separator = "";
        boolean flag = System.getProperty("os.name").toUpperCase().contains("WINDOWS");
        separator = flag ? "\\" : "/";
        return separator;
    }

}
