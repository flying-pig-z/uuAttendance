package com.flyingpig.service;

import com.flyingpig.dataobject.vo.SupervisionTaskAddVO;
import com.flyingpig.pojo.PageBean;
import org.springframework.stereotype.Service;

@Service
public interface SupervisionTaskServie {
    PageBean page(Integer pageNo, Integer pageSize, Integer userId);

    void addSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(String teaUserid, SupervisionTaskAddVO supervisionTaskAddVO);

    void deleteSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(String teaUserid, SupervisionTaskAddVO supervisionTaskAddVO);
}
