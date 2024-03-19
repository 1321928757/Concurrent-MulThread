package com.luckysj.demo.concurrent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Luckysj @刘仕杰
 * @description 信息聚合对象
 * @create 2024/03/19 21:48:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBehaviorDataDTO {

    //用户ID
    private Long userId ;

    //发布文章数
    private Long articleCount ;

    //点赞数
    private Long likeCount ;

    //粉丝数
    private Long fansCount ;

    //消息数
    private Long msgCount ;

    //收藏数
    private Long collectCount ;

    //关注数
    private Long followCount ;

    //红包数
    private Long redBagCount ;

    // 卡券数
    private Long couponCount ;

}
