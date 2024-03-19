package com.luckysj.demo.concurrent;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * @author Luckysj @刘仕杰
 * @description 一个页面可能有多达10个左右的一个用户行为数据,我们可以通过多线程来提高查询速率
 * @create 2024/03/19 21:45:04
 */
@Slf4j
@Component
public class MyFutureTask {
    @Resource
    UserService userService;

    // 线程池
    @Resource
    private ExecutorService executor;
    public UserBehaviorDataDTO getUserAggregatedResult(final Long userId) {
        System.out.println("MyFutureTask的线程:" + Thread.currentThread());
        try {
            // 1.发布文章数
            CompletableFuture<Long> articleCountFT = CompletableFuture.supplyAsync(() -> userService.countArticleCountByUserId(userId), executor);
            // 2.点赞数
            CompletableFuture<Long> LikeCountFT = CompletableFuture.supplyAsync(() -> userService.countLikeCountByUserId(userId), executor);
            // 3.粉丝数
            CompletableFuture<Long> fansCountFT = CompletableFuture.supplyAsync(() -> userService.countFansCountByUserId(userId), executor);
            // 4.消息数
            CompletableFuture<Long> msgCountFT = CompletableFuture.supplyAsync(() -> userService.countMsgCountByUserId(userId), executor);
            // 5.收藏数
            CompletableFuture<Long> collectCountFT = CompletableFuture.supplyAsync(() -> userService.countCollectCountByUserId(userId), executor);
            // 6.关注数
            CompletableFuture<Long> followCountFT = CompletableFuture.supplyAsync(() -> userService.countFollowCountByUserId(userId), executor);
            // 7.红包数
            CompletableFuture<Long> redBagCountFT = CompletableFuture.supplyAsync(() -> userService.countRedBagCountByUserId(userId), executor);
            // 8.卡券数
            CompletableFuture<Long> couponCountFT = CompletableFuture.supplyAsync(() -> userService.countCouponCountByUserId(userId), executor);

            // 等待全部线程执行完毕 这里一定要设超时时间，不然会一直等待
            CompletableFuture.allOf(articleCountFT, LikeCountFT, fansCountFT, msgCountFT, collectCountFT, followCountFT, redBagCountFT, couponCountFT).get(6, TimeUnit.SECONDS);

            // 必须设置合理的超时时间
            UserBehaviorDataDTO userBehaviorData = UserBehaviorDataDTO.builder().articleCount(articleCountFT.get()).likeCount(LikeCountFT.get()).fansCount(fansCountFT.get()).msgCount(msgCountFT.get()).collectCount(collectCountFT.get()).followCount(followCountFT.get()).redBagCount(redBagCountFT.get()).couponCount(couponCountFT.get()).build();
            return userBehaviorData;
        } catch (Exception e) {
            log.error("get user behavior data error", e);
            return new UserBehaviorDataDTO();
        }
    }

}