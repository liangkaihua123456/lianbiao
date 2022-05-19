package com.rjwm5.rjwm5.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjwm5.rjwm5.entity.Setmeal;
import com.rjwm5.rjwm5.mapper.SetmealMapper;
import com.rjwm5.rjwm5.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
