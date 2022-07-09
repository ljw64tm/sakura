package com.example.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 所有映射至前端的枚举
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EnumType {

    tagType2(1,TagType.class),;
    private Integer value;
    private Class webSupportClass;

    public static EnumType valueOf(Integer val) {
        return Arrays.stream(EnumType.values()).filter(p -> p.getValue() == val.intValue()).findFirst().orElse(null);
    }

}
