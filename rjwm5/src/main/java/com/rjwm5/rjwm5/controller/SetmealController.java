package com.rjwm5.rjwm5.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjwm5.rjwm5.common.R;
import com.rjwm5.rjwm5.dto.SetmealDto;
import com.rjwm5.rjwm5.entity.Category;
import com.rjwm5.rjwm5.entity.Setmeal;
import com.rjwm5.rjwm5.service.CategoryService;
import com.rjwm5.rjwm5.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;
//    添加套餐
    @PostMapping
    public R<String> add(HttpServletRequest request, @RequestBody SetmealDto setmealDto){

        Long employeeId =(Long) request.getSession().getAttribute("employeeId");
        setmealDto.setCreateUser(employeeId);
        setmealDto.setUpdateUser(employeeId);
  /*      setmealDto.setCreateTime(LocalDateTime.now());
        setmealDto.setUpdateTime(LocalDateTime.now());*/
//        log.info("-----------------------------"+setmealDto.toString());
        setmealService.addSetmeal(setmealDto);
        return R.success("添加套餐成功");
    }
    @GetMapping("/page")
//    注意：由于是分页查询，所以其泛型必须为Page
    public R<Page> page(Integer page,Integer pageSize,String name){
//        注意：与数据库操作的实体类只能是setmeal不能是setmealDto
//        setmealDto的作用为返回前端数据
        Page<Setmeal> setmealDtoPage = new Page<>();
        setmealDtoPage.setCurrent(page);
        setmealDtoPage.setSize(pageSize);
        LambdaQueryWrapper<Setmeal> setmealDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Setmeal> records = setmealDtoPage.getRecords();
        LambdaQueryWrapper<Setmeal> query = setmealDtoLambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        Page<Setmeal> page1 = setmealService.page(setmealDtoPage, query);
//        注意：缺少套餐分类的名称
        List<Setmeal> records1 = page1.getRecords();
        Page<SetmealDto> setmealDtoPage1 = new Page<>();
//        将page1中的数据装入setmealDtoPage1中
        BeanUtils.copyProperties(page1,setmealDtoPage1,"records");
//        再将套餐的分类名称填入

//        注意：records2为空值，在集合中没有对象
     /*   List<SetmealDto> records2 = setmealDtoPage1.getRecords();
        records2.stream().map(item->{
            SetmealDto setmealDto = new SetmealDto();

        })*/
        List<SetmealDto> collect = records1.stream().map(item -> {
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage1.setRecords(collect);
        return R.success(setmealDtoPage1);
    }


//    删除套餐
//    注意：多表操作，需要自己重新写接口中的方法
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
//        log.info("----------------"+ids.toString());
      setmealService.deleteSetmeal(ids);
      return R.success("删除套餐成功");
    }
}
