package com.flyingpig.service.serviceImpl;
import com.flyingpig.dataobject.dto.SupervisionTaskWithCourseNameAndBeginTimeAndEndTime;
import com.flyingpig.dataobject.entity.CourseDetail;
import com.flyingpig.mapper.CourseDetailMapper;
import com.flyingpig.mapper.SupervisionTaskMapper;
import com.flyingpig.pojo.PageBean;
import com.flyingpig.service.SupervisionTaskServie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
public class SupervisonTaskServiceImpl implements SupervisionTaskServie {
    @Autowired
    SupervisionTaskMapper supervisionTaskMapper;
    @Autowired
    CourseDetailMapper courseMapper;
    @Override
    public PageBean page(Integer pageNo, Integer pageSize, Integer userId) {
        //记录总记录数
        Long count= supervisionTaskMapper.count();
        //获取分页查询结果列表
        Integer start=(pageNo-1)*pageSize;//计算起始索引，公式：(页码-1）*页大小
        List<SupervisionTaskWithCourseNameAndBeginTimeAndEndTime> resultTaskList=supervisionTaskMapper.list(start,pageSize,userId);
        for(SupervisionTaskWithCourseNameAndBeginTimeAndEndTime item:resultTaskList){
            CourseDetail target=courseMapper.selectById(item.getCourseId());
            item.setCourseName(target.getCourseName());
            item.setBeginTime(target.getBeginTime());
            item.setEndTime(target.getEndTime());
            item.setSemester(target.getSemester());
            item.setWeekday(target.getWeekday());
        }
        //封装PageBean对象
        PageBean pageBean=new PageBean(count,resultTaskList);
        return pageBean;
    }
}
