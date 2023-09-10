package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.mapper.CounsellorMapper;
import com.flyingpig.dataobject.entity.Counsellor;
import com.flyingpig.service.CounsellorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class CounsellorServiceImpl implements CounsellorService {
    @Autowired
    private CounsellorMapper counsellorMapper;
    @Override
    public Counsellor getCounsellorByNo(String username) {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("no",username);
        Counsellor counsellor=counsellorMapper.selectOne(queryWrapper);
        return counsellor;
    }
}