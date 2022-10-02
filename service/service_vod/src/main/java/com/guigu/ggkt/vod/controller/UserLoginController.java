package com.guigu.ggkt.vod.controller;

import com.guigu.ggkt.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/admin/vod/user")
//@CrossOrigin
public class UserLoginController {

//    {"code":20000,"data":{"token":"admin-token"}}
    @PostMapping("login")
    public Result login(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }

    @GetMapping("info")
    public Result info(){
        HashMap<String, Object> map = new HashMap<>();
//        {"code":20000,"data":{
//        "roles":["admin"],
//        "introduction":"I am a super administrator",
//        "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
//        "name":"Super Admin"}}
        map.put("token","admin");
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return Result.ok(map);
    }
}
