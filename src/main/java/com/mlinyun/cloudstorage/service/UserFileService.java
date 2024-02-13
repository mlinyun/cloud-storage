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

    /**
     * 删除用户文件服务
     *
     * @param userFileId    用户文件ID
     * @param sessionUserId 用户ID
     */
    int deleteUserFile(Long userFileId, Long sessionUserId);

    /**
     * @param filePath 文件路径
     * @param userId   用户ID
     * @return 文件列表
     */
    List<UserFile> selectFileTreeListLikeFilePath(String filePath, long userId);

    /**
     * 通过用户ID查询文件目录树服务
     *
     * @param userId 用户ID
     * @return 文件目录树
     */
    List<UserFile> selectFilePathTreeByUserId(Long userId);

    /**
     * 移动文件服务
     *
     * @param oldFilePath 原文件路径
     * @param newFilePath 新文件路径
     * @param fileName    文件名
     * @param extendName  文件扩展名
     * @param userId      用户ID
     */
    void updateFilePathByFilePath(String oldFilePath, String newFilePath, String fileName, String extendName, Long userId);

    /**
     * 通过文件名和路径获取文件列表服务
     *
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param userId   用户ID
     * @return 文件列表
     */
    List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, Long userId);

    /**
     * 重命名文件服务
     *
     * @param filePath    文件路径
     * @param oldFilePath 原文件路径
     * @param userId      用户ID
     */
    void replaceUserFilePath(String filePath, String oldFilePath, Long userId);

}
