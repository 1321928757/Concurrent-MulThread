package com.luckysj.demo.concurrent;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Luckysj @刘仕杰
 * @description 用户控制器
 * @create 2024/03/19 21:48:30
 */
@RestController
@RequestMapping("api/user")
public class UserController {


    @Autowired
    private MyFutureTask myFutureTask;


    @GetMapping("/index")
    @ResponseBody
    public String index() {
        return "启动用户模块成功~~~~~~~~";
    }

    //http://localhost:8080/api/user/get/data?userId=4
    /**
     * java并发查询数据实战 提高接口响应速度
     * @param userId
     * @return
     */
    @GetMapping("/get/data")
    @ResponseBody
    public UserBehaviorDataDTO getUserData(Long userId) {
        System.out.println("UserController的线程:" + Thread.currentThread());
        long begin = System.currentTimeMillis();
        UserBehaviorDataDTO userAggregatedResult = myFutureTask.getUserAggregatedResult(userId);
        long end = System.currentTimeMillis();
        System.out.println("===============总耗时:" + (end - begin) / 1000.0000 + "秒");
        return userAggregatedResult;
    }


}