package com.example.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户表
 */
@Data
public class UserEntity {

    private Integer id;
    private String loginName;
    private String password;
    private String nickName;
    private Integer invitationId;
    private String invitationCode;
    private Integer status;
    private Date createTime;
    private Date modifyTime;

}
