package com.example.nettywebsocket.sql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChessMapper {

    @Select("SELECT * FROM five_chess_user WHERE name = #{name}")
    ChessUser findByName(@Param("name") String name);

    @Select("replace into five_chess_user (name, uid, header_url) values (#{uid}, #{name}, #{header_url})")
    ChessUser updateById(@Param("uid") String uid,@Param("name") String name,@Param("header_url") String header_url);


    @Select("insert into five_chess_user (uid, name, header_url) values (#{uid}, #{name}, #{header_url}) on duplicate key update header_url=#{header_url}")
    ChessUser updateHeader(@Param("uid") String uid,@Param("name") String name,@Param("header_url") String header_url);
}