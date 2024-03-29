package com.mlinyun.cloudstorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlinyun.cloudstorage.constant.FileConstant;
import com.mlinyun.cloudstorage.mapper.FileMapper;
import com.mlinyun.cloudstorage.mapper.RecoveryFileMapper;
import com.mlinyun.cloudstorage.mapper.UserFileMapper;
import com.mlinyun.cloudstorage.model.File;
import com.mlinyun.cloudstorage.model.RecoveryFile;
import com.mlinyun.cloudstorage.model.UserFile;
import com.mlinyun.cloudstorage.service.UserFileService;
import com.mlinyun.cloudstorage.util.DateUtil;
import com.mlinyun.cloudstorage.util.PathUtil;
import com.mlinyun.cloudstorage.vo.UserFileListVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 用户文件服务实现类
 */
@Slf4j
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements UserFileService {

    public static Executor executor = Executors.newFixedThreadPool(20);

    @Resource
    UserFileMapper userFileMapper;

    @Resource
    FileMapper fileMapper;

    @Resource
    RecoveryFileMapper recoveryFileMapper;

    /**
     * 获取文件夹列表服务实现
     *
     * @param filePath    文件路径
     * @param userId      用户ID
     * @param currentPage 当前页码
     * @param pageCount   一页显示的数量
     * @return 文件夹列表
     */
    @Override
    public List<UserFileListVO> getUserFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount) {
        // 计算返回的起始位置
        Long beginCount = (currentPage - 1) * pageCount;
        UserFile userFile = new UserFile();
        userFile.setUserId(userId);
        userFile.setFilePath(filePath);
        return userFileMapper.userFileList(userFile, beginCount, pageCount);
    }

    /**
     * 通过文件类型获取用户文件列表服务实现
     *
     * @param fileType    文件类型
     * @param currentPage 当前页码
     * @param pageCount   一页显示的数量
     * @return 该类型的文件列表
     */
    @Override
    public Map<String, Object> getUserFileByType(int fileType, Long currentPage, Long pageCount, Long userId) {
        Long beginCount = (currentPage - 1) * pageCount;
        List<UserFileListVO> userFileListVOS = null;
        Long total = 0L;
        // 如果文件类型为其他类
        if (fileType == FileConstant.OTHER_TYPE) {
            List<String> arrList = new ArrayList<>();
            arrList.addAll(Arrays.asList(FileConstant.IMG_FILE));
            arrList.addAll(Arrays.asList(FileConstant.DOC_FILE));
            arrList.addAll(Arrays.asList(FileConstant.VIDEO_FILE));
            arrList.addAll(Arrays.asList(FileConstant.MUSIC_FILE));
            userFileListVOS = userFileMapper.selectFileNotInExtendNames(arrList, beginCount, pageCount, userId);
            total = userFileMapper.selectCountNotInExtendNames(arrList, beginCount, pageCount, userId);
        } else {
            List<String> fileExtends = null;
            if (fileType == FileConstant.IMAGE_TYPE) {
                fileExtends = Arrays.asList(FileConstant.IMG_FILE);
            } else if (fileType == FileConstant.DOC_TYPE) {
                fileExtends = Arrays.asList(FileConstant.DOC_FILE);
            } else if (fileType == FileConstant.VIDEO_TYPE) {
                fileExtends = Arrays.asList(FileConstant.VIDEO_FILE);
            } else if (fileType == FileConstant.MUSIC_TYPE) {
                fileExtends = Arrays.asList(FileConstant.MUSIC_FILE);
            }
            userFileListVOS = userFileMapper.selectFileByExtendName(fileExtends, beginCount, pageCount, userId);
            total = userFileMapper.selectCountByExtendName(fileExtends, beginCount, pageCount, userId);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", userFileListVOS);
        map.put("total", total);
        return map;
    }

    /**
     * 删除用户文件服务实现
     *
     * @param userFileId    用户文件ID
     * @param sessionUserId 用户ID
     */
    @Override
    public int deleteUserFile(Long userFileId, Long sessionUserId) {
        UserFile userFile = userFileMapper.selectById(userFileId);
        String uuid = UUID.randomUUID().toString();
        // 如果是目录
        if (userFile.getIsDir() == 1) {
            // 更新数据库中的数据
            LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userFileLambdaUpdateWrapper
                    .set(UserFile::getDeleteFlag, 1) // 设置删除标识 1-删除
                    .set(UserFile::getDeleteBatchNum, uuid) // 设置删除批次号
                    .set(UserFile::getDeleteTime, DateUtil.getCurrentTime()) // 设置删除时间
                    .set(UserFile::getUserFileId, userFileId); // 设置用户文件ID
            userFileMapper.update(null, userFileLambdaUpdateWrapper);
            String filePath = userFile.getFilePath() + userFile.getFileName() + PathUtil.getSystemSeparator();
            updateFileDeleteStateByFilePath(filePath, userFile.getDeleteBatchNum(), sessionUserId);
        } else { // 如果是文件
            UserFile userFileTemp = userFileMapper.selectById(userFileId);
            File file = fileMapper.selectById(userFileTemp.getFileId());
            LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userFileLambdaUpdateWrapper
                    .set(UserFile::getDeleteTime, DateUtil.getCurrentTime())
                    .set(UserFile::getDeleteBatchNum, uuid)
                    .eq(UserFile::getUserFileId, userFileTemp.getUserFileId());
            userFileMapper.update(null, userFileLambdaUpdateWrapper);
        }

        RecoveryFile recoveryFile = new RecoveryFile();
        recoveryFile.setUserFileId(userFileId);
        recoveryFile.setDeleteTime(DateUtil.getCurrentTime());
        recoveryFile.setDeleteBatchNum(uuid);
        return recoveryFileMapper.insert(recoveryFile);
    }

    private void updateFileDeleteStateByFilePath(String filePath, String deleteBatchNum, Long userId) {
        new Thread(() -> {
            List<UserFile> fileList = selectFileTreeListLikeFilePath(filePath, userId);
            for (int i = 0; i < fileList.size(); i++) {
                UserFile userFileTemp = fileList.get(i);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //标记删除标志
                        LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
                        userFileLambdaUpdateWrapper1.set(UserFile::getDeleteFlag, 1)
                                .set(UserFile::getDeleteTime, DateUtil.getCurrentTime())
                                .set(UserFile::getDeleteBatchNum, deleteBatchNum)
                                .eq(UserFile::getUserFileId, userFileTemp.getUserFileId())
                                .eq(UserFile::getDeleteFlag, 0);
                        userFileMapper.update(null, userFileLambdaUpdateWrapper1);
                    }
                });
            }
        }).start();
    }

    /**
     * @param filePath 文件路径
     * @param userId   用户ID
     * @return 文件列表
     */
    @Override
    public List<UserFile> selectFileTreeListLikeFilePath(String filePath, long userId) {
        filePath = filePath.replace("\\", "\\\\\\\\");
        filePath = filePath.replace("'", "\\'");
        filePath = filePath.replace("%", "\\%");
        filePath = filePath.replace("_", "\\_");
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        log.info("查询文件路径：" + filePath);
        lambdaQueryWrapper.eq(UserFile::getUserId, userId).likeRight(UserFile::getFilePath, filePath);
        return userFileMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 通过用户ID查询文件目录树服务实现
     *
     * @param userId 用户ID
     * @return 文件目录树
     */
    @Override
    public List<UserFile> selectFilePathTreeByUserId(Long userId) {
        LambdaQueryWrapper<UserFile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userFileLambdaQueryWrapper.eq(UserFile::getUserId, userId).eq(UserFile::getIsDir, 1);
        return userFileMapper.selectList(userFileLambdaQueryWrapper);
    }

    /**
     * 移动文件服务实现
     *
     * @param oldFilePath 原文件路径
     * @param newFilePath 新文件路径
     * @param fileName    文件名
     * @param extendName  文件扩展名
     * @param userId      用户ID
     */
    @Override
    public void updateFilePathByFilePath(String oldFilePath, String newFilePath, String fileName, String extendName, Long userId) {
        if ("null".equals(extendName)) {
            extendName = null;
        }

        LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userFileLambdaUpdateWrapper
                .set(UserFile::getFilePath, newFilePath)
                .eq(UserFile::getFilePath, oldFilePath)
                .eq(UserFile::getFileName, fileName)
                .eq(UserFile::getUserId, userId);
        if (StringUtils.isNotEmpty(extendName)) {
            userFileLambdaUpdateWrapper.eq(UserFile::getExtendName, extendName);
        } else {
            userFileLambdaUpdateWrapper.isNull(UserFile::getExtendName);
        }
        userFileMapper.update(null, userFileLambdaUpdateWrapper);
        // 移动子目录
        String separator = PathUtil.getSystemSeparator();
        oldFilePath = oldFilePath + fileName + separator;
        newFilePath = newFilePath + fileName + separator;

        oldFilePath = oldFilePath.replace("\\", "\\\\\\\\");
        oldFilePath = oldFilePath.replace("'", "\\'");
        oldFilePath = oldFilePath.replace("%", "\\%");
        oldFilePath = oldFilePath.replace("_", "\\_");

        if (extendName == null) { // 为 null 说明是目录，则需要移动子目录
            userFileMapper.updateFilePathByFilePath(oldFilePath, newFilePath, userId);
        }
    }

    /**
     * 通过文件名和路径获取文件列表服务实现
     *
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param userId   用户ID
     * @return 文件列表
     */
    @Override
    public List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, Long userId) {
        LambdaQueryWrapper<UserFile> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userFileLambdaQueryWrapper
                .eq(UserFile::getFileName, fileName)
                .eq(UserFile::getFilePath, filePath)
                .eq(UserFile::getUserId, userId)
                .eq(UserFile::getDeleteFlag, 0);
        return userFileMapper.selectList(userFileLambdaQueryWrapper);
    }

    /**
     * 重命名文件服务实现
     *
     * @param filePath    文件路径
     * @param oldFilePath 原文件路径
     * @param userId      用户ID
     */
    @Override
    public void replaceUserFilePath(String filePath, String oldFilePath, Long userId) {
        userFileMapper.replaceFilePath(filePath, oldFilePath, userId);
    }

}
