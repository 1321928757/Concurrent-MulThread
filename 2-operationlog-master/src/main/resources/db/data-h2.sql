CREATE TABLE `operation_log` (
                                 `log_id` BIGINT(32) AUTO_INCREMENT NOT NULL COMMENT '日志主键',
                                 `type` VARCHAR(20) DEFAULT NULL COMMENT '日志类型',
                                 `request_uri` VARCHAR(300) DEFAULT NULL COMMENT 'URI',
                                 `name` VARCHAR(100) DEFAULT NULL COMMENT '日志标题',
                                 `ip_address` VARCHAR(20) DEFAULT NULL COMMENT '请求IP',
                                 `method` VARCHAR(300) DEFAULT NULL COMMENT '请求方式',
                                 `params` TEXT COMMENT '提交参数',
                                 `data` TEXT COMMENT '返回数据',
                                 `nick_name` VARCHAR(32) DEFAULT NULL COMMENT '昵称',
                                 `user_id` BIGINT(32) DEFAULT NULL COMMENT '用户id',
                                 `times` BIGINT(32) DEFAULT NULL COMMENT '请求时间戳',
                                 `status` INT(11) DEFAULT NULL COMMENT '日志状态',
                                 `error_message` TEXT COMMENT '异常',
                                 PRIMARY KEY (`log_id`)
) ENGINE=INNODB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `t_user` (
                          `user_id` BIGINT(32) AUTO_INCREMENT	NOT NULL COMMENT '用户id',
                          `user_phone` VARCHAR(20) DEFAULT NULL COMMENT '用户手机号',
                          `user_name` VARCHAR(300) DEFAULT NULL COMMENT '用户名',
                          `password` VARCHAR(100) DEFAULT NULL COMMENT '密码',
                          PRIMARY KEY (`user_id`)
) ENGINE=INNODB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;