package com.flyingpig.controller;

import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.*;
import com.flyingpig.dataobject.vo.CourseAttendanceAddVO;
import com.flyingpig.dataobject.vo.SignInVO;
import com.flyingpig.dataobject.vo.SupervisionTaskAddVO;
import com.flyingpig.pojo.Result;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courseAttendances")
public class CourseAttendanceController {
    @Autowired
    private CourseAttendanceService courseAttendanceService;
    @PutMapping ("/status")
    public Result updateAttendanceStatusByStudentIdAndCourseId(@RequestBody CourseAttendance attendance) {
        //调用service层的添加功能
        courseAttendanceService.updateAttendanceStatus(attendance);
        return Result.success();
    }

    @PutMapping("/signin")
    public Result signIn(@RequestHeader String Authorization,@RequestBody SignInVO signInVO){
        Claims claims= JwtUtil.parseJwt(Authorization);
        String userId=claims.getSubject();
        if(courseAttendanceService.signIn(userId,signInVO)){
            return Result.success("签到成功");
        }
        else {
            return Result.error("签到失败");
        }
    }
    @PostMapping("")
    public Result addCourseAttendances(@RequestBody CourseAttendanceAddVO courseAttendanceAddVO, @RequestHeader String Authorization){
        Claims claims=JwtUtil.parseJwt(Authorization);
        String userId=claims.getSubject();
        courseAttendanceService.addCourseAttendances(courseAttendanceAddVO,userId);
        return Result.success();
    }
    @GetMapping("/courseTableInfo")
    public Result getCourseDetailWithStatusByWeekAndStudentId(@RequestParam("week") Integer week,@RequestParam("semester") Integer semester,@RequestHeader String Authorization){
        CourseDetail select=new CourseDetail();
        //设置学生id
        Claims claims= JwtUtil.parseJwt(Authorization);
        String id=claims.getSubject();
        Integer userId=Integer.parseInt(id);
        List<CourseTableInfo> courseDetailWithStatusList = courseAttendanceService.getCourseTableInfoByWeekAndUserId(userId,week,semester);
        return Result.success(courseDetailWithStatusList);
    }
    @GetMapping("/whoNoCheck")
    public Result getWhoNoCheck(@RequestParam("courseId") Integer courseId) {
        //调用service层的添加功能
        ResultAttendance resultAttendance=courseAttendanceService.getWhoNoCheck(courseId);
        return Result.success(resultAttendance);
    }
    //获取这个课程的学生名单及其这节课的考勤情况
    @GetMapping("")
    public Result getCourseAttendanceBycourseId(@RequestParam("courseId") Integer courseId) {
        //调用service层的添加功能id
        List<ResultAttendance> resultAttendanceList= courseAttendanceService.getresultAttendanceListByCourseId(courseId);
        return Result.success(resultAttendanceList);
    }
    //通过老师的id和学期还有课程名字获取到这个课程的学生
    @GetMapping("/student")
    public Result getStudentByTeauserIdAndsemesterAndCourseName(@RequestHeader String Authorization,@RequestParam Integer semester,@RequestParam String courseName){
        Claims claims= JwtUtil.parseJwt(Authorization);
        String teaUserid=claims.getSubject();
        List<CourseStudent> courseStudentList=courseAttendanceService.getStudentByTeauserIdAndsemesterAndCourseName(teaUserid,semester,courseName);
        return Result.success(courseStudentList);
    }
}