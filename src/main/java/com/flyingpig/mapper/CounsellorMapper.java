package com.flyingpig.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.entity.Counsellor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CounsellorMapper extends BaseMapper<Counsellor> {
    Counsellor getByUsernameAndPassword(Counsellor counsellor);
}
