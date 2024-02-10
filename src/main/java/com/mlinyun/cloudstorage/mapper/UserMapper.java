package com.mlinyun.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mlinyun.cloudstorage.model.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 新增用户
     *
     * @param user 用户
     */
    void insertUser(User user);

    /**
     * 查询用户
     *
     * @return 查询到的所有用户
     */
    List<User> selectUser();

}




