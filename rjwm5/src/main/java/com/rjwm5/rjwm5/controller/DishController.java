package com.rjwm5.rjwm5.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjwm5.rjwm5.common.R;
import com.rjwm5.rjwm5.dto.DishDto;
import com.rjwm5.rjwm5.entity.Category;
import com.rjwm5.rjwm5.entity.Dish;
import com.rjwm5.rjwm5.service.CategoryService;
import com.rjwm5.rjwm5.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> addDish(HttpServletRequest request, @RequestBody DishDto dishDto){
        log.info(dishDto.toString());
//        System.out.println("-------------"+dishDto.toString());
        Long employeeId =(Long) request.getSession().getAttribute("employeeId");
        dishDto.setCreateUser(employeeId);
        dishDto.setUpdateUser(employeeId);
        dishService.addDishandDishflavor(dishDto);
//        log.info(dishDto.toString());
           return R.success("添加菜品成功");
    }


//    name进行搜索
//    默认：不加搜索条件搜索全部
    @GetMapping("/page")
    public R<Page<DishDto>> page(Integer page,Integer pageSize,String name){
/*        Page<DishDto> dishPage = new Page<>();
        dishPage.setSize(pageSize);
        dishPage.setCurrent(page);
        LambdaUpdateWrapper<DishDto> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        LambdaUpdateWrapper<DishDto> dishLambdaUpdateWrapper1 = dishLambdaUpdateWrapper.orderByDesc(DishDto::getUpdateTime);*/

//        分页缺少菜品分类如何写

//        注意：在页面中还有查询条件需要进行填写

//        解决方法：进行重新填写，写2个page后进行复制
//        一个DishDTO,一个Dish
        Page<Dish> dishPage = new Page<>();
        dishPage.setCurrent(page);
        dishPage.setSize(pageSize);
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Dish> query = dishLambdaQueryWrapper.like(name != null, Dish::getName, name).orderByDesc(Dish::getUpdateTime);
//        E page1 = dishService.page(query);
        Page<Dish> page1 = dishService.page(dishPage, query);
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(page1,dishDtoPage,"records");

//        还是缺少categoryName
//        需要使用dish表的categoryId进行关联
//        注意：要获得分页查找的数据，需要使用recordsApi进行查找
        List<Dish> records = page1.getRecords();
//        为每个记录添加categoryName
/*        for (Dish record : records) {
            Long categoryId = record.getCategoryId();
//            查找category表
            Category category = categoryService.getById(categoryId);
            String name1 = category.getName();

        }*/

//        List<DishDto> records1 = dishDtoPage.getRecords();
//      注意：records集合为一个空集合

//        修改流中的元素dish数据类型为dishDto数据类型
//        dish->{return dishDto;表示一个返回值为DishDto类型的函数}
        List<DishDto> collect = records.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
//            获取categoryName
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
//        注意：最后将使用map方法修改的数据类型的元素收集为一个List集合
//        注意：在使用map方法时不忘记return 给函数返回修改的值



//        java.lang.ClassCastException: java.util.Collections$EmptyList cannot be cast to java.util.ArrayList
//        无法解决
//        只能使用流
//        List<List<DishDto>> lists = Arrays.asList(records1);

//        此处一下把整张表的记录都复制到另一个dto中会报错
//        所以忽略记录数，再进行复制
 /*       for (DishDto dishDto : records1) {
            Long categoryId = dishDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String name1 = category.getName();
            dishDto.setCategoryName(name1);
//            BeanUtils.copyProperties();
        }*/

/*        for (int i = 0; i < records.size(); i++) {
            Dish dish = records.get(i);
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String name1 = category.getName();
//            DishDto dishDto = records1.get(i);
            DishDto dishDto = new DishDto();
            log.info(dish.toString());
            BeanUtils.copyProperties(dish,dishDto);
            dishDto.setCategoryName(name1);
            log.info("-----------------"+dishDto.toString());
            records1.add(dishDto);
            log.info("-----------------"+records1.toString());
//            dishDto.setCategoryName(name1);
        }*/
//            在集合中重新添加records


//        java.lang.UnsupportedOperationException: null
//        TODO

//        使用流解决
        dishDtoPage.setRecords(collect);
        System.out.println("-----------------"+records);
        System.out.println("-----------------"+collect.toString());
        return R.success(dishDtoPage);
//        dishService.page()
//        return R.success(page1);
    }

//    对菜品进行回显
//    涉及到多表查询
//    注意：路径参数的写法（“/{id}”）在Mapping注解上
    @GetMapping("/{id}")
    public R<DishDto> show(@PathVariable("id") Long id){
        DishDto dishDto = dishService.showDishDto(id);
        return R.success(dishDto);
    }


//    修改菜品信息
    @PutMapping
    public R<String> updateDish(HttpServletRequest request,@RequestBody DishDto dishDto){
//        将菜品信息以及菜品口味信息添加并保存
//        出现错误：
//        2022-05-20 14:52:44.940  INFO 56964 --- [nio-8082-exec-7] c.r.r.exception.GlobalExceptionHandler   : '1527311627028467714'已存在
//2022-05-20 14:52:44.942 ERROR 56964 --- [nio-8082-exec-7] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Could not resolve view with name 'dish' in servlet with name 'dispatcherServlet'] with root cause
        Long employeeId =(Long) request.getSession().getAttribute("employeeId");
        dishDto.setUpdateUser(employeeId);
//        由于其dishId已经存在所以不能进行重复添加，会报错
//        解决的方法：在修改时，需要先清空后再重新添加修改的数据
//        dishService.addDishandDishflavor(dishDto);
        dishService.updateDishAndDishflavor(dishDto);
        return R.success("修改菜品成功");
    }
}
