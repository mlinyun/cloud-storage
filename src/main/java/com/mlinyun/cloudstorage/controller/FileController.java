package com.mlinyun.cloudstorage.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mlinyun.cloudstorage.common.RestResult;
import com.mlinyun.cloudstorage.common.ResultCodeEnum;
import com.mlinyun.cloudstorage.dto.BatchDeleteFileDTO;
import com.mlinyun.cloudstorage.dto.CreateFileDTO;
import com.mlinyun.cloudstorage.dto.DeleteFileDTO;
import com.mlinyun.cloudstorage.dto.UserFileListDTO;
import com.mlinyun.cloudstorage.exception.BusinessException;
import com.mlinyun.cloudstorage.model.User;
import com.mlinyun.cloudstorage.model.UserFile;
import com.mlinyun.cloudstorage.service.FileService;
import com.mlinyun.cloudstorage.service.UserFileService;
import com.mlinyun.cloudstorage.service.UserService;
import com.mlinyun.cloudstorage.util.DateUtil;
import com.mlinyun.cloudstorage.vo.UserFileListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
