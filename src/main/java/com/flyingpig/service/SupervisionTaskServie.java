package com.flyingpig.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flyingpig.entity.SupervisionTask;
import com.flyingpig.pojo.PageBean;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

@Service
public interface SupervisionTaskServie {
    PageBean page(Integer pageNo, Integer pageSize, Integer userId);
}
