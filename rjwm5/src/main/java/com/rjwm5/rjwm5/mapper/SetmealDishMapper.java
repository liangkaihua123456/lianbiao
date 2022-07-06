package com.rjwm5.rjwm5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjwm5.rjwm5.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
}
