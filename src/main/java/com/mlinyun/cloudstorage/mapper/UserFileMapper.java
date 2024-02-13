package com.mlinyun.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mlinyun.cloudstorage.model.UserFile;
import com.mlinyun.cloudstorage.vo.UserFileListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserFileMapper 接口
 */
public interface UserFileMapper extends BaseMapper<UserFile> {

    /**
     * 文件夹查询服务接口
     *
     * @param userFile   用户文件
     * @param beginCount 当前页码
     * @param pageCount  一页显示数量
     * @return 文件夹列表
     */
    List<UserFileListVO> userFileList(UserFile userFile, Long beginCount, Long pageCount);

    /**
     * 通过文件扩展名获取文件列表服务接口
     *
     * @param fileNameList 文件名列表
     * @param beginCount   当前页码
     * @param pageCount    一页显示数量
     * @param userId       用户ID
     * @return 包含该扩展名的文件列表
     */
    List<UserFileListVO> selectFileByExtendName(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    /**
     * 统计该扩展名的文件数量
     *
     * @param fileNameList 文件名列表
     * @param beginCount   当前页码
     * @param pageCount    一页显示数量
     * @param userId       用户ID
     * @return 该扩展名的文件数量
     */
    Long selectCountByExtendName(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    /**
     * 不通过文件扩展名获取文件列表
     *
     * @param fileNameList 文件名列表
     * @param beginCount   当前页码
     * @param pageCount    一页显示数量
     * @param userId       用户ID
     * @return 不包含该扩展名的文件列表
     */
    List<UserFileListVO> selectFileNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    /**
     * 不通过文件扩展名获取文件数量
     *
     * @param fileNameList 文件名列表
     * @param beginCount   当前页码
     * @param pageCount    一页显示数量
     * @param userId       用户ID
     * @return 文件数量
     */
    Long selectCountNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    /**
     * 文件移动服务接口
     *
     * @param oldFilePath 原文件路径
     * @param newFilePath 新文件路径
     * @param userId      用户ID
     */
    void updateFilePathByFilePath(String oldFilePath, String newFilePath, Long userId);

    /**
     * 文件重命名服务接口
     *
     * @param filePath    文件路径
     * @param oldFilePath 原文件路径
     * @param userId      用户ID
     */
    void replaceFilePath(@Param("filePath") String filePath, @Param("oldFilePath") String oldFilePath, @Param("userId") Long userId);

    /**
     * 获取存储信息服务接口
     *
     * @param userId 用户ID
     * @return 文件存储大小
     */
    Long selectStorageSizeByUserId(Long userId);

}




