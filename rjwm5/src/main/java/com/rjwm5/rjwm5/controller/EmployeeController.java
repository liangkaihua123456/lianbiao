package com.rjwm5.rjwm5.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjwm5.rjwm5.common.BeanContext;
import com.rjwm5.rjwm5.common.R;
import com.rjwm5.rjwm5.entity.Employee;
import com.rjwm5.rjwm5.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
//    注意：此处直接注入接口即可，不必注入实现类实现解耦
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

//        使用LambdaQueryWrapper的优点：条件查询使用（pojo的类名：：属性名）Employee::getUsername代替数据库表中的属性名，防止字符串拼写时发生错误
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
//        此处表示登录成功





//        登录成功，将信息存放在session中
//        通过HttpRequest中获取session
//         //登录成功，将员工id存入Session并返回登录成功结果
        HttpSession session = request.getSession();
        session.setAttribute("employeeId",one.getId());

//        将id值放入ThreadLocal中
//        TODO
        BeanContext.setThreadLocal(one.getId());


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
//        最后将查询到的数据库的用户信息返回到前端，在前端的application,中的storage中可以找到对应信息
        return R.success(one);
    }
//    退出登录，清理当前session中保存的员工ID
//    注意：清理session中的Id对于退出功能不影响，不清理ID也也可以实现退出功能
    @PostMapping("/logout")
//    泛型的方法表示该方法中可以添加的数据类型
    public R<String> logOut(HttpServletRequest request){
        request.getSession().removeAttribute("employeeId");
        return R.success("退出成功");
    }
//    添加员工信息
@PostMapping()
    public R<String> add(HttpServletRequest request,@RequestBody Employee employee){
//    System.out.println(employee);
//    LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
    String password = DigestUtils.md5DigestAsHex("123456".getBytes());
//    获取创建时间
    LocalDateTime createTime = LocalDateTime.now();
//    注意：第一次添加时创建时间和跟新时间相同
//    获取创建者
    Long employeeId =(Long) request.getSession().getAttribute("employeeId");
    System.out.println(employeeId);
//   第一次创建时，创建者与跟新者为同一个人
    employee.setPassword(password);

//   添加员工时： 观察是否会自动填充
/*    employee.setCreateTime(createTime);
    employee.setUpdateTime(createTime);*/
    employee.setCreateUser(employeeId);
    employee.setUpdateUser(employeeId);
    employee.setStatus(1);
//    添加
    employeeService.save(employee);
    return R.success("添加成功");
}

// 分页查询
@GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name){
//        log.info("{}{}{}",page,pageSize,name);
    LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
//    参数一：传入的值的条件
//    此语句表示：只有在name不为空的条件下，才进行name条件的添加，并进行查询
    LambdaQueryWrapper<Employee> query = employeeLambdaQueryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
//    按照跟新时间进行降序排序
    LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper1 = query.orderByDesc(Employee::getUpdateTime);
//    查询
    Page page1 = new Page();
    page1.setCurrent(page);
    page1.setSize(pageSize);
    Page page2 = employeeService.page(page1, employeeLambdaQueryWrapper1);
    return R.success(page2);
}

//注意：编辑后保存是对employee中的数据进行操作，若改为employee1中的数据则无法更新
@PutMapping()
    public R<String> bind(HttpServletRequest request,@RequestBody Employee employee){
/*//        log.info(employee.toString());

//    先通过ID查询出employee的所有信息
   *//* LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
    LambdaQueryWrapper<Employee> query2 = employeeLambdaQueryWrapper.eq(Employee::getId, employee.getId());*//*
//    Employee one = employeeService.getOne(query2);
//    System.out.println("-----------"+employee.getId()+"---------------");
*//*    Employee one = employeeService.getById(employee.getId());
    System.out.println("---------------"+one.toString()+"-------------");*//*
    Integer status = employee.getStatus();
//    0
    LambdaUpdateWrapper<Employee> employeeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//    status.equals(0)才执行此sql语句
    LambdaUpdateWrapper<Employee> query = employeeLambdaUpdateWrapper.
            eq(Employee::getId,employee.getId());
//    注意：employee存在很多空数据

    Employee employee1 = new Employee();
//    one.setId(null);
//    BeanUtils.copyProperties(one,employee1);
*//*    employee1.setPassword(one.getPassword());
    employee1.setCreateUser(one.getCreateUser());
    employee1.setCreateTime(one.getCreateTime());*//*
    employee1.setStatus(status);
    employee1.setUpdateTime(LocalDateTime.now());
//    通过session获取创建者
    HttpSession session = request.getSession();
    Long employeeId =(Long) session.getAttribute("employeeId");
    employee1.setUpdateUser(employeeId);
//    注意：此处跟新，只需要传入修改的数据即可，无需传入所有的数据
    employeeService.update(employee1,query);
//    System.out.println(employee1.toString()+"-----------------");
    return R.success("修改账号成功");*/
 /*   employee.setUpdateTime(LocalDateTime.now());
    HttpSession session = request.getSession();
    Long employeeId =(Long) session.getAttribute("employeeId");
    employee.setUpdateUser(employeeId);
    boolean b = employeeService.updateById(employee);
    System.out.println("-----------------------"+employee);
    return R.success("修改成功");*/

    LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
    LambdaQueryWrapper<Employee> query = employeeLambdaQueryWrapper.eq(Employee::getId, employee.getId());
    employee.setUpdateTime(LocalDateTime.now());
    Long employeeId =(Long) request.getSession().getAttribute("employeeId");
    employee.setUpdateUser(employeeId);
    employeeService.update(employee,query);
    return R.success("修改成功");
}
//@PathVariable接收路径上的参数
@GetMapping("/{id}")
    public R<Employee> updateEmployee(@PathVariable(name = "id") Long id ){
//        return R.success("编辑员工成功");
    LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
    LambdaQueryWrapper<Employee> query = employeeLambdaQueryWrapper.eq(Employee::getId, id);
    Employee one = employeeService.getOne(query);
    return R.success(one);
}


}
