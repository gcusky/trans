package com.gcusky.trans.service;

import com.gcusky.trans.service.model.UserModel;

public interface UserService {

    // 通过用户 ID 获取用户对象的方法
    UserModel getUserById(Integer id);
}
