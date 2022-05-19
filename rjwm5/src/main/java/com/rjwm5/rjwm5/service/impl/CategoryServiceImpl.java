package com.rjwm5.rjwm5.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjwm5.rjwm5.entity.Category;
import com.rjwm5.rjwm5.mapper.CategoryMapper;
import com.rjwm5.rjwm5.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
