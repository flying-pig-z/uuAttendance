package com.flyingpig.service;

import com.flyingpig.pojo.PageBean;
import org.springframework.stereotype.Service;

@Service
public interface SupervisionTaskServie {
    PageBean page(Integer pageNo, Integer pageSize, Integer userId);
}
