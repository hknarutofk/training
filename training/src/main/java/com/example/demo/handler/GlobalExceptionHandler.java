package com.example.demo.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.api.R;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yeqiang
 * @since 5/22/20 4:44 PM
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 注意： 1. @ExceptionHander用于指定异常类型， Exception.class指示所有异常 2. @ResponseBody 必须添加，否则异常处理器完成处理后，仍然会走向springboot /error
     * 错误处理流程（内置或自定义ErrorController），导致此处返回的R被丢弃 3. @ExceptionHandler会导致原本的http状态码丢失，如，原本应该返回500的，在此注解配合下，返回http 200
     * 
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        /**
         * 此处已经变成200了， 丢失了原有状态码
         */
        log.warn("status:{}", response.getStatus());
        log.error("wtf " + e.getMessage(), e);
        return R.failed("wtf-" + e.getMessage());
    }
}
