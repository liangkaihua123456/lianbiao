package com.rjwm5.rjwm5.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjwm5.rjwm5.entity.Dish;
import com.rjwm5.rjwm5.mapper.DishMapper;
import com.rjwm5.rjwm5.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
