package com.rjwm5.rjwm5.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjwm5.rjwm5.common.R;
import com.rjwm5.rjwm5.entity.Category;
import com.rjwm5.rjwm5.entity.Dish;
import com.rjwm5.rjwm5.entity.Setmeal;
import com.rjwm5.rjwm5.exception.CustomException;
import com.rjwm5.rjwm5.service.CategoryService;
import com.rjwm5.rjwm5.service.DishService;
import com.rjwm5.rjwm5.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
@Autowired
    private CategoryService categoryService;
@Autowired
    private DishService dishService;
@Autowired
    private SetmealService setmealService;

@PostMapping
public R<String> save(HttpServletRequest request, @RequestBody Category category){
    Long employeeId =(Long) request.getSession().getAttribute("employeeId");
    category.setCreateUser(employeeId);
    category.setUpdateUser(employeeId);
//    LambdaUpdateWrapper<Category> categoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
    categoryService.save(category);
    return R.success("添加分类成功");
}

@GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize){
    Page<Category> categoryPage = new Page<>();
    categoryPage.setCurrent(page);
    categoryPage.setSize(pageSize);
//    LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
    Page<Category> page1 = categoryService.page(categoryPage);
//    调用page方法进行分页查询
    return R.success(page1);
}

@DeleteMapping
    public R<String> deleteById(Long id){
//    在删除前需要判断其它套餐
//    1.判断是否关联菜品
    LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
    LambdaQueryWrapper<Dish> query = dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
    Dish one = dishService.getOne(query);
//    或者调用count()方法，查询出记录数，进行判断是否可以进行删除
    if (one != null) {
        throw new CustomException("菜品发生关联");
    }

//    2.判断是否关联套餐
    LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
    LambdaQueryWrapper<Setmeal> query2 = setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
    Setmeal one1 = setmealService.getOne(query2);
    if (one1 != null) {
        throw new CustomException("套餐发生关联");
    }
//    3.删除
    boolean b = categoryService.removeById(id);
    return R.success("删除成功");
}

@PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Category category){
//   修改：name,sort
    Long employeeId =(Long) request.getSession().getAttribute("employeeId");
//   依据Id进行修改
    LambdaUpdateWrapper<Category> categoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
    LambdaUpdateWrapper<Category> query = categoryLambdaUpdateWrapper.eq(Category::getId, category.getId());
//    在update方法中加入category之后才能将修改的数据传入数据库中
    boolean update = categoryService.update(category,query);
    return R.success("修改分类成功");
}

}
