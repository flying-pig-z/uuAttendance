package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.dto.CourseStudent;
import com.flyingpig.dataobject.dto.SupervisionTaskWithCourseNameAndBeginTimeAndEndTime;
import com.flyingpig.dataobject.entity.CourseDetail;
import com.flyingpig.dataobject.entity.SupervisionTask;
import com.flyingpig.dataobject.entity.User;
import com.flyingpig.dataobject.entity.UserRoleRelation;
import com.flyingpig.dataobject.vo.SupervisionTaskAddVO;
import com.flyingpig.mapper.CourseDetailMapper;
import com.flyingpig.mapper.SupervisionTaskMapper;
import com.flyingpig.mapper.UserMapper;
import com.flyingpig.common.PageBean;
import com.flyingpig.mapper.UserRoleRelationMapper;
import com.flyingpig.service.SupervisionTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
public class SupervisonTaskServiceImpl implements SupervisionTaskService {
    @Autowired
    SupervisionTaskMapper supervisionTaskMapper;
    @Autowired
    CourseDetailMapper courseDetailMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleRelationMapper userRoleRelationMapper;

    @Override
    public PageBean pageSupervisonTaskBySupervisonId(Integer pageNo, Integer pageSize, Integer userId) {
        //记录总记录数
        Long count= supervisionTaskMapper.selectCountBySupervisonIdAndTimeNow();
        //获取分页查询结果列表
        Integer start=(pageNo-1)*pageSize;//计算起始索引，公式：(页码-1）*页大小
        System.out.println(start);
        System.out.println(pageSize);
        List<SupervisionTaskWithCourseNameAndBeginTimeAndEndTime> resultTaskList=supervisionTaskMapper.list(start,pageSize,userId);
        for(SupervisionTaskWithCourseNameAndBeginTimeAndEndTime item:resultTaskList){
            CourseDetail target=courseDetailMapper.selectById(item.getCourseId());
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

    @Override
    public void addSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(String teaUserid, SupervisionTaskAddVO supervisionTaskAddVO) {
        //查询对应的课程列表
        QueryWrapper<CourseDetail> courseDetailQueryWrapper=new QueryWrapper<CourseDetail>();
        courseDetailQueryWrapper.eq("course_teacher",teaUserid);
        System.out.println(teaUserid);
        courseDetailQueryWrapper.eq("course_name",supervisionTaskAddVO.getCourseName());
        courseDetailQueryWrapper.eq("semester",supervisionTaskAddVO.getSemester());
        List<CourseDetail> courseDetailList=courseDetailMapper.selectList(courseDetailQueryWrapper);
        System.out.println(courseDetailList.size());
        for(CourseDetail courseDetail:courseDetailList){
            //插入数据
            SupervisionTask supervisionTask=new SupervisionTask();
            supervisionTask.setUserid(supervisionTaskAddVO.getUserId());
            supervisionTask.setCourseId(courseDetail.getId());
            supervisionTaskMapper.insert(supervisionTask);
            //更新user表用户的身份
            User user=new User();
            user.setId(supervisionTaskAddVO.getUserId());
            user.setUserType(2);
            userMapper.updateById(user);
            //修改关系表中用户督导身份和权限
            UserRoleRelation userRoleRelation=new UserRoleRelation(supervisionTaskAddVO.getUserId(),2);
            userRoleRelationMapper.updateById(userRoleRelation);
        }
    }

    @Override
    public void deleteSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(String teaUserid, SupervisionTaskAddVO supervisionTaskAddVO) {
        //查询对应的课程列表
        QueryWrapper<CourseDetail> courseDetailQueryWrapper=new QueryWrapper<CourseDetail>();

        courseDetailQueryWrapper.eq("course_teacher",teaUserid);
        System.out.println(teaUserid);
        courseDetailQueryWrapper.eq("course_name",supervisionTaskAddVO.getCourseName());
        courseDetailQueryWrapper.eq("semester",supervisionTaskAddVO.getSemester());
        List<CourseDetail> courseDetailList=courseDetailMapper.selectList(courseDetailQueryWrapper);
        for(CourseDetail courseDetail:courseDetailList){
            QueryWrapper<SupervisionTask> supervisionTaskQueryWrapper=new QueryWrapper<>();
            //删除督导任务
            supervisionTaskQueryWrapper.eq("userid",supervisionTaskAddVO.getUserId());
            supervisionTaskQueryWrapper.eq("course_id",courseDetail.getId());
            supervisionTaskMapper.delete(supervisionTaskQueryWrapper);
            //检测用户有没有督导身份,没有去除督导身份
            QueryWrapper<SupervisionTask> monitor=new QueryWrapper<>();
            monitor.eq("userid",supervisionTaskAddVO.getUserId());
            List<SupervisionTask> supervisionTaskList=supervisionTaskMapper.selectList(monitor);
            if(supervisionTaskList.size()==0){
                User user=new User();
                user.setId(supervisionTaskAddVO.getUserId());
                user.setUserType(1);
                userMapper.updateById(user);
                //修改关系表中用户督导身份和权限
                UserRoleRelation userRoleRelation=new UserRoleRelation(supervisionTaskAddVO.getUserId(),1);
                QueryWrapper<UserRoleRelation> userRoleRelationQueryWrapper=new QueryWrapper<>();
                userRoleRelationQueryWrapper.eq("user_id",supervisionTaskAddVO.getUserId());
                userRoleRelationMapper.update(userRoleRelation,userRoleRelationQueryWrapper);
            }
        }
    }
    @Override
    public List<CourseStudent> listSupervisonByteaUserIdAndCourseNameAndsemester(String teaUserid, String semester, String courseName) {
        //查询对应的其中一节课程
        QueryWrapper<CourseDetail> courseDetailQueryWrapper=new QueryWrapper<CourseDetail>();
        courseDetailQueryWrapper.eq("course_teacher",teaUserid)
                .eq("course_name",courseName)
                .eq("semester",semester)
                .select("id");
        Integer courseId=courseDetailMapper.selectList(courseDetailQueryWrapper).get(0).getId();
        //通过课程列表查询督导列表
        QueryWrapper<SupervisionTask> supervisionTaskQueryWrapper=new QueryWrapper<>();
        supervisionTaskQueryWrapper.eq("course_id",courseId).select("userid");
        List<SupervisionTask> supervisionTaskList=supervisionTaskMapper.selectList(supervisionTaskQueryWrapper);
        List<CourseStudent> courseStudentList=new ArrayList<>();
        for(SupervisionTask supervisionTask:supervisionTaskList){
            User user=userMapper.selectById(supervisionTask.getUserid());
            CourseStudent courseStudent=new CourseStudent();
            courseStudent.setStuUserId(user.getId());
            courseStudent.setStudentNo(user.getNo());
            courseStudent.setStudentName(user.getName());
            courseStudentList.add(courseStudent);
        }
        return courseStudentList;
    }
}
