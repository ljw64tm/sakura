package com.example.controller.user;

import com.example.model.file.Resp;
import com.example.model.user.LoginFrom;
import com.example.model.user.RegisterFrom;
import com.example.model.user.UserInfoVo;
import com.example.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 检查是否在登录中
     *
     * @return
     */
    @GetMapping("/loginCheck")
    public Resp<UserInfoVo> loginCheck(HttpSession session) {
        return Resp.success(userService.loginCheck(session));
    }

    /**
     * 登录
     *
     * @param loginFrom
     * @return
     */
    @PostMapping("/login")
    public Resp login(@RequestBody LoginFrom loginFrom, HttpSession session) {
        String err = this.loginValidate(loginFrom);
        if (err != null) {
            return Resp.fail(err);
        }
        err = userService.login(loginFrom, session);
        if (err != null) {
            return Resp.fail(err);
        }
        return Resp.success();
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping("/logout")
    public Resp logout(HttpSession session) {
        session.setAttribute("user", null);
        return Resp.success();
    }

    /**
     * 注册
     *
     * @param registerFrom
     * @return
     */
    @PostMapping("/register")
    public Resp register(@RequestBody RegisterFrom registerFrom) {
        String err = this.registerValidate(registerFrom);
        if (err != null) {
            return Resp.fail(err);
        }
        err = userService.register(registerFrom);
        if (err != null) {
            return Resp.fail(err);
        }
        return Resp.success();
    }

    /**
     * 注册前验证
     *
     * @param registerFrom
     * @return
     */
    private String registerValidate(RegisterFrom registerFrom) {
        if (registerFrom == null) {
            return "表单提交错误";
        }
        if (StringUtils.isBlank(registerFrom.getLoginName())) {
            return "登录名称不能为空";
        }
        if (StringUtils.isBlank(registerFrom.getPassword())) {
            return "密码不能为空";
        }
        if (StringUtils.isBlank(registerFrom.getNickName())) {
            return "昵称不能为空";
        }
        if (StringUtils.isBlank(registerFrom.getInvitationCode())) {
            return "邀请码不能为空";
        }
        if (registerFrom.getLoginName().length() > 20) {
            return "登录名称长度不能超过20个字符";
        }
        if (registerFrom.getPassword().length() > 20) {
            return "密码不能超过20个字符";
        }
        if (registerFrom.getNickName().length() > 20) {
            return "昵称长度不能超过20个字符";
        }
        return null;
    }

    /**
     * 登录验证
     *
     * @param loginFrom
     * @return
     */
    private String loginValidate(LoginFrom loginFrom) {
        if (loginFrom == null) {
            return "表单提交错误";
        }
        if (StringUtils.isBlank(loginFrom.getLoginName())) {
            return "登录名称不能为空";
        }
        if (StringUtils.isBlank(loginFrom.getPassword())) {
            return "密码不能为空";
        }
        return null;
    }
}
