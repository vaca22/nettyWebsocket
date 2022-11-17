package com.example.nettywebsocket.sql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChessMapper {

    @Select("SELECT * FROM five_chess_user WHERE name = #{name}")
    ChessUser findByName(@Param("name") String name);

    @Select("SELECT * FROM five_chess_user")
    List<ChessUser> findAll();
    @Select("SELECT * FROM five_chess_user WHERE uid = #{uid}")
    ChessUser findById(@Param("uid") String uid);


    @Select("replace into five_chess_user (name, uid, header_url) values (#{uid}, #{name}, #{header_url})")
    ChessUser updateById(@Param("uid") String uid,@Param("name") String name,@Param("header_url") String header_url);


    @Select("insert into five_chess_user (uid, name, header_url) values (#{uid}, #{name}, #{header_url}) on duplicate key update header_url=#{header_url}")
    ChessUser updateHeader(@Param("uid") String uid,@Param("name") String name,@Param("header_url") String header_url);
}