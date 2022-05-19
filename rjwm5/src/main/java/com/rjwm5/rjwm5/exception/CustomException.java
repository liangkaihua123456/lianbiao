package com.rjwm5.rjwm5.exception;


//自定义异常类
//出现异常时将会抛出
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
