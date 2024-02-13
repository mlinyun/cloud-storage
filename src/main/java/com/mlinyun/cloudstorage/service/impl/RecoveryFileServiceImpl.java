package com.mlinyun.cloudstorage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlinyun.cloudstorage.mapper.RecoveryFileMapper;
import com.mlinyun.cloudstorage.model.RecoveryFile;
import com.mlinyun.cloudstorage.service.RecoveryFileService;
import org.springframework.stereotype.Service;

/**
 * 文件恢复服务接口实现
 */
@Service
public class RecoveryFileServiceImpl extends ServiceImpl<RecoveryFileMapper, RecoveryFile> implements RecoveryFileService {

}
