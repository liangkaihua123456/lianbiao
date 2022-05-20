package com.rjwm5.rjwm5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//开启事务
@EnableTransactionManagement
public class Rjwm5Application {
    public static void main(String[] args) {
        SpringApplication.run(Rjwm5Application.class, args);
    }
}
