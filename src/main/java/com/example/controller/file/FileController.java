package com.example.controller.file;

import com.example.model.file.FileResVo;
import com.example.model.file.Resp;
import com.example.service.file.FileService;
import com.example.service.file.SearchService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private SearchService searchService;

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/index")
    private Resp<List<FileResVo>> index(@RequestParam(required = false, defaultValue = "") String subIndex) {
        return Resp.success(fileService.index(subIndex));
    }

    @GetMapping("/search")
    private Resp<List<FileResVo>> search(@RequestParam(required = false, defaultValue = "") String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return Resp.success(new ArrayList<>());
        }
        return Resp.success(searchService.search(keyword));
    }

    @GetMapping("/download")
    public void downloadLocal(@RequestParam(required = false, defaultValue = "") String index,
                              @RequestParam(required = false, defaultValue = "") String subIndex, HttpServletResponse response) {
        File downloadFile = fileService.getFullUrl(index, subIndex);
        if (downloadFile.isDirectory()) {
            response.setStatus(500);
            return;
        }
        try {
            log.info("有人开始下载：" + index + subIndex);
            InputStream inStream = new FileInputStream(downloadFile);// 文件的存放路径
            response.reset();
            response.setContentType("bin");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + subIndex + "\"");
            byte[] b = new byte[1000];
            int len;
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
            log.info("有人下完了：" + index + subIndex);
        } catch (IOException e) {
            log.info("有人下载出错了：" + index + subIndex);
        }
    }

}
