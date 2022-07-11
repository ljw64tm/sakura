package com.example.model.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 文件响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResVo implements Comparable {

    private Integer id;

    private String name;

    private String fullName;

    private boolean file;

    private String size;

    private List<TagVo> tags;

    public FileResVo(String name, boolean isFile) {
        this.name = name;
        this.file = isFile;
    }

    public FileResVo(Integer id, String name, String fullName, boolean file) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.file = file;
    }

    @Override
    public int compareTo(Object o) {
        FileResVo p2 = (FileResVo) o;
        Comparator<Object> compare = Collator.getInstance(Locale.CHINA);
        return compare.compare(this.getName(), p2.getName());
    }

}
