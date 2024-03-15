package com.luckysj.demo.aop;

import com.alibaba.fastjson.JSON;
import com.luckysj.demo.annotation.OperationLog;
import com.luckysj.demo.config.AsyncFactory;
import com.luckysj.demo.config.AsyncManager;
import com.luckysj.demo.entity.OperationLogVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 多线程记录操作日志
 * @author 牵着猫散步的鼠鼠-LiuShiJie
 */
@Aspect
@Component
@Slf4j
public class OperationLogAspect {


    /**
     * 设置操作日志切入点，记录操作日志，在注解(@OperationLogger)的位置切入代码
     */
    @Pointcut("@annotation(com.luckysj.demo.annotation.OperationLog)")
    public void optLogPointCut() {
    }

    @Around("optLogPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName();
        OperationLogVo operationLogVo = null;
        try {
            // 获取日志实体
            operationLogVo = this.recordLog(joinPoint);
        } catch (IllegalStateException e) {
            log.error("no web request:{}", e.getMessage());
        }
        // 记录日志耗时
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            // 获取方法返回数据
            result = joinPoint.proceed();
            // 保存返回数据到日志实体
            operationLogVo.setData(JSON.toJSONString(result));
        } catch (Throwable e) {
            log.info("method: {}, throws: {}", methodName, ExceptionUtils.getStackTrace(e));
            if (operationLogVo != null) {
                operationLogVo.setErrorMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()));
            }
        } finally {
            long endTime = System.currentTimeMillis();
            if (operationLogVo != null) {
                operationLogVo.setTimes(endTime - startTime);
                //异步记录操作日志
                AsyncManager.getInstance().execute(AsyncFactory.recordOperation(operationLogVo));
            }
        }
        return result;
    }

    private OperationLogVo recordLog(ProceedingJoinPoint joinPoint) {
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        OperationLog optLogger = method.getAnnotation(OperationLog.class);
        // 获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        // 日志保存到数据库
        OperationLogVo operationLogVo = new OperationLogVo();
        // 操作类型
        operationLogVo.setType(optLogger.value());
        // 请求URI
        operationLogVo.setUri(request.getRequestURI());
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // 请求方法
        operationLogVo.setName(methodName);

        // 请求参数
        if (joinPoint.getArgs()[0] instanceof MultipartFile) {
            operationLogVo.setParams(((MultipartFile) joinPoint.getArgs()[0]).getOriginalFilename());
        } else {
            operationLogVo.setParams(JSON.toJSONString(joinPoint.getArgs()));
        }
        // 请求方式
        operationLogVo.setMethod(Objects.requireNonNull(request).getMethod());
        // 请求用户ID 先写死
        operationLogVo.setUserId(22);
//        operationLogVo.setUserId(SecurityUtils.getUserId());
        // 请求用户昵称 先写死
        operationLogVo.setNickname("woniu");
        // 操作ip地址
        String ip = request.getRemoteAddr();
        operationLogVo.setIpAddress(ip);
        return operationLogVo;
    }

    // 将异常相关的全部信息(类名、描述、堆栈跟踪)格式化为一个字符串,方便存储到日志记录对象OperationLogVo的errorMessage属性中。
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement stet : elements) {
            stringBuilder.append(stet).append("\n");
        }
        return exceptionName + ":" + exceptionMessage + "\n" + stringBuilder;
    }


}


