package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.entity.Supervison;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SupervisionMapper extends BaseMapper<Supervison> {
    public Integer selectSupervisionIdByStudentId(Integer studentId);
}
