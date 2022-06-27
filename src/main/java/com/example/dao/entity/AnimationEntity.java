package com.example.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * 动画表
 */
@Data
public class AnimationEntity {

    private Integer id;
    private String name;
    private String namePinyin;
    private String fullName;
    private Integer delete;
    private Date deleteTime;
    private Date createTime;
    private Date modifyTime;

}
