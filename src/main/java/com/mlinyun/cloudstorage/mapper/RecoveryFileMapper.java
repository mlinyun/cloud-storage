package com.mlinyun.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mlinyun.cloudstorage.model.RecoveryFile;
import com.mlinyun.cloudstorage.vo.RecoveryFileListVO;

import java.util.List;

/**
 * RecoveryFileMapper 接口
 */
public interface RecoveryFileMapper extends BaseMapper<RecoveryFile> {

    List<RecoveryFileListVO> selectRecoveryFileList();

}




