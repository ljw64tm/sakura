package com.example.dao.mapper;

import com.example.dao.entity.AnimationEntity;
import com.example.model.file.SearchDto;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface AnimationMapper {

    @Select("select count(*) from animation")
    int test();

    @Select("select id,name,name_pinyin as `namePinyin`,`delete`,delete_time as `deleteTime`,create_time as `createTime`,modify_time as `modifyTime` from animation where name=#{name} limit 1")
    AnimationEntity getByName(@Param("name") String name);

    @Select("select id,name,name_pinyin as `namePinyin`,`delete`,delete_time as `deleteTime`,create_time as `createTime`,modify_time as `modifyTime` from animation where id=#{id} limit 1")
    AnimationEntity getById(@Param("id") Integer id);

    @Select("select id,name,name_pinyin as `namePinyin`,`delete`,delete_time as `deleteTime`,create_time as `createTime`,modify_time as `modifyTime` from animation")
    List<AnimationEntity> getAll();

    @Insert("insert into animation(name,name_pinyin,full_name,create_time,modify_time) values(#{name},#{namePinyin},#{fullName},#{time},#{time})")
    int insert(@Param("name") String name, @Param("namePinyin") String namePinyin, @Param("fullName") String fullName, @Param("time") Date time);

    @Update("update animation set `delete` = #{delete},delete_time=#{time} where id = #{id}")
    int updateStatusById(@Param("id") Integer id, @Param("delete") Integer delete, @Param("time") Date time);

    @Select("<script>SELECT id,name,name_pinyin AS `namePinyin`,full_name AS `fullName`,`delete`,delete_time AS `deleteTime`,create_time AS `createTime`,modify_time AS `modifyTime` " +
            "FROM animation " +
            "WHERE (name LIKE #{keyword,jdbcType=VARCHAR} OR name_pinyin LIKE #{keyword,jdbcType=VARCHAR}) " +
            "AND `delete`=0 " +
            "<if test='animationIds!=null'>AND id IN <foreach collection='animationIds' index='index' item='item' open='(' separator=',' close=')'>#{item}</foreach></if> " +
            "ORDER BY id ASC " +
            "LIMIT #{pageStart},#{pageSize}</script>")
    List<AnimationEntity> searchByName(SearchDto searchDto);

}
