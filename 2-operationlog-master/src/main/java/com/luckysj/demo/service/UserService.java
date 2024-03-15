package com.luckysj.demo.service;

import com.luckysj.demo.model.request.UserReq;
import org.springframework.http.ResponseEntity;

public interface UserService {


    /**
     * 添加用户
     *
     * @param addReq 参数
     * @return 添加结果
     */
    ResponseEntity<String> addUser(UserReq addReq);


}
