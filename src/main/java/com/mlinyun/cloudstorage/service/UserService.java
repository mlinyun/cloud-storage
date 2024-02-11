package com.mlinyun.cloudstorage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mlinyun.cloudstorage.common.RestResult;
import com.mlinyun.cloudstorage.model.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册服务
     *
     * @param user 要注册的用户
     * @return 注册结果
     */
    RestResult<String> registerUser(User user);

    /**
     * 用户登录服务
     *
     * @param user 要登录的用户
     * @return 登录结果
     */
    RestResult<User> login(User user);

}
