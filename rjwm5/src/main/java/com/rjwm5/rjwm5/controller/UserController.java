package com.rjwm5.rjwm5.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rjwm5.rjwm5.common.R;
import com.rjwm5.rjwm5.entity.User;
import com.rjwm5.rjwm5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
//    移动端的登录
/*    @PostMapping("/login")
    public R<User> login(@RequestBody String phone){

    }*/
}
