package com.luckysj.demo.controller;


import com.luckysj.demo.model.request.UserReq;
import com.luckysj.demo.service.UserService;
import com.luckysj.demo.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/add")
    @OperationLog("添加用户")// 这里可以写上操作日志的描述
    public ResponseEntity<String> addUser(@RequestBody UserReq addReq) {
        return userService.addUser(addReq);
    }


}
