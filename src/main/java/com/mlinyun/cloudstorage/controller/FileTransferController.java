package com.mlinyun.cloudstorage.controller;


import com.mlinyun.cloudstorage.common.RestResult;
import com.mlinyun.cloudstorage.common.ResultCodeEnum;
import com.mlinyun.cloudstorage.dto.DownloadFileDTO;
import com.mlinyun.cloudstorage.dto.UploadFileDTO;
import com.mlinyun.cloudstorage.exception.BusinessException;
import com.mlinyun.cloudstorage.model.File;
import com.mlinyun.cloudstorage.model.Storage;
import com.mlinyun.cloudstorage.model.User;
import com.mlinyun.cloudstorage.model.UserFile;
import com.mlinyun.cloudstorage.service.FileService;
import com.mlinyun.cloudstorage.service.FileTransferService;
import com.mlinyun.cloudstorage.service.UserFileService;
import com.mlinyun.cloudstorage.service.UserService;
import com.mlinyun.cloudstorage.util.DateUtil;
import com.mlinyun.cloudstorage.util.FileUtil;
import com.mlinyun.cloudstorage.vo.UploadFileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件传输接口
 */
@RestController
@RequestMapping("/fileTransfer")
@Tag(name = "文件传输接口", description = "该接口为文件传输接口，主要用来做文件的上传和下载")
public class FileTransferController {

    @Resource
    UserService userService;

    @Resource
    FileService fileService;

    @Resource
    UserFileService userFileService;

    @Resource
    FileTransferService fileTransferService;

    /**
     * 极速上传文件接口
     *
     * @param token         token 值
     * @param uploadFileDTO 前台上传文件接口请求参数载体
     * @return 上传文件信息
     */
    @ResponseBody
    @GetMapping(value = "/uploadFile")
    @Operation(summary = "极速上传", description = "校验文件MD5判断文件是否存在，如果存在直接上传成功并返回skipUpload=true，如果不存在返回skipUpload=false需要再次调用该接口的POST方法", tags = {"文件传输接口"})
    public RestResult<UploadFileVO> uploadFileSpeed(@RequestHeader("token") String token, @RequestBody UploadFileDTO uploadFileDTO) {
        // 非空判断
        if (uploadFileDTO == null || token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        UploadFileVO uploadFileVO = new UploadFileVO();
        Map<String, Object> param = new HashMap<>();
        param.put("identifier", uploadFileDTO.getIdentifier());
        synchronized (FileTransferController.class) {
            List<File> list = fileService.listByMap(param);
            if (list != null && !list.isEmpty()) {
                File file = list.get(0);
                UserFile userfile = new UserFile();
                userfile.setFileId(file.getFileId());
                userfile.setUserId(sessionUser.getUserId());
                userfile.setFilePath(uploadFileDTO.getFilePath());
                String fileName = uploadFileDTO.getFilename();
                userfile.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
                userfile.setExtendName(FileUtil.getFileExtendName(fileName));
                userfile.setIsDir(0);
                userfile.setUploadTime(DateUtil.getCurrentTime());
                userfile.setDeleteFlag(0);
                userFileService.save(userfile);
                // fileService.increaseFilePointCount(file.getFileId());
                uploadFileVO.setSkipUpload(true);
            } else {
                uploadFileVO.setSkipUpload(false);
            }
        }
        return RestResult.success().data(uploadFileVO);
    }

    /**
     * 上传文件接口
     *
     * @param token         token 值
     * @param request       请求
     * @param uploadFileDTO 前台上传文件接口请求参数载体
     * @return 文件上传信息
     */
    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @Operation(summary = "上传文件", description = "真正的上传文件接口", tags = {"文件传输接口"})
    public RestResult<UploadFileVO> uploadFile(@RequestHeader("token") String token, HttpServletRequest request, @RequestBody UploadFileDTO uploadFileDTO) {
        // 非空判断
        if (uploadFileDTO == null || token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        fileTransferService.uploadFile(request, uploadFileDTO, sessionUser.getUserId());
        UploadFileVO uploadFileVO = new UploadFileVO();
        return RestResult.success().data(uploadFileVO);
    }

    /**
     * 下载文件接口
     *
     * @param response        响应
     * @param downloadFileDTO 前台下载文件接口请求参数载体
     */
    @Operation(summary = "下载文件", description = "下载文件接口", tags = {"文件传输接口"})
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @RequestBody DownloadFileDTO downloadFileDTO) {
        // 非空判断
        if (downloadFileDTO == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        fileTransferService.downloadFile(response, downloadFileDTO);
    }

    /**
     * 获取存储信息
     *
     * @param token token 值
     * @return 存储信息大小
     */
    @ResponseBody
    @RequestMapping(value = "/getStorage", method = RequestMethod.GET)
    @Operation(summary = "获取存储信息", description = "获取存储信息", tags = {"文件传输接口"})
    public RestResult<Long> getStorage(@RequestHeader("token") String token) {
        // 非空判断
        if (token == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        // 通过 token 获取用户信息，验证用户是否登录
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            throw new BusinessException(ResultCodeEnum.TOKEN_AUTH_FAILED, "用户未登录");
        }
        Storage storage = new Storage();
        Long storageSize = fileTransferService.selectStorageSizeByUserId(sessionUser.getUserId());
        return RestResult.success().data(storageSize);
    }

}
