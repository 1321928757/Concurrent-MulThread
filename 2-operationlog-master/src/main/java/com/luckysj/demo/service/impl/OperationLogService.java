package com.luckysj.demo.service.impl;

import com.luckysj.demo.entity.OperationLogVo;
import com.luckysj.demo.mapper.LogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final LogMapper logMapper;

    public void saveOperationLog(OperationLogVo operationLog) {
        logMapper.insert(operationLog);
    }
}
