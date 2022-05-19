package com.rjwm5.rjwm5.common;


//编写工具类
//获取ThreadLocal,并在其中存储值
//基于ThreadLocal封装的工具类

//TODO
public class BeanContext {
    public static ThreadLocal threadLocal=new ThreadLocal();
    public static void setThreadLocal(Long id){
        threadLocal.set(id);
//        Object o = threadLocal.get();
    }

    public static Long getThreadLocal(){
        Long id =(Long) threadLocal.get();
        return id;
    }

}
