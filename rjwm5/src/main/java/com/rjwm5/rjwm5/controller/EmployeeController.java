package com.rjwm5.rjwm5.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rjwm5.rjwm5.common.R;
import com.rjwm5.rjwm5.entity.Employee;
import com.rjwm5.rjwm5.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
//    注意：此处直接注入接口即可，
//    不必注入实
//    现类实
//
//    现解耦
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
//        R<Employee> r = new R<>();
//        进行md5加密
        String password = employee.getPassword();
        String password2 = DigestUtils.md5DigestAsHex(password.getBytes());
//        查询该用户是否存在
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        通过用户名进行查询
        LambdaQueryWrapper<Employee> employeequery = employeeLambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
//        查询
        Employee one = employeeService.getOne(employeequery);
        if (one == null) {
//            r.setMsg("该用户不存在");
//            return r;
//            注意：不用直接new R对象返回，直接调用其静态方法可以直接进行返回
            R.error("该用户不存在");
        }
//        查询密码是否正确
//        即查询数据库中查询到的密码与前端传来的密码是否匹配
        if (!one.getPassword().equals(employee.getPassword())) {
//            r.setMsg("用户密码不正确");
//            return r;
            R.error("用户密码不正确");
        }
//       判断账号是否被禁用
        if (one.getStatus()==0) {
//            r.setMsg("该用户账号被禁用");
//            return r;
            R.error("该用户账号被禁用");
        }
//        登录成功，将信息存放在session中
//        通过HttpRequest中获取session
//         //登录成功，将员工id存入Session并返回登录成功结果
        HttpSession session = request.getSession();
        session.setAttribute("employeeId",one.getId());
//        返回前端封装好的数据
//        System.out.println("--------------"+session.toString()+"----------------");
/*        Enumeration<String> attributeNames = session.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            // 获取session键值
            String name = attributeNames.nextElement().toString();
            // 根据键值取session中的值
            Object value = session.getAttribute(name);
            // 打印结果
//            log.info("this is :" + session.toString() + "--> ");
            System.out.println("this is :" + session.toString() + "--> ");
//            log.info(name + " : " + value);
            System.out.println(name + " : " + value);
        }*/
//        注意：此处不能为空值，否则会报空指针异常
//        从session中获取数据
    /*    String employeeId =  session.getAttribute("employeeId").toString();
        System.out.println(employeeId);*/
        return R.success(one);
    }
}
