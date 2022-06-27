package com.example.service.file;

import com.example.model.file.FileResVo;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class FileService {

    private static final String BASE_URL = "Z:\\所有归档动画\\";

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


}
