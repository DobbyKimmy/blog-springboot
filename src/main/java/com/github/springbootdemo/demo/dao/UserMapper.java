package com.github.springbootdemo.demo.dao;

import com.github.springbootdemo.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
@Service
public interface UserMapper {
    @Select("select * from user where id = #{id}")
    User selectUserById(@Param("id") Integer id);

    @Select("select * from user where username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Insert("insert into user(username,encrypted_password,created_at,updated_at)" +
            "values(#{username},#{encryptedPassword},now(),now())")
    void save(@Param("username") String username, @Param("encryptedPassword") String encryptedPassword);

}
