package com.luckysj.demo.entity;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@TableName("t_user")
@NoArgsConstructor
@AllArgsConstructor
public class UserDO  {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户手机号
     */
    @Column(name = "user_phone")
    private String userPhone;

    /**
     * 用户名称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户密码
     */
    @Column(name = "password")
    private String password;
}