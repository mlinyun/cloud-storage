package com.mlinyun.cloudstorage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlinyun.cloudstorage.mapper.UserFileMapper;
import com.mlinyun.cloudstorage.model.UserFile;
import com.mlinyun.cloudstorage.service.UserFileService;
import org.springframework.stereotype.Service;

/**
 * 用户文件服务实现类
 */
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements UserFileService {

}




