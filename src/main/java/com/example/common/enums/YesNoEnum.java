package com.example.common.enums;

public enum YesNoEnum {

    YES(1, "是"),
    NO(0, "否"),
    ;

    private Integer value;
    private String description;

    YesNoEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
