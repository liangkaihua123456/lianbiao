package com.rjwm5.rjwm5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjwm5.rjwm5.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
