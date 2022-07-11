package com.example.model.file;

import lombok.Data;

import java.util.List;

/**
 * 搜索条件表单
 */
@Data
public class SearchForm {
    /**
     * 搜索关键字
     */
    private String keyword;
    /**
     * 所选的标签id
     */
    private List<Integer> tagIds;
    /**
     * 分页参数*2
     */
    private Integer pageNum;
    private Integer pageSize;
    //↓日后更多条件添加


}
