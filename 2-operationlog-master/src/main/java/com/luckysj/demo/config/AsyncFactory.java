package com.luckysj.demo.config;


import com.luckysj.demo.service.impl.OperationLogService;
import com.luckysj.demo.spring.SpringUtils;
import com.luckysj.demo.entity.OperationLogVo;

import java.util.TimerTask;

public class AsyncFactory {

    /**
     * 记录操作日志
     * @param operationLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOperation(OperationLogVo operationLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 找到日志服务bean，进行日志持久化操作
                SpringUtils.getBean(OperationLogService.class).saveOperationLog(operationLog);
            }
        };
    }


}
