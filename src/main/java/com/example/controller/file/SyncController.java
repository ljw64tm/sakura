package com.example.controller.file;

import com.example.model.file.Resp;
import com.example.service.file.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据同步相关
 */
@RestController
@RequestMapping("/sync")
public class SyncController {

    @Autowired
    private SyncService syncService;

    /**
     * 正向同步，硬盘中的动画目录将会新增至数据库中（如果存在跳过）
     *
     * @return
     */
    @GetMapping("/disk2db")
    public Resp disk2db() {
        String err = syncService.disk2db();
        if (err == null) {
            return Resp.success();
        } else {
            return Resp.fail(err);
        }
    }

    /**
     * 反向同步，数据库中存在，硬盘已不存在，将会删除数据库中信息
     *
     * @return
     */
    @GetMapping("/db2disk")
    public Resp db2disk() {
        String err = syncService.db2disk();
        if (err == null) {
            return Resp.success();
        } else {
            return Resp.fail(err);
        }
    }
}
