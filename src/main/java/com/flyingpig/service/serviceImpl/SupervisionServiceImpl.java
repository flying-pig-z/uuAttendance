package com.flyingpig.service.serviceImpl;

import com.flyingpig.mapper.StudentMapper;
import com.flyingpig.mapper.SupervisionMapper;
import com.flyingpig.service.SupervisionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class SupervisionServiceImpl implements SupervisionService {
    @Autowired
    private SupervisionMapper supervisionMapper;

}
