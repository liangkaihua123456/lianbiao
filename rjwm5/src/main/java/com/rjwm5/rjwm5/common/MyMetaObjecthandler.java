package com.rjwm5.rjwm5.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//自定义元数据对象处理器
@Component
@Slf4j
//实现MetaObjectHandler元数据处理器接口
public class MyMetaObjecthandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段的自动填充insert");
//        为插入时自动填充的字段赋值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
      /*  metaObject.setValue("createUser",BeanContext.getThreadLocal());
        metaObject.setValue("updateUser",BeanContext.getThreadLocal());*/
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段的自动填充update");
        metaObject.setValue("updateTime",LocalDateTime.now());
//        metaObject.setValue("updateUser",BeanContext.getThreadLocal());
    }
}
