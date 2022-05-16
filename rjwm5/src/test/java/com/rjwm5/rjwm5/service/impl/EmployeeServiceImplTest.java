package com.rjwm5.rjwm5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rjwm5.rjwm5.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeServiceImplTest {
    @Autowired
    private EmployeeServiceImpl employeeService;
@Test
    public void test01(){
//    LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
    QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
    QueryWrapper<Employee> id = employeeQueryWrapper.eq("id", 1);
    Employee one = employeeService.getOne(id);
    System.out.println(one);
//    System.out.println("test");
}
}