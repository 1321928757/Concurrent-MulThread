package com.luckysj.demo.concurrent;


import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Long countFansCountByUserId(Long userId) {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("获取FansCount===睡眠:" + 1 + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("UserService获取FansCount的线程  " + Thread.currentThread().getName());
        return 520l;
    }

    @Override
    public Long countMsgCountByUserId(Long userId) {
        System.out.println("UserService获取MsgCount的线程  " + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("获取MsgCount===睡眠:" + 1 + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 618L;
    }

    @Override
    public Long countCollectCountByUserId(Long userId) {
        System.out.println("UserService获取CollectCount的线程  " + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("获取CollectCount==睡眠:" + 2 + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 6664L;
    }

    @Override
    public Long countFollowCountByUserId(Long userId) {
        System.out.println("UserService获取FollowCount的线程  " + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("获取FollowCount===睡眠:" + 1 + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 102L;
    }

    @Override
    public Long countRedBagCountByUserId(Long userId) {
        System.out.println("UserService获取RedBagCount的线程  " + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("获取RedBagCount===睡眠:" + 1 + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 99L;
    }

    @Override
    public Long countCouponCountByUserId(Long userId) {
        System.out.println("UserService获取CouponCount的线程  " + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(0);
            System.out.println("获取CouponCount===睡眠:" + 0 + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 66L;
    }

    @Override
    public Long countArticleCountByUserId(Long userId) {
        System.out.println("UserService获取ArticleCount的线程  " + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("获取ArticleCount===睡眠:" + 1 + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 135L;
    }

    @Override
    public Long countLikeCountByUserId(Long userId) {
        System.out.println("UserService获取likeCount的线程  " + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("获取likeCount===睡眠:" + 2 + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 46L;
    }
}