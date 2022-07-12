package com.example.service.file;

import com.example.dao.entity.AnimationEntity;
import com.example.dao.mapper.AnimationMapper;
import com.example.model.file.FileResVo;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class FileService {

    //基本路径
    private static final String BASE_URL = "Z:\\所有归档动画\\";
    //动画封面保存路径
    private static final String TITLE_PIC_URL = BASE_URL + "000PIC\\";
    //支持的封面图片类型
    private static final String SUPPORT_PIC_EXT_NAMES = "jpg,bmp,png";

    @Autowired
    private AnimationMapper animationMapper;

    public List<FileResVo> index(String subIndex) {
        File rootFile = new File(BASE_URL + subIndex);
        if (!rootFile.exists()) {
            return new ArrayList<>();
        }
        if (rootFile.isFile()) {
            return new ArrayList<>();
        }
        File[] listFiles = rootFile.listFiles();
        List<FileResVo> result = new ArrayList<>();
        for (File file : listFiles) {
            FileResVo fileResVo = new FileResVo(file.getName(), file.isFile());
            if (file.isFile()) {
                fileResVo.setSize(this.fileSizeByteAuto(file.length()));
            }
            result.add(fileResVo);
        }
        Collections.sort(result);
        return result;
    }

    /**
     * 字节自动转换KB MB GB
     *
     * @param fileSizeByte
     * @return
     */
    private String fileSizeByteAuto(long fileSizeByte) {
        float fileSize = fileSizeByte;
        if (fileSize < 1000) {
            return this.shortSize(fileSize) + "B";
        }
        fileSize /= 1024;
        if (fileSize < 1000) {
            return this.shortSize(fileSize) + "KB";
        }
        fileSize /= 1024;
        if (fileSize < 1000) {
            return this.shortSize(fileSize) + "MB";
        }
        fileSize /= 1024;
        if (fileSize < 1000) {
            return this.shortSize(fileSize) + "GB";
        }
        fileSize /= 1024;
        return this.shortSize(fileSize) + "TB";
    }

    private String shortSize(float f) {
        BigDecimal b = new BigDecimal(f);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public File getFullUrl(String index, String subIndex) {
        String fullUrl = BASE_URL + index + "\\" + subIndex;
        return new File(fullUrl);
    }

    public void downloadFile(File file, HttpServletRequest request, HttpServletResponse response) {
        BufferedInputStream bis = null;
        try {
            if (file.exists()) {
                long p = 0L, toLength = 0L, contentLength = 0L, fileLength;
                int rangeSwitch = 0;
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
                    String contentRange = new StringBuffer("bytes ").append(new Long(p)).append("-")
                            .append(new Long(fileLength - 1)).append("/")
                            .append(new Long(fileLength)).toString();
                    response.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else if (rangeSwitch == 2) {
                    String contentRange = range.replace("=", " ") + "/" + new Long(fileLength);
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

    /**
     * 上传图片
     *
     * @param id
     * @param photo
     */
    public void uploadPic(Integer id, MultipartFile photo) {
        File file = this.getTitlePicFileByAnimationId(id);
        if (file == null) {
            return;
        }
        if (file.exists()) {
            //已存在先删除
            file.delete();
        }
        //扩展名
        String uploadPicName = photo.getOriginalFilename();
        String extName = uploadPicName.substring(uploadPicName.lastIndexOf("."));
        //全路径名除去扩展名
        String oldPath = file.getPath();
        String oldPathWithOutExtName = oldPath.substring(0, oldPath.lastIndexOf("."));
        //最终本次存储的名字
        file = new File(oldPathWithOutExtName + extName);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(photo.getBytes());
        } catch (Exception e) {
            log.error("上传图片出现了错误", e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    log.error("关闭流出现异常", e);
                }
            }
        }
    }

    /**
     * 下载图片
     *
     * @param id
     * @param request
     * @param response
     */
    public void downloadPic(Integer id, HttpServletRequest request, HttpServletResponse response) {
        try {
            File file = this.getTitlePicFileByAnimationId(id);
            if (file == null || !file.exists()) {
                //找不到时默认图片
                file = new File(TITLE_PIC_URL + "default.jpg");
            }
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
//            response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
//            response.addHeader("Content-Length", "" + file.length());
            response.setHeader("connection","close");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//            response.setContentType("application/octet-stream");
            Thumbnails.of(file).size(240,300).outputQuality(1.0f).toOutputStream(toClient);
            //toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 根据动画id获取图片File对象
     * 可能实际并不存在该对象
     *
     * @param id
     * @return
     */
    private File getTitlePicFileByAnimationId(Integer id) {
        AnimationEntity animationEntity = animationMapper.getById(id);
        File file = null;
        if (animationEntity == null) {
            return file;
        }
        for (String extName : SUPPORT_PIC_EXT_NAMES.split(",")) {
            file = new File(TITLE_PIC_URL + animationEntity.getName() + "." + extName);
            if (file.exists()) {
                return file;
            }
        }
        return file;
    }
}
