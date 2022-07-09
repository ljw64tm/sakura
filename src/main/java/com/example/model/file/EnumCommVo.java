package com.example.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 枚举通用返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnumCommVo {

    private Integer key;
    private String value;
}
