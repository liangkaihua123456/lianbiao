package com.rjwm5.rjwm5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjwm5.rjwm5.dto.SetmealDto;
import com.rjwm5.rjwm5.entity.Setmeal;
import com.rjwm5.rjwm5.entity.SetmealDish;
import com.rjwm5.rjwm5.exception.CustomException;
import com.rjwm5.rjwm5.mapper.SetmealMapper;
import com.rjwm5.rjwm5.service.SetmealDishService;
import com.rjwm5.rjwm5.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional
    public void addSetmeal(SetmealDto setmealDto) {
//        setmealDto中包含前端传来的数据，现在需要将前端传来的数据，分别放入到2张表中
      /*  Long id = setmealDto.getId();
        System.out.println("----------------------------------"+id);*/
//        注意：此处发生错误，在数据库中无法找到数据根据该ID，因为该ID不存在
//        Setmeal setmeal = this.getById(id);
        this.save(setmealDto);
      /*  Long id = setmealDto.getId();
        System.out.println("-----------------------------------"+id);*/
//        TODO
//        注意：在套餐没有添加在数据库之前是不会出现ID的生成，只有将ID添加在数据库之后，才能获取到id
//        获取setmeal_dish表中的数据，进行添加
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
//        将其插入到表中

//        原来的思路：使用for循环，从setmealDto中取出集合
//        但是注意：从集合中取出的数据没有包含setmeal_id，需要手动加入
//        对于一个集合的数据可以使用saveBaches方法进行批量添加
         setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDish.setCreateUser(setmealDto.getCreateUser());
            setmealDish.setUpdateUser(setmealDto.getUpdateUser());
//          注意：必须要有返回值
            return setmealDish;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void deleteSetmeal(List<Long> ids) {
//        若状态为1，则不能进行删除，表示正常售卖
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> query = setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
//        采用in作为查询条件
        LambdaQueryWrapper<Setmeal> query2 = query.eq(Setmeal::getStatus, 1);
//        注意：批量删除有一个删除不成功，则所有的都删除不成功
        long count = this.count(query2);
        if (count>0) {
            throw new CustomException("该套餐在正常售卖不能进行删除");
        }
//        若可以删除还需要进行关联删除
//        删除setmeal
        this.removeByIds(ids);
//        删除关联的dish
//        注意：采用in设置条件进行批量删除
//        省去for循环
//        TODO
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SetmealDish> query3 = setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(query3);
    }
}
