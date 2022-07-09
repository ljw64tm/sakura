package com.example.util;

import com.example.common.enums.EnumType;
import com.example.common.enums.WebSupport;

import java.util.HashMap;
import java.util.Map;

public class EnumHelper {

    /**
     * 反射找枚举
     *
     * @param id
     * @return
     */
    public static Map<Integer, String> getEnumById(Integer id) {
        EnumType et = EnumType.valueOf(id);
        if (et == null) {
            return new HashMap<>();
        }
        try {

            WebSupport webSupport = (WebSupport)  Enum.valueOf(et.getWebSupportClass(), "PLOT");
            return webSupport.getALl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
