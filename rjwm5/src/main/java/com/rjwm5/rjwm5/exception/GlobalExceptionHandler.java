package com.rjwm5.rjwm5.exception;

import com.rjwm5.rjwm5.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

//TODO
//该类没有起作用
@ControllerAdvice(annotations = {RestController.class,Controller.class})
@Slf4j
// 将处理的异常返回到前端页面上
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> handler(SQLIntegrityConstraintViolationException ex){
//        调用异常类的.getMessage方法获取其异常信息
//        log.info(ex.getMessage());
//        System.out.println(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")) {
//            使用split方法对字符串进行分割
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            log.info(msg);
            R.error(msg);
        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> handlerCustom(CustomException ex){
        return R.error(ex.getMessage());
    }
}
