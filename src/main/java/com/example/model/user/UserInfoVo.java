package com.example.model.user;

import lombok.Data;

/**
 * 用户信息
 * 用户返回至前端
 */
@Data
public class UserInfoVo {

    private Integer userId;
    private String loginName;
    private String nickName;

}
