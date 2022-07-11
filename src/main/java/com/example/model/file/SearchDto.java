package com.example.model.file;

import lombok.Data;

import java.util.List;

@Data
public class SearchDto {

    /**
     * 搜索关键字
     */
    private String keyword;
    /**
     * 动画Ids
     */
    private List<Integer> animationIds;
    /**
     * 分页参数*2
     */
    private Integer pageNum;
    private Integer pageSize;
    //↓日后更多条件添加

    /**
     * 获取分页start，必要方法，名称唯一
     *
     * @return
     */
    public Integer getPageStart() {
        if (pageNum == null || pageSize == null) {
            return 0;
        }
        return pageNum * pageSize;
    }
}
