package com.rjwm5.rjwm5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjwm5.rjwm5.dto.DishDto;
import com.rjwm5.rjwm5.entity.Category;
import com.rjwm5.rjwm5.entity.Dish;
import com.rjwm5.rjwm5.entity.DishFlavor;
import com.rjwm5.rjwm5.mapper.DishFlavorMapper;
import com.rjwm5.rjwm5.mapper.DishMapper;
import com.rjwm5.rjwm5.service.CategoryService;
import com.rjwm5.rjwm5.service.DishFlavorService;
import com.rjwm5.rjwm5.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
/*    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;*/

//    注意：此处DishService已经交给spring容器管理
//    可以调用本类的对象的API，进行增删改查操作，不用注入
//    使用this进行调用，代表本类的对象
    @Transactional
    @Override
//    TODO
//    for循环中的内容需要使用函数式编程改进
    public void addDishandDishflavor(DishDto dishDto) {
//        先添加Dish数据,后添加dishflavor数据
//        dishMapper.insert()
//        Dish dish = new Dish();
//        由于dishDto继承自dish,所以可以直接将dto的数据直接传入表中
        this.save(dishDto);
//        将flavor的数据写入flavor表
//        将flavor表中的数据从dto中拆出来，再填入该表中

//        取出的为dish的id
        Long id = dishDto.getId();
//        通过id找到表中的数据
/*        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        找到dish的id关联的所有flavor数据
        LambdaQueryWrapper<DishFlavor> query = dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        List<DishFlavor> listDishFlavor = dishFlavorService.list(query);
        for (DishFlavor dishFlavor : listDishFlavor) {
            dishFlavorService.save(dishFlavor);
        }*/

        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        for (DishFlavor dishFlavor : dishFlavorList) {
            dishFlavor.setCreateUser(dishDto.getCreateUser());
            dishFlavor.setUpdateUser(dishDto.getUpdateUser());
            dishFlavor.setDishId(id);
//            log.info("---------------"+dishFlavor.toString());
//            System.out.println("---------------------------"+dishFlavor.toString());
            dishFlavorService.save(dishFlavor);
        }
//        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
    }


//    多表查询对菜品进行回显
    @Transactional
    @Override
    public DishDto showDishDto(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
     /*   dishDto.setName(dish.getName());
        dishDto.setPrice(dish.getPrice());*/
//        获取categoryName封装到dishDto中

//        注意：由于dishDto继承dish所以可以直接进行拷贝
        BeanUtils.copyProperties(dish,dishDto);
        Long categoryId = dish.getCategoryId();
//        通过categoryId进行获取categoryName
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Category> query = categoryLambdaQueryWrapper.eq(categoryId != null, Category::getId, categoryId);
        Category category = categoryService.getOne(query);
        dishDto.setCategoryName(category.getName());
//        回显口味表的信息
//        依据dishId进行查询
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DishFlavor> query2 = dishFlavorLambdaQueryWrapper.eq(id != null, DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(query2);
        dishDto.setFlavors(list);
        return dishDto;
    }



//    修改菜品以及菜品口味信息
    @Transactional
    @Override
    public void updateDishAndDishflavor(DishDto dishDto) {
//        注意：跟新内部使用update不是save方法进行重新添加数据
//        1.跟新dish表中的数据
//        2.跟新dishflavor表中的数据
//        3.将数据添加在表中

//        注意：Dish继承Dishdto，所以dish和dishDto可以直接复制或作为同一个实体类操作表
        log.info("----------------"+dishDto.getId().toString());
        LambdaQueryWrapper<Dish> dishDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Dish> query = dishDtoLambdaQueryWrapper.eq(Dish::getId, dishDto.getId());
//        跟新
//        this.updateById(dishDto);
//    只能添加dish类型的实体类，以及条件
//        dishDto类型的实体类无法进行添加
        this.update(dishDto,query);

//        清空dishFlavor表中的信息
//        依据flavor表中的Id进行查询
        Long dishid = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DishFlavor> query2 = dishFlavorLambdaQueryWrapper.eq(dishid != null, DishFlavor::getDishId, dishid);
        dishFlavorService.remove(query2);

//        或者直接使用此方法，进行更新
//        this.updateById(dishDto);

//        跟新dishFlavor表
        List<DishFlavor> flavors = dishDto.getFlavors();
/*        for (DishFlavor flavor : flavors) {
//            添加
//            注意：此处flavor中添加信息时没有添加dish_id属性的信息
            flavor.setDishId(dishid);
            flavor.setCreateUser(dishDto.getCreateUser());
            flavor.setUpdateUser(dishDto.getUpdateUser());
            dishFlavorService.save(flavor);
        }*/

//        方法二：进行书写
//        修改集合中的每个元素，采用stream流的方式
        flavors.stream().map((flavor)->{
            flavor.setUpdateUser(dishDto.getUpdateUser());
            flavor.setCreateUser(dishDto.getCreateUser());
            return flavor;
        }).collect(Collectors.toList());
    }
}
