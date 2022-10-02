package com.guigu.ggkt.exctption;

import com.guigu.ggkt.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行了全局异常处理");
    }

    //自定义异常处理
    @ExceptionHandler(GgktException.class)
    @ResponseBody
    public Result error(GgktException e){
        e.printStackTrace();
        return Result.fail(null).message("执行GgktException异常处理");
    }

}