package com.example.model.user;

import lombok.Data;

/**
 * 注册专用表单
 */
@Data
public class RegisterFrom {

    private String  loginName;
    private String  password;
    private String  nickName;
    private String  invitationCode;

}
