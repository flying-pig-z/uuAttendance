package com.flyingpig.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.dataobject.dto.StudentInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
