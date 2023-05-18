package com.example.nettywebsocket.dao;

import com.example.nettywebsocket.model.P2pTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface P2pMapper {

    @Select("SELECT * FROM p2p_table")
        //SQL
    List<P2pTable> findAll();

    @Select("SELECT * FROM p2p_table WHERE phone = #{phone}")
    List<P2pTable> findByPhone(@Param("phone") String phone);


    @Insert("INSERT INTO p2p_table (phone, device, note) VALUES (#{phone}, #{device}, #{note})")
    void insert(@Param("phone") String phone, @Param("device") String device, @Param("note") String note);


    @Insert("UPDATE p2p_table SET note = #{note} WHERE phone = #{phone} and device = #{device}")
    void updateNote(@Param("phone") String phone, @Param("device") String device, @Param("note") String note);



    //find phone by device
    @Select("SELECT phone FROM p2p_table WHERE device = #{device}")
    List<String> findPhoneByDevice(@Param("device") String device);


}

