package com.rjwm5.rjwm5.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjwm5.rjwm5.entity.Employee;
import com.rjwm5.rjwm5.mapper.EmployeeMapper;
import com.rjwm5.rjwm5.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
