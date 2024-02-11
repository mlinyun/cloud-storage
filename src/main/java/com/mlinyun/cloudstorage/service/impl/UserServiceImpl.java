package com.mlinyun.cloudstorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlinyun.cloudstorage.common.RestResult;
import com.mlinyun.cloudstorage.common.ResultCodeEnum;
import com.mlinyun.cloudstorage.exception.BusinessException;
import com.mlinyun.cloudstorage.mapper.UserMapper;
import com.mlinyun.cloudstorage.model.User;
import com.mlinyun.cloudstorage.service.UserService;
import com.mlinyun.cloudstorage.util.DateUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    /**
     * 用户注册服务实现
     *
     * @param user 要注册的用户
     * @return 注册结果
     */
    @Override
    public RestResult<String> registerUser(User user) {
        // 获取注册时的手机号和密码
        String telephone = user.getTelephone();
        String password = user.getPassword();
        // 非空判断
        if (!StringUtils.hasLength(telephone) || !StringUtils.hasLength(password)) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL, "手机号或密码不能为空！");
        }
        // 验证手机号是否存在
        if (isTelephoneExit(telephone)) {
            return RestResult.fail().message("手机号已存在！");
        }
        // 盐值
        String salt = UUID.randomUUID().toString().replace("-", "").substring(15);
        String passwordAndSalt = password + salt;
        // 密码加密
        String newPassword = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes(StandardCharsets.UTF_8));
        // 设置用户盐值，密码，创建时间
        user.setSalt(salt);
        user.setPassword(newPassword);
        user.setRegisterTime(DateUtil.getCurrentTime());
        // 将用户保存到数据库中
        int result = userMapper.insert(user);
        // 返回注册结果
        if (result == 1) {
            return RestResult.success().message("用户注册成功！");
        } else {
            return RestResult.fail().message("注册用户失败，请检查输入信息！");
        }
    }

    @Override
    public RestResult<User> login(User user) {
        // 获取登录时的手机号和密码
        String telephone = user.getTelephone();
        String password = user.getPassword();
        // 非空判断
        if (!StringUtils.hasLength(telephone) || !StringUtils.hasLength(password)) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL, "手机号或密码不能为空！");
        }
        // 根据手机号查询用户数据
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getTelephone, telephone);
        User saveUser = userMapper.selectOne(lambdaQueryWrapper);
        // 获取用户盐值，
        String salt = saveUser.getSalt();
        String passwordAntSalt = password + salt;
        // 加密后的密码
        String newPassword = DigestUtils.md5DigestAsHex(passwordAntSalt.getBytes(StandardCharsets.UTF_8));
        // 验证密码是否正确
        if (newPassword.equals(saveUser.getPassword())) {
            // 如果密码正确，返回脱敏后的用户数据
            saveUser.setPassword("");
            saveUser.setSalt("");
            return RestResult.success().data(saveUser);
        } else {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR, "手机号或密码错误！");
        }
    }

    /**
     * 判断手机号是否存在
     *
     * @param telephone 手机号
     * @return true：存在 false：不存在
     */
    private boolean isTelephoneExit(String telephone) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getTelephone, telephone);
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        return list != null && !list.isEmpty();
    }

}




