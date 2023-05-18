package com.example.nettywebsocket.controller;

import com.example.nettywebsocket.dao.P2pMapper;
import com.example.nettywebsocket.model.P2pTable;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.example.nettywebsocket.MyWebSocketHandler.channelMap;

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
  public List<P2pTable> findDeviceByPhone(@PathVariable("phone") String phone) {

    List<P2pTable>  data=p2pMapper.findByPhone( phone);
    int index=0;
    for(P2pTable k : data){
      Channel b=channelMap.get(k.device);
      if(b!=null){
        data.get(index).status=1;
      }else{
        data.get(index).status=0;
      }
      index++;
    }

    return data;
  }

  @PostMapping("/bind")
  @ResponseBody
  public String uploadUserInfo(@RequestBody P2pTable data) throws IOException {
    p2pMapper.insert(data.getPhone(), data.getDevice(), data.getNote());
    return "ok";
  }


  @PostMapping("/update/note")
  @ResponseBody
  public String uploadNote(@RequestBody P2pTable data) throws IOException {
    p2pMapper.updateNote(data.getPhone(), data.getDevice(), data.getNote());
    return "ok";
  }
}
