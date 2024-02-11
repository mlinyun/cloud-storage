package com.mlinyun.cloudstorage.exception;

import com.mlinyun.cloudstorage.common.RestResult;
import com.mlinyun.cloudstorage.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice // 这是一个增强的 Controller。使用这个 Controller ，可以实现三个方面的功能：1、全局异常处理， 2、全局数据绑定， 3、全局数据预处理
public class GlobalExceptionHandler {

    /**
     * 通用异常处理方法
     *
     * @param exception 异常
     * @param <T>       泛型
     * @return 通用返回失败
     */
    @ResponseBody   // 该注解为 Spring Boot 响应体注解，用在这里的目的就是当出现异常时，直接将错误返回给前台，可以避免前台页面阻塞
    @ExceptionHandler(Exception.class)  // 用于指明异常处理类型
    public <T> RestResult<T> error(Exception exception) {
        log.error("全局异常捕获(通用异常): " + exception.getMessage());
        return RestResult.fail();
    }

    /**
     * 空指针异常处理方法
     *
     * @param nullPointerException 空指针异常
     * @param <T>                  泛型
     * @return 通用返回设置空指针异常
     */
    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    public <T> RestResult<T> error(NullPointerException nullPointerException) {
        log.error("全局异常捕获(空指针异常): " + nullPointerException.getMessage());
        return RestResult.setResult(ResultCodeEnum.NULL_POINT);
    }

    /**
     * 下标越界异常处理
     *
     * @param indexOutOfBoundsException 下标越界异常
     * @param <T>                       泛型
     * @return 通用返回设置下标越界异常
     */
    @ResponseBody
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public <T> RestResult<T> error(IndexOutOfBoundsException indexOutOfBoundsException) {
        log.error("全局异常捕获(下标越界异常): " + indexOutOfBoundsException.getMessage());
        return RestResult.setResult(ResultCodeEnum.INDEX_OUT_OF_BOUNDS);
    }
}
