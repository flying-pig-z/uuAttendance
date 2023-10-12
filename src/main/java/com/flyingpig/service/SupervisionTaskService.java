package com.flyingpig.service;

import com.flyingpig.dataobject.dto.CourseStudent;
import com.flyingpig.dataobject.vo.SupervisionTaskAddVO;
import com.flyingpig.common.PageBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SupervisionTaskService {
    PageBean pageSupervisonTaskBySupervisonId(Integer pageNo, Integer pageSize, Integer userId);

    void addSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(String teaUserid, SupervisionTaskAddVO supervisionTaskAddVO);

    void deleteSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(String teaUserid, SupervisionTaskAddVO supervisionTaskAddVO);

}
