package com.example.service.user;

import com.example.dao.entity.UserEntity;
import com.example.dao.mapper.UserMapper;
import com.example.model.user.LoginFrom;
import com.example.model.user.RegisterFrom;
import com.example.model.user.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserService {

    private String INVITATION_CODE = "NDOR5-3435F-DDDPE-URS93-44GVV";

    @Autowired
    private UserMapper userMapper;

    /**
     * 检查登录
     *
     * @param session
     */
    public UserInfoVo loginCheck(HttpSession session) {
        Object sessionObj = session.getAttribute("user");
        if (sessionObj == null) {
            return null;
        }
        UserEntity userEntity = (UserEntity) sessionObj;
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId(userEntity.getId());
        userInfoVo.setLoginName(userEntity.getLoginName());
        userInfoVo.setNickName(userEntity.getNickName());
        return userInfoVo;
    }

    /**
     * 注册
     *
     * @param registerFrom
     * @return
     */
    public String register(RegisterFrom registerFrom) {
        UserEntity userEntity = userMapper.getByLoginName(registerFrom.getLoginName());
        if (userEntity != null) {
            return "该账号已被注册";
        }
        if (!registerFrom.getInvitationCode().equals(INVITATION_CODE)) {
            return "邀请码错误";
        }
        userEntity = new UserEntity();
        userEntity.setLoginName(registerFrom.getLoginName());
        userEntity.setPassword(this.MD5(registerFrom.getPassword()));
        userEntity.setNickName(registerFrom.getNickName());
        userEntity.setInvitationId(0);
        userEntity.setInvitationCode(registerFrom.getInvitationCode());
        userMapper.insert(userEntity);
        return null;
    }

    /**
     * 登录
     *
     * @param loginFrom
     * @return
     */
    public String login(LoginFrom loginFrom, HttpSession session) {
        UserEntity userEntity = userMapper.getByLoginName(loginFrom.getLoginName());
        if (userEntity == null) {
            return "账号不存在";
        }
        if (!this.MD5(loginFrom.getPassword()).equals(userEntity.getPassword())) {
            return "密码错误";
        }
        session.setAttribute("user", userEntity);
        return null;
    }

    /**
     * MD5加密
     *
     * @param input
     * @return
     */
    private String MD5(String input) {
        //暂时简单实现
        return "123" + input;
    }

}
