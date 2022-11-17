package com.example.nettywebsocket.http;

import com.example.nettywebsocket.sql.ChessMapper;
import com.example.nettywebsocket.sql.ChessUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;

import static org.apache.xmlbeans.impl.common.XBeanDebug.log;

@RestController
@RequestMapping("/cloud-napi/v1")
@Slf4j
public class UserInfoController {
    @GetMapping("/serverIndex")
    public ModelAndView hello(){
        return new ModelAndView("index");
    }

    @Autowired
    ChessMapper chessMapper;

    @ResponseBody
    @RequestMapping("/")
    public String hello2() {
        ChessUser chessUser= chessMapper.findByName("San Francisco");
        UserInfoController.log.error("fuck  "+chessUser.getHeader_url());
        return "Hello World!";
    }



    @ResponseBody
    @PostMapping("/update")
    public String hello2(MultipartFile file) throws IOException {
        if(file.isEmpty()){
            System.out.println("fuck");
        }


        String name="yi.apk";
        String name2=name;
        File myObj = new File(name2);

        if(myObj.exists()){
            myObj.delete();
        }

        file.transferTo(myObj.getAbsoluteFile());
        System.out.println(myObj.getAbsolutePath()+"  "+  file.getOriginalFilename());

        return "Hello W22orld!";
    }


    @PostMapping("/upload/info")
    @ResponseBody
    public String uploadUserInfo(@RequestBody ChessUser chessUser) throws IOException {


        chessMapper.updateById(chessUser.getUid(),chessUser.getName(),chessUser.getHeader_url());
        log.error("NettyServerError:" +chessUser.getHeader_url()+"  "+chessUser.getUid());
        System.out.println("fuck    "+chessUser.getName());
        return "Hello W22orld!";
    }
}
