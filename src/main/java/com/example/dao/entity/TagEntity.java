package com.example.dao.entity;

import lombok.Data;

/**
 * 标签实体
 */
@Data
public class TagEntity {

    private Integer id;
    private String name;
    private Integer type;
    private Integer sort;
}
