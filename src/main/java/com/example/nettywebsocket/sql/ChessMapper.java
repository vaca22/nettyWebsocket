package com.example.nettywebsocket.sql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChessMapper {

    @Select("SELECT * FROM five_chess_user WHERE name = #{name}")
    ChessUser findByName(@Param("name") String name);

}