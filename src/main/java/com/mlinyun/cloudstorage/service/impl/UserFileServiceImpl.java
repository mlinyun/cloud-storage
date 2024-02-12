package com.mlinyun.cloudstorage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlinyun.cloudstorage.constant.FileConstant;
import com.mlinyun.cloudstorage.mapper.UserFileMapper;
import com.mlinyun.cloudstorage.model.UserFile;
import com.mlinyun.cloudstorage.service.UserFileService;
import com.mlinyun.cloudstorage.vo.UserFileListVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户文件服务实现类
 */
@Slf4j
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements UserFileService {

    @Resource
    UserFileMapper userFileMapper;

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
}




