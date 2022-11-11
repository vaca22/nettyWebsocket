package com.example.nettywebsocket.http;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class StartController {
    @GetMapping("/serverIndex")
    public ModelAndView hello(){
        return new ModelAndView("index");
    }


    @ResponseBody
    @RequestMapping("/")
    public String hello2() {
        return "Hello World!";
    }

    void updateFile() throws IOException {
        File fuck=new File("update_yiyangkang.txt");
        byte[] bytes= Files.readAllBytes(Path.of("update_yiyangkang.txt"));
        System.out.println(bytes.length);
        String fileContent=new String(bytes);
        JSONObject jj=new JSONObject(fileContent);
        int version=jj.getInt("version");
        System.out.println(version);
        version++;
        jj.put("version",version);
        String outString=jj.toString();
        System.out.println(outString);
        fuck.delete();
        FileOutputStream out=new FileOutputStream("update_yiyangkang.txt");
        out.write(outString.getBytes(StandardCharsets.UTF_8));
        out.close();
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
        updateFile();
        return "Hello W22orld!";
    }
}
