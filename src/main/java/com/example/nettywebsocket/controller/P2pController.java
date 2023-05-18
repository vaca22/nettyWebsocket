package com.example.nettywebsocket.controller;

import com.example.nettywebsocket.model.P2pTable;
import com.example.nettywebsocket.dao.P2pMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class P2pController {

  @Autowired
  private P2pMapper p2pMapper;


//  JSONArray jsa = JSONArray.fromObject(list);
//String result = jsa.toString();
  @RequestMapping("/p2p")
  public List<P2pTable> home() {
    return p2pMapper.findAll();
  }



  @RequestMapping("/p2p/{phone}")
  public List<P2pTable> bookByIsbn(@PathVariable("phone") String phone) {
    return p2pMapper.findByPhone( phone);
  }

  @PostMapping("/p2p/bind")
  @ResponseBody
  public String uploadUserInfoHead(@RequestBody P2pTable data) throws IOException {
    p2pMapper.insert(data.getPhone(), data.getDevice(), data.getNote());
    return "ok";
  }

}
