package com.luckysj.demo.concurrent;


public interface UserService {
    Long countFansCountByUserId(Long userId);

    Long countMsgCountByUserId(Long userId);

    Long countCollectCountByUserId(Long userId);

    Long countFollowCountByUserId(Long userId);

    Long countRedBagCountByUserId(Long userId);

    Long countCouponCountByUserId(Long userId);

    Long countArticleCountByUserId(Long userId);

    Long countLikeCountByUserId(Long userId);
}
