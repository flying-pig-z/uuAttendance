package com.flyingpig.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.GetMapping;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
