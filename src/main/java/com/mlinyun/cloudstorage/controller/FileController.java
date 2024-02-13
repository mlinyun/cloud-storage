package com.mlinyun.cloudstorage.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mlinyun.cloudstorage.common.RestResult;
import com.mlinyun.cloudstorage.common.ResultCodeEnum;
import com.mlinyun.cloudstorage.dto.*;
import com.mlinyun.cloudstorage.exception.BusinessException;
import com.mlinyun.cloudstorage.model.File;
import com.mlinyun.cloudstorage.model.User;
import com.mlinyun.cloudstorage.model.UserFile;
import com.mlinyun.cloudstorage.service.FileService;
import com.mlinyun.cloudstorage.service.UserFileService;
import com.mlinyun.cloudstorage.service.UserService;
import com.mlinyun.cloudstorage.util.DateUtil;
import com.mlinyun.cloudstorage.util.PathUtil;
import com.mlinyun.cloudstorage.vo.TreeNodeVO;
import com.mlinyun.cloudstorage.vo.UserFileListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 文件接口
 */
@Slf4j
@RestController
@RequestMapping("/file")
@Tag(name = "文件接口", description = "该接口为文件接口，主要用来做一些文件的基本操作，如创建目录，删除，移动，复制等。")
public class FileController {

    @Resource
    FileService fileService;

    @Resource
    UserService userService;

    @Resource
    UserFileService userFileService;

    /**
     * 目录（文件夹）创建接口
     *
     * @param createFileDTO 前台文件创建接口请求参数
     * @param token         前台文件创建请求时携带的token
     * @return 文件创建结果
     */
    @ResponseBody
    @PostMapping(value = "/createFile")
    @Operation(summary = "创建文件", description = "目录（文件夹）的创建", tags = {"文件接口"})
    public RestResult<String> createFile(@RequestHeader("token") String token, @RequestBody CreateFileDTO createFileDTO) {
        // 非空判断
        if (createFileDTO == null || token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        // 判断文件夹是否存在
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getFileName, "").eq(UserFile::getFilePath, "").eq(UserFile::getUserId, 0);
        List<UserFile> userFiles = userFileService.list(lambdaQueryWrapper);
        if (!userFiles.isEmpty()) {
            return RestResult.fail().message("同目录下文件名重复");
        }
        // 创建文件夹信息
        UserFile userFile = new UserFile();
        userFile.setUserId(sessionUser.getUserId());    // 文件创建用户ID
        userFile.setFileName(createFileDTO.getFileName());  // 文件夹名
        userFile.setFilePath(createFileDTO.getFilePath());  // 文件夹路径
        userFile.setIsDir(1);   // 是否是目录 0-否, 1-是
        userFile.setUploadTime(DateUtil.getCurrentTime());  // 文件上传时间
        userFile.setDeleteFlag(0);  // 文件逻辑删除
        // 保存文件夹
        boolean result = userFileService.save(userFile);
        if (result) {
            return RestResult.success().message("文件夹创建成功！");
        } else {
            return RestResult.fail().message("文件夹创建失败！");
        }
    }

    /**
     * 目录（文件夹）列表查询接口
     *
     * @param userFileListDTO 前台文件夹列表查询接口请求参数
     * @param token           前台文件夹列表查询请求时携带的token
     * @return 文件夹列表
     */
    @ResponseBody
    @GetMapping(value = "/getFileList")
    @Operation(summary = "获取文件列表", description = "用来做前台文件列表展示", tags = {"文件接口"})
    public RestResult<UserFileListVO> getUserFileList(@RequestHeader("token") String token, @RequestBody UserFileListDTO userFileListDTO) {
        // 非空判断
        if (userFileListDTO == null || token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        // 获取前台传来的参数值
        String filePath = userFileListDTO.getFilePath();    // 文件路径
        Long currentPage = userFileListDTO.getCurrentPage();// 当前页码
        Long pageCount = userFileListDTO.getPageCount();    // 一页显示的数量
        Long userId = sessionUser.getUserId();  // 用户ID
        // 调用获取文件夹列表服务
        List<UserFileListVO> fileListVOS = userFileService.getUserFileByFilePath(filePath, userId, currentPage, pageCount);
        // 获取文件总数
        LambdaQueryWrapper<UserFile> userFilLlambdaQueryWrapper = new LambdaQueryWrapper<>();
        userFilLlambdaQueryWrapper
                .eq(UserFile::getUserId, userId)
                .eq(UserFile::getFilePath, filePath)
                .eq(UserFile::getDeleteFlag, 0);
        long total = userFileService.count(userFilLlambdaQueryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", fileListVOS);
        return RestResult.success().data(map);
    }

    /**
     * 通过文件类型选择文件
     *
     * @param token       前台发送请求时需携带的token
     * @param fileType    文件类型
     * @param currentPage 当前页码
     * @param pageCount   一页显示的数量
     * @return 该类型的文件列表
     */
    @ResponseBody
    @GetMapping(value = "/selectFileByFileType")
    @Operation(summary = "通过文件类型选择文件", description = "该接口可以实现文件格式分类查看", tags = {"文件接口"})
    public RestResult<List<Map<String, Object>>> selectFileByFileType(@RequestHeader("token") String token, int fileType, Long currentPage, Long pageCount) {
        // 非空判断
        if (token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        // 获取用户ID
        long userId = sessionUser.getUserId();
        // 调用通过文件类型获取用户文件列表服务
        Map<String, Object> map = userFileService.getUserFileByType(fileType, currentPage, pageCount, userId);
        return RestResult.success().data(map);
    }

    /**
     * @param token         前台发送删除单个文件时携带的token
     * @param deleteFileDTO 前台删除文件接口请求参数
     * @return 删除结果
     */
    @ResponseBody
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    @Operation(summary = "删除单个文件", description = "可以删除文件或者目录", tags = {"文件接口"})
    public RestResult<String> deleteFile(@RequestHeader("token") String token, @RequestBody DeleteFileDTO deleteFileDTO) {
        // 非空判断
        if (deleteFileDTO == null || token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        int result = userFileService.deleteUserFile(deleteFileDTO.getUserFileId(), sessionUser.getUserId());
        if (result > 0) {
            return RestResult.success().message("文件删除成功！");
        } else {
            return RestResult.fail().message("文件删除失败！");
        }
    }

    /**
     * @param token              前台发送批量删除文件时携带的token
     * @param batchDeleteFileDTO 前台批量删除文件接口参数
     * @return 批量删除文件接结果
     */
    @ResponseBody
    @RequestMapping(value = "/batchDeleteFile", method = RequestMethod.POST)
    @Operation(summary = "批量删除文件", description = "批量删除文件", tags = {"文件接口"})
    public RestResult<String> batchDeleteFile(@RequestHeader("token") String token, @RequestBody BatchDeleteFileDTO batchDeleteFileDTO) {
        // 非空判断
        if (batchDeleteFileDTO == null || token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        List<UserFile> userFiles = JSON.parseArray(batchDeleteFileDTO.getFiles(), UserFile.class);

        boolean flag = true;
        int result;
        for (UserFile userFile : userFiles) {
            result = userFileService.deleteUserFile(userFile.getUserFileId(), sessionUser.getUserId());
            if (!(result > 0)) {
                flag = false;
                break;
            }
        }

        if (flag) {
            return RestResult.success().message("批量删除文件成功！");
        } else {
            return RestResult.fail().message("批量删除文件失败！");
        }
    }

    /**
     * 获取文件树接口
     *
     * @param token 前台获取文件树接口时携带的 token 值
     * @return 文件树
     */
    @ResponseBody
    @RequestMapping(value = "/getFileTree", method = RequestMethod.GET)
    @Operation(summary = "获取文件树", description = "文件移动的时候需要用到该接口，用来展示目录树", tags = {"文件接口"})
    public RestResult<TreeNodeVO> getFileTree(@RequestHeader("token") String token) {
        // 非空判断
        if (token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        RestResult<TreeNodeVO> result = new RestResult<>();
        UserFile userFile = new UserFile();
        userFile.setUserId(sessionUser.getUserId());
        List<UserFile> filePathList = userFileService.selectFilePathTreeByUserId(sessionUser.getUserId());
        TreeNodeVO resultTreeNode = new TreeNodeVO();
        String separator = PathUtil.getSystemSeparator();
        resultTreeNode.setLabel(separator);
        for (int i = 0; i < filePathList.size(); i++) {
            String filePath = filePathList.get(i).getFilePath() + filePathList.get(i).getFileName() + separator;
            Queue<String> queue = new LinkedList<>();
            String[] strArr = filePath.split(separator);
            for (int j = 0; j < strArr.length; j++) {
                if (!"".equals(strArr[j]) && strArr[j] != null) {
                    queue.add(strArr[j]);
                }
            }
            if (queue.size() == 0) {
                continue;
            }
            resultTreeNode = insertTreeNode(resultTreeNode, separator, queue);
        }
        result.setSuccess(true);
        result.setData(resultTreeNode);
        return result;
    }

    public TreeNodeVO insertTreeNode(TreeNodeVO treeNode, String filePath, Queue<String> nodeNameQueue) {
        List<TreeNodeVO> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null) {
            return treeNode;
        }
        Map<String, String> map = new HashMap<>();
        String separator = PathUtil.getSystemSeparator();
        filePath = filePath + currentNodeName + separator;
        map.put("filePath", filePath);
        if (!isExistPath(childrenTreeNodes, currentNodeName)) {  // 1、判断有没有该子节点，如果没有则插入
            // 插入
            TreeNodeVO resultTreeNode = new TreeNodeVO();
            resultTreeNode.setAttributes(map);
            resultTreeNode.setLabel(nodeNameQueue.poll());
            // resultTreeNode.setId(treeid++);
            childrenTreeNodes.add(resultTreeNode);
        } else {  // 2、如果有，则跳过
            nodeNameQueue.poll();
        }
        if (nodeNameQueue.size() != 0) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {
                TreeNodeVO childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())) {
                    childrenTreeNode = insertTreeNode(childrenTreeNode, filePath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildren(childrenTreeNodes);
                }
            }
        } else {
            treeNode.setChildren(childrenTreeNodes);
        }
        return treeNode;
    }

    public boolean isExistPath(List<TreeNodeVO> childrenTreeNodes, String path) {
        boolean isExistPath = false;
        try {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {
                if (path.equals(childrenTreeNodes.get(i).getLabel())) {
                    isExistPath = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExistPath;
    }

    /**
     * 文件移动接口
     *
     * @param token       前台发送文件移动请求时携带的 token
     * @param moveFileDTO 前台移动文件接口请求参数
     * @return 移动结果
     */
    @ResponseBody
    @RequestMapping(value = "/moveFile", method = RequestMethod.POST)
    @Operation(summary = "文件移动", description = "可以移动文件或者目录", tags = {"文件接口"})
    public RestResult<String> moveFile(@RequestHeader("token") String token, @RequestBody MoveFileDTO moveFileDTO) {
        // 非空判断
        if (moveFileDTO == null || token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        String oldFilePath = moveFileDTO.getOldFilePath();
        String newFilePath = moveFileDTO.getFilePath();
        String fileName = moveFileDTO.getFileName();
        String extendName = moveFileDTO.getExtendName();
        userFileService.updateFilePathByFilePath(oldFilePath, newFilePath, fileName, extendName, sessionUser.getUserId());
        return RestResult.success().message("文件移动成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/batchMoveFile", method = RequestMethod.POST)
    @Operation(summary = "批量移动文件", description = "可以同时选择移动多个文件或者目录", tags = {"文件接口"})
    public RestResult<String> batchMoveFile(@RequestHeader("token") String token, @RequestBody BatchMoveFileDTO batchMoveFileDTO) {
        // 非空判断
        if (batchMoveFileDTO == null || token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        String files = batchMoveFileDTO.getFiles();
        String newFilePath = batchMoveFileDTO.getFilePath();
        List<UserFile> userFiles = JSON.parseArray(files, UserFile.class);

        for (UserFile userFile : userFiles) {
            userFileService.updateFilePathByFilePath(
                    userFile.getFilePath(),
                    newFilePath,
                    userFile.getFileName(),
                    userFile.getExtendName(),
                    sessionUser.getUserId()
            );
        }
        return RestResult.success().message("批量移动文件成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/renameFile", method = RequestMethod.POST)
    @Operation(summary = "文件重命名", description = "文件重命名", tags = {"文件接口"})
    public RestResult<String> renameFile(@RequestHeader("token") String token, @RequestBody RenameFileDTO renameFileDTO) {
        // 非空判断
        if (renameFileDTO == null || token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        UserFile userFile = userFileService.getById(renameFileDTO.getUserFileId());
        List<UserFile> userFiles = userFileService.selectUserFileByNameAndPath(
                renameFileDTO.getFileName(),
                userFile.getFilePath(),
                sessionUser.getUserId()
        );
        if (userFiles != null && !userFiles.isEmpty()) {
            return RestResult.fail().message("同名文件已存在");
        }
        if (userFile.getIsDir() == 1) {
            LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userFileLambdaUpdateWrapper
                    .set(UserFile::getFileName, renameFileDTO.getFileName())
                    .eq(UserFile::getUserFileId, renameFileDTO.getUserFileId());
            userFileService.update(userFileLambdaUpdateWrapper);
            userFileService.replaceUserFilePath(
                    userFile.getFilePath() + renameFileDTO.getFileName() + PathUtil.getSystemSeparator(),
                    userFile.getFilePath() + userFile.getFileName() + PathUtil.getStaticPath(),
                    sessionUser.getUserId()
            );
        } else {
            File file = fileService.getById(userFile.getFileId());
            LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userFileLambdaUpdateWrapper
                    .set(UserFile::getFileName, renameFileDTO.getFileName())
                    .set(UserFile::getUploadTime, DateUtil.getCurrentTime())
                    .eq(UserFile::getUserFileId, renameFileDTO.getUserFileId());
            userFileService.update(userFileLambdaUpdateWrapper);
        }
        return RestResult.success().message("文件重命名成功！");
    }
}
