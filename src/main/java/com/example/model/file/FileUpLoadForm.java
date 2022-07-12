package com.example.model.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUpLoadForm {

    private MultipartFile file;
    private Integer id;

}
