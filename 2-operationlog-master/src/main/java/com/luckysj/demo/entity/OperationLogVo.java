package com.luckysj.demo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("operation_log")
public class OperationLogVo {

    @TableId(type = IdType.AUTO)
    private Long logId;

    private String type;

    @TableField("request_uri")
    private String uri;

    private String name;

    @TableField("ip_address")
    private String ipAddress;

    private String method;

    private String params;

    private String data;

    @TableField("nick_name")
    private String nickname;

    private Integer userId;

    private Long times;

    private String errorMessage;

}
