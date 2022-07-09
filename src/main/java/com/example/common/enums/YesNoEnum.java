package com.example.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum YesNoEnum {

    YES(1, "是"),
    NO(0, "否"),
    ;

    private Integer value;
    private String description;

}
