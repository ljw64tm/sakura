package com.example.dao.mapper;

import com.example.dao.entity.TagEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper {

    @Select("select * from tag order by sort")
    List<TagEntity> getAll();

}
