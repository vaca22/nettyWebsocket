package com.example.nettywebsocket.controller;

import com.example.nettywebsocket.model.P2pTable;
import com.example.nettywebsocket.dao.P2pMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class P2pController {

  @Autowired
  private P2pMapper p2pMapper;


//  JSONArray jsa = JSONArray.fromObject(list);
//String result = jsa.toString();
  @RequestMapping("/")
  public List<P2pTable> home() {
    return p2pMapper.findAll();
  }

  @RequestMapping("/p2p")
  public List<P2pTable> books() {
    return home();
  }

  @RequestMapping("/p2p/{phone}")
  public List<P2pTable> bookByIsbn(@PathVariable("phone") String phone) {
    return p2pMapper.findByPhone( phone);
  }
}
