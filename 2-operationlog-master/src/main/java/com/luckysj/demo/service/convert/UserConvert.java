package com.luckysj.demo.service.convert;

import com.luckysj.demo.model.request.UserReq;
import com.luckysj.demo.entity.UserDO;

public class UserConvert {
    public static UserDO toDOWhenSave(UserReq addReq) {
        UserDO userDO = new UserDO();
        userDO.setUserName(addReq.getUserName());
        userDO.setUserPhone(addReq.getUserPhone());
        return userDO;
    }
}
