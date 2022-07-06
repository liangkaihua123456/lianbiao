package com.rjwm5.rjwm5.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjwm5.rjwm5.dto.SetmealDto;
import com.rjwm5.rjwm5.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
//    添加套餐
    void addSetmeal(SetmealDto setmealDto);
//    删除套餐
    void deleteSetmeal(List<Long> ids);
}
