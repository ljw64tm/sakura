package com.example.controller.file;

import com.example.model.file.FileResVo;
import com.example.model.file.Resp;
import com.example.model.file.SearchForm;
import com.example.service.file.FileService;
import com.example.service.file.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
        BufferedInputStream bis = null;
        try {
            File file = fileService.getFullUrl(index, subIndex);
            if (file.exists()) {
                long p = 0L;
                long toLength = 0L;
                long contentLength = 0L;
                int rangeSwitch = 0;
                long fileLength;
                String rangBytes = "";
                fileLength = file.length();
// get file content
                InputStream ins = new FileInputStream(file);
                bis = new BufferedInputStream(ins);
// tell the client to allow accept-ranges
                response.reset();
                response.setHeader("Accept-Ranges", "bytes");
// client requests a file block download start byte
                String range = request.getHeader("Range");
                if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                    response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                    rangBytes = range.replaceAll("bytes=", "");
                    if (rangBytes.endsWith("-")) { // bytes=270000-
                        rangeSwitch = 1;
                        p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                        contentLength = fileLength - p; // 客户端请求的是270000之后的字节(包括bytes下标索引为270000的字节)
                    } else { // bytes=270000-320000
                        rangeSwitch = 2;
                        String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                        String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
                        p = Long.parseLong(temp1);
                        toLength = Long.parseLong(temp2);
                        contentLength = toLength - p + 1; // 客户端请求的是 270000-320000 之间的字节
                    }
                } else {
                    contentLength = fileLength;
                }
// 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
// Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
                response.setHeader("Content-Length", new Long(contentLength).toString());
// 断点开始
// 响应的格式是:
// Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                if (rangeSwitch == 1) {
                    String contentRange = new StringBuffer("bytes ").append(new Long(p).toString()).append("-")
                            .append(new Long(fileLength - 1).toString()).append("/")
                            .append(new Long(fileLength).toString()).toString();
                    response.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else if (rangeSwitch == 2) {
                    String contentRange = range.replace("=", " ") + "/" + new Long(fileLength).toString();
                    response.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else {
                    String contentRange = new StringBuffer("bytes ").append("0-").append(fileLength - 1).append("/").append(fileLength).toString();
                    response.setHeader("Content-Range", contentRange);
                }
                String fileName = file.getName();
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                OutputStream out = response.getOutputStream();
                int n = 0;
                long readLength = 0;
                int bsize = 1024;
                byte[] bytes = new byte[bsize];
                if (rangeSwitch == 2) {
// 针对 bytes=27000-39000 的请求，从27000开始写数据
                    while (readLength <= contentLength - bsize) {
                        n = bis.read(bytes);
                        readLength += n;
                        out.write(bytes, 0, n);
                    }
                    if (readLength <= contentLength) {
                        n = bis.read(bytes, 0, (int) (contentLength - readLength));
                        out.write(bytes, 0, n);
                    }
                } else {
                    while ((n = bis.read(bytes)) != -1) {
                        out.write(bytes, 0, n);
                    }
                }
                out.flush();
                out.close();
                bis.close();
            }
        } catch (IOException ie) {
// 忽略 ClientAbortException 之类的异常
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
