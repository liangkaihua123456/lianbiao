package com.rjwm5.rjwm5.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjwm5.rjwm5.entity.DishFlavor;
import com.rjwm5.rjwm5.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
