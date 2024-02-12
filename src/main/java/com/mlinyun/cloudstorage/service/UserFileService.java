package com.mlinyun.cloudstorage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mlinyun.cloudstorage.model.UserFile;
import com.mlinyun.cloudstorage.vo.UserFileListVO;

import java.util.List;
import java.util.Map;

/**
 * 用户文件服务接口
 */
public interface UserFileService extends IService<UserFile> {

    /**
     * 获取文件夹列表服务
     *
     * @param filePath    文件路径
     * @param userId      用户ID
     * @param currentPage 当前页码
     * @param pageCount   一页显示的数量
     * @return 文件夹列表
     */
    List<UserFileListVO> getUserFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount);

    /**
     * 通过文件类型获取用户文件列表服务
     *
     * @param fileType    文件类型
     * @param currentPage 当前页码
     * @param pageCount   一页显示的数量
     * @return 该类型的文件列表
     */
    Map<String, Object> getUserFileByType(int fileType, Long currentPage, Long pageCount, Long userId);

}
