package com.gcusky.trans.service;

import com.gcusky.trans.error.BusinessException;
import com.gcusky.trans.service.model.UserModel;

public interface UserService {

    // 通过用户 ID 获取用户对象的方法
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /**
     * @param telephone 用户注册手机
     * @param encrptPassword 用户加密后的密码
     * @throws BusinessException
     */
    UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException;
}
