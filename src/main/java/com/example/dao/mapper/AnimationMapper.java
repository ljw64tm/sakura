package com.example.dao.mapper;

import com.example.dao.entity.AnimationEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface AnimationMapper {

    @Select("select count(*) from animation")
    int test();

    @Select("select id,name,name_pinyin as `namePinyin`,`delete`,delete_time as `deleteTime`,create_time as `createTime`,modify_time as `modifyTime` from animation where name=#{name} limit 1")
    AnimationEntity getByName(@Param("name") String name);

    @Select("select id,name,name_pinyin as `namePinyin`,`delete`,delete_time as `deleteTime`,create_time as `createTime`,modify_time as `modifyTime` from animation")
    List<AnimationEntity> getAll();

    @Insert("insert into animation(name,name_pinyin,full_name,create_time,modify_time) values(#{name},#{namePinyin},#{fullName},#{time},#{time})")
    int insert(@Param("name") String name, @Param("namePinyin") String namePinyin,@Param("fullName") String fullName, @Param("time") Date time);

    @Update("update animation set `delete` = #{delete},delete_time=#{time} where id = #{id}")
    int updateStatusById(@Param("id") Integer id, @Param("delete") Integer delete, @Param("time") Date time);

    @Select("select id,name,name_pinyin as `namePinyin`,full_name as `fullName`,`delete`,delete_time as `deleteTime`,create_time as `createTime`,modify_time as `modifyTime` from animation where name like #{keyword,jdbcType=VARCHAR} and `delete`=0 limit 20")
    List<AnimationEntity> searchByName(@Param("keyword") String keyword);

    @Select("select id,name,name_pinyin as `namePinyin`,full_name as `fullName`,`delete`,delete_time as `deleteTime`,create_time as `createTime`,modify_time as `modifyTime` from animation where name_pinyin like #{keyword,jdbcType=VARCHAR} and `delete`=0 limit 20")
    List<AnimationEntity> searchByNamePinYin(@Param("keyword") String keyword);
}
