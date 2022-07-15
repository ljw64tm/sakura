package com.example.dao.mapper;

import com.example.dao.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select id,login_name as `loginName`,password,nick_name as `nickName`,invitation_id as `invitationId`,invitation_code as `invitationCode`,status,create_time as `createTime`,modify_time as `modifyTime`" +
            "from user where login_name= #{loginName} limit 1 ")
    UserEntity getByLoginName(@Param("loginName") String loginName);

    @Insert("insert into user(login_name,password,nick_name,invitation_id,invitation_code,create_time,modify_time)values(#{loginName},#{password},#{nickName},#{invitationId},#{invitationCode},now(),now()) ")
    int insert(UserEntity userEntity);
}
