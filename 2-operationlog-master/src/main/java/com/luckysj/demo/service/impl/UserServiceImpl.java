package com.luckysj.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luckysj.demo.dao.UserMapper;
import com.luckysj.demo.entity.UserDO;
import com.luckysj.demo.model.request.UserReq;
import com.luckysj.demo.service.UserService;
import com.luckysj.demo.service.convert.UserConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userRepository;


    @Override
    public ResponseEntity<String> addUser(UserReq addReq) {
        //构建用户对象
        UserDO userDO = UserConvert.toDOWhenSave(addReq);

        UserDO userPhone = userRepository.selectOne(new QueryWrapper<UserDO>().eq("user_phone", userDO.getUserPhone()));
        if (userPhone != null) {
            return ResponseEntity.ok("用户已存在");
        }
        //添加数据库
        int insert = userRepository.insert(userDO);
        if (insert == 0) {
            return ResponseEntity.ok("添加用户失败");
        }
        //添加用户状态
        return ResponseEntity.ok("添加用户成功");
    }


}
