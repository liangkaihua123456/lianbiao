package com.rjwm5.rjwm5;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//开启事务
//@MapperScan("com.rjwm5.rjwm5.filter.LoginCheckFilter.java")
@EnableTransactionManagement
public class Rjwm5Application {
    public static void main(String[] args) {
        SpringApplication.run(Rjwm5Application.class, args);
    }
}
