package com.luckysj.demo.exception.biz;

import com.luckysj.demo.model.response.ResponseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private ResponseCodeEnum errorCode;

    /**
     * 自定义错误信息
     */
    private String errorMsg;

}
