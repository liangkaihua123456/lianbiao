package com.rjwm5.rjwm5.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjwm5.rjwm5.dto.DishDto;
import com.rjwm5.rjwm5.entity.Dish;

public interface DishService extends IService<Dish> {
//    在该接口中添加方法
    void addDishandDishflavor(DishDto dishDto);
//    多表查询，回显菜品数据,根据Id进行查询
    DishDto showDishDto(Long id);
//    修改菜品信息
    void updateDishAndDishflavor(DishDto dishDto);
}
