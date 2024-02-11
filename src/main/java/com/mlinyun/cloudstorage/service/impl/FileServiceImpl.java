package com.mlinyun.cloudstorage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlinyun.cloudstorage.mapper.FileMapper;
import com.mlinyun.cloudstorage.model.File;
import com.mlinyun.cloudstorage.service.FileService;
import org.springframework.stereotype.Service;

/**
 * 文件服务实现类
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

}




