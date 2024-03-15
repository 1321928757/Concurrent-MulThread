package com.luckysj.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckysj.demo.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}