package com.gcusky.trans.controller;

import com.gcusky.trans.controller.viewobject.UserVO;
import com.gcusky.trans.error.BusinessException;
import com.gcusky.trans.error.EmBusinessError;
import com.gcusky.trans.response.CommonReturnType;
import com.gcusky.trans.service.UserService;
import com.gcusky.trans.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

import static com.gcusky.trans.controller.BaseController.CONTENT_TYPE_FORMED;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // 用户获取OTP短信接口 ***
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telephone") String telephone) {
        // 需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(89999);
        randomInt += 10000; // [10000, 99999]
        String otpCode = String.valueOf(randomInt);

        // 将OTP验证码同对应的手机号关联，使用httpSession的方式绑定他的手机号与otpCode *** redis
        httpServletRequest.getSession().setAttribute(telephone, otpCode);

        // 将OTP验证码通过短信通道发送给用户 *** 第三方发送
        System.out.println("telephone=" + telephone + "&otpcode=" + otpCode);

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        // 调用 service 服务获取对应 id 的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        // 若获取的对应用户信息不存在
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            // userModel.setEncrptPassword("123"); // 空指针
        }

        // 将核心领域模型用户对象转化为可供UI使用的ViewObject
        UserVO userVO = convertFromModel(userModel);

        // 返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }


}
