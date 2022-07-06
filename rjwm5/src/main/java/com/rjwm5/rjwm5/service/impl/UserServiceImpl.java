package com.rjwm5.rjwm5.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjwm5.rjwm5.entity.User;
import com.rjwm5.rjwm5.mapper.UserMapper;
import com.rjwm5.rjwm5.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
