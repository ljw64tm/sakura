package com.example.controller.file;

import com.example.model.file.FileResVo;
import com.example.model.file.Resp;
import com.example.model.file.SearchForm;
import com.example.service.file.FileService;
import com.example.service.file.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private SearchService searchService;

    @GetMapping("/index")
    private Resp<List<FileResVo>> index(@RequestParam(required = false, defaultValue = "") String subIndex) {
        return Resp.success(fileService.index(subIndex));
    }

    @GetMapping("/search")
    private Resp<List<FileResVo>> search(SearchForm searchForm) {
        if (searchForm.getKeyword() == null) {
            searchForm.setKeyword("");
        }
        if (searchForm.getPageNum() == null) {
            searchForm.setPageNum(0);
        }
        if (searchForm.getPageSize() == null) {
            searchForm.setPageSize(10);
        }
        return Resp.success(searchService.search(searchForm));
    }

    @GetMapping("/download")
    public void downloadLocal(@RequestParam(required = false, defaultValue = "") String index,
                              @RequestParam(required = false, defaultValue = "") String subIndex,
                              HttpServletRequest request, HttpServletResponse response) {
        File file = fileService.getFullUrl(index, subIndex);
        fileService.downloadFile(file, request, response);
    }

    /**
     * 上传动画封面
     *
     * @param photo
     * @param id
     * @param request
     * @param response
     */
    @PostMapping("/uploadPic")
    public void uploadPic(MultipartFile photo, @RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前不支持文件上传");
        }
        fileService.uploadPic(id, photo);
    }

    /**
     * 加载动画封面
     *
     * @param id
     * @param request
     * @param response
     */
    @GetMapping("/downloadPic/{id}")
    public void downloadPic(@PathVariable("id") Integer id,
                            HttpServletRequest request, HttpServletResponse response) {
        fileService.downloadPic(id, request, response);
    }

    /**
     * 防止无参情况爆炸
     * @param request
     * @param response
     */
    @GetMapping("/downloadPic/")
    public void downloadPic(    HttpServletRequest request, HttpServletResponse response) {
        fileService.downloadPic(-1, request, response);
    }

}
