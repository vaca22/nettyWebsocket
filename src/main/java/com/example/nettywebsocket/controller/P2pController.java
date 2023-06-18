package com.example.nettywebsocket.controller;

import com.example.nettywebsocket.dao.P2pMapper;
import com.example.nettywebsocket.model.P2pTable;
import com.example.nettywebsocket.model.P2pUser;
import io.netty.channel.Channel;
import org.json.JSONObject;
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

        List<P2pTable> data = p2pMapper.findByPhone(phone);
        int index = 0;
        for (P2pTable k : data) {
            Channel b = channelMap.get(k.device);
            if (b != null) {
                data.get(index).status = 1;
            } else {
                data.get(index).status = 0;
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


    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestBody P2pUser data) throws IOException {
        JSONObject json = new JSONObject();
        String password = p2pMapper.findPasswordByPhone(data.getPhone());
        if (password != null) {
            json.put("code", 1);
            json.put("msg", "phone already exists");
            return json.toString();
        }
        p2pMapper.insertP2pUser(data.getPhone(), data.getPassword());
        json.put("code", 0);
        json.put("msg", "ok");
        return json.toString();
    }

    //delete
    @PostMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody P2pTable data) throws IOException {
        p2pMapper.deleteP2pUser(data.getPhone());
        return "ok";
    }

    //login
    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody P2pUser data) throws IOException {
        String password = p2pMapper.findPasswordByPhone(data.getPhone());
        JSONObject json = new JSONObject();
        if (password == null) {
            json.put("code", 1);
            json.put("msg", "phone not exists");
            return json.toString();
        }
        if (!password.equals(data.getPassword())) {
            json.put("code", 2);
            json.put("msg", "password error");
            return json.toString();
        }
        json.put("code", 0);
        json.put("msg", "ok");
        return json.toString();
    }

    @PostMapping("/update/note")
    @ResponseBody
    public String uploadNote(@RequestBody P2pTable data) throws IOException {
        p2pMapper.updateNote(data.getPhone(), data.getDevice(), data.getNote());
        return "ok";
    }
}
