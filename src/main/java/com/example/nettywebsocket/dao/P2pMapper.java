package com.example.nettywebsocket.dao;

import com.example.nettywebsocket.model.P2pTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface P2pMapper {

  @Select("SELECT * FROM p2p_table") //SQL
  List<P2pTable> findAll( );

  @Select("SELECT * FROM p2p_table WHERE phone = #{phone}")
  List<P2pTable> findByPhone(@Param("phone") String phone);

}

