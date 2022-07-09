package com.example.controller.comm;

import com.example.model.file.EnumCommVo;
import com.example.model.file.Resp;
import com.example.util.EnumHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enum")
public class EnumController {

    /**
     * 获取枚举
     */
    @GetMapping("/getById/{id}")
    public Resp<List<EnumCommVo>> getById(@PathVariable("id") Integer id) {
        List<EnumCommVo> result = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : EnumHelper.getEnumById(id).entrySet()) {
            result.add(new EnumCommVo(entry.getKey(), entry.getValue()));
        }
        return Resp.success(result);
    }

}
