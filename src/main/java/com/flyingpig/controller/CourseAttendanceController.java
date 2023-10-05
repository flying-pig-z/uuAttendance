package com.flyingpig.controller;

import com.flyingpig.common.PageBean;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.*;
import com.flyingpig.dataobject.vo.CourseAttendanceAddVO;
import com.flyingpig.dataobject.vo.CourseAttendanceQueryVO;
import com.flyingpig.dataobject.vo.SignInVO;
import com.flyingpig.common.Result;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.service.ExportService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courseAttendances")
public class CourseAttendanceController {
    @Autowired
    private CourseAttendanceService courseAttendanceService;
    @Autowired
    private ExportService exportService;

    @PreAuthorize("hasAuthority('sys:supervision:operation')")
    @PutMapping("/status")
    public Result updateAttendanceStatusByStudentIdAndCourseId(@RequestBody CourseAttendance attendance) {
        //调用service层的添加功能
        courseAttendanceService.updateAttendanceStatus(attendance);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('sys:student:operation')")
    @PutMapping("/signin")
    public Result signIn(@RequestHeader String Authorization, @RequestBody SignInVO signInVO) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        String userId = claims.getSubject();
        System.out.println(signInVO.getLatitude());
        System.out.println(signInVO.getLatitude());
        if (courseAttendanceService.signIn(userId, signInVO)) {
            return Result.success("签到成功");
        } else {
            return Result.error("签到失败");
        }
    }

    @PreAuthorize("hasAuthority('sys:supervision:operation')")
    @PostMapping("")
    public Result addCourseAttendances(@RequestBody CourseAttendanceAddVO courseAttendanceAddVO, @RequestHeader String Authorization) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        String userId = claims.getSubject();
        courseAttendanceService.addCourseAttendances(courseAttendanceAddVO, userId);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('sys:student:operation')")
    @GetMapping("/courseTableInfo")
    public Result listCourseDetailWithStatusByWeekAndStudentId(@RequestParam("week") Integer week, @RequestParam("semester") Integer semester, @RequestHeader String Authorization) {
        CourseDetail select = new CourseDetail();
        //设置学生id
        Claims claims = JwtUtil.parseJwt(Authorization);
        String id = claims.getSubject();
        Integer userId = Integer.parseInt(id);
        List<CourseTableInfo> courseDetailWithStatusList = courseAttendanceService.getCourseTableInfoByWeekAndUserId(userId, week, semester);
        return Result.success(courseDetailWithStatusList);
    }

    @PreAuthorize("hasAuthority('sys:supervision:operation')")
    @GetMapping("/whoNoCheck")
    public Result listWhoNoCheck(@RequestParam("courseId") Integer courseId, @RequestParam(required = false, defaultValue = "1") Integer returneesNumber,
                                 @RequestParam(required = false, defaultValue = "-1,-1,-1") List<Integer> existingStudentId) {
        //调用service层的添加功能
        List<ResultAttendance> resultAttendanceList = courseAttendanceService.listWhoNoCheck(courseId, returneesNumber, existingStudentId);
        return Result.success(resultAttendanceList);
    }

    //获取这个课程的学生名单及其这节课的考勤情况
    @PreAuthorize("hasAuthority('sys:supervision:operation')")
    @GetMapping("")
    public Result listCourseAttendanceBycourseId(@RequestParam("courseId") Integer courseId) {
        //调用service层的添加功能id
        List<ResultAttendance> resultAttendanceList = courseAttendanceService.getresultAttendanceListByCourseId(courseId);
        return Result.success(resultAttendanceList);
    }

    //通过老师的id和学期还有课程名字获取到这个课程的学生
    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/student")
    public Result listStudentByTeauserIdAndsemesterAndCourseName(@RequestHeader String Authorization, @RequestParam Integer semester, @RequestParam String courseName) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        String teaUserid = claims.getSubject();
        List<CourseStudent> courseStudentList = courseAttendanceService.getStudentByTeauserIdAndsemesterAndCourseName(teaUserid, semester, courseName);
        return Result.success(courseStudentList);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/courseAttendanceList")
    public Result pageCourseAttendance(@RequestHeader String Authorization, @RequestParam String courseName, @RequestParam Integer semester,
                                       @RequestParam(required = false) Integer week, @RequestParam(required = false) Integer weekday,
                                       @RequestParam(required = false) Integer beginSection, @RequestParam(required = false) Integer endSection,
                                       @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        Integer teaUserid = Integer.parseInt(claims.getSubject());
        CourseAttendanceQueryVO courseAttendanceQueryVO = new CourseAttendanceQueryVO(teaUserid, courseName, semester, week, weekday, beginSection, endSection,
                pageNo, pageSize);
        PageBean resultPage = courseAttendanceService.pageCourseAttendance(courseAttendanceQueryVO);
        return Result.success(resultPage);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/studentAttendanceList")
    public Result pageStudentAttendanceByStudentInfo(@RequestHeader String Authorization, @RequestParam String courseName, @RequestParam Integer semester,
                                                     @RequestParam String studentNo,
                                                     @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        Integer teaUserid = Integer.parseInt(claims.getSubject());
        PageBean resultPage = courseAttendanceService.pageStudentAttendanceByteaUserIdAndCourseInfoAndStudentNo(teaUserid, courseName
                , semester, studentNo, pageNo, pageSize);
        return Result.success(resultPage);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/export/studentAttendanceList")
    public void exportstudentAttendanceList(HttpServletResponse response, @RequestHeader String Authorization, @RequestParam String courseName, @RequestParam Integer semester,
                                            @RequestParam String studentNo) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        Integer teaUserid = Integer.parseInt(claims.getSubject());
        exportService.exportStudentAttendance(response, teaUserid, courseName
                , semester, studentNo);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/export/courseAttendanceList")
    public void exportcourseAttendance(HttpServletResponse response, @RequestHeader String Authorization, @RequestParam String courseName, @RequestParam Integer semester,
                                       @RequestParam(required = false) Integer week, @RequestParam(required = false) Integer weekday,
                                       @RequestParam(required = false) Integer beginSection, @RequestParam(required = false) Integer endSection) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        Integer teaUserid = Integer.parseInt(claims.getSubject());
        exportService.exportCourseAttendance(response, teaUserid, courseName, semester, week, weekday, beginSection, endSection);
    }
}