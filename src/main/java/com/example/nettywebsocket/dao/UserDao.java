package com.example.nettywebsocket.dao;

import com.example.nettywebsocket.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {


    @Select("SELECT ID, FIRST_NAME   as firstName, LAST_NAME    as lastName, USER_NAME    as userName,ACTIVE_SINCE as activeOn FROM USERS ORDER BY FIRST_NAME ASC, LAST_NAME ASC ")
    List<User> findOrderedUsers();

    @Select("SELECT * FROM USERS")
    List<User> findAll();
}