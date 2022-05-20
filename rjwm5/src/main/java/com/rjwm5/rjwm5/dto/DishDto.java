package com.rjwm5.rjwm5.dto;

import com.rjwm5.rjwm5.entity.Dish;
import com.rjwm5.rjwm5.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 新添加了参数菜品分类名称
//菜品味道集合
//添加图片
@Data
public class DishDto extends Dish {
//    直接进行初始化，集合若没有进行初始化对象，在获取到属性时则无法为其赋值

//    注意;此处的参数名一定要与前端封装的参数名一致
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
