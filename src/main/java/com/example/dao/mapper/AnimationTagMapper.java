package com.example.dao.mapper;

import com.example.dao.entity.TagEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 动画《》标签关系
 */
@Mapper
public interface AnimationTagMapper {

    @Insert("insert into animation_tag(animation_id,tag_id) values(#{animationId},#{tagId})")
    int add(@Param("animationId") Integer animationId, @Param("tagId") Integer tagId);

    @Delete("delete from animation_tag where animation_id=#{animationId} and tag_id=#{tagId}")
    int delete(@Param("animationId") Integer animationId, @Param("tagId") Integer tagId);

    @Select("select t.id as id,t.name as name from animation_tag as at inner join tag as t on at.tag_id=t.id where animation_id=#{animationId}")
    List<TagEntity> getByAnimationId(Integer animationId);
}
