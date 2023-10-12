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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courseAttendances")
@Api("与课程考勤表相关的api")
public class CourseAttendanceController {
    @Autowired
    private CourseAttendanceService courseAttendanceService;
    @Autowired
    private ExportService exportService;

    @PreAuthorize("hasAuthority('sys:supervision:operation')")
    @PutMapping("/status")
    @ApiOperation("督导点名，更改学生考勤状态")
    public Result updateAttendanceStatusByStudentIdAndCourseId(@RequestBody CourseAttendance attendance) {
        //调用service层的添加功能
        courseAttendanceService.updateAttendanceStatus(attendance);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('sys:student:operation')")
    @PutMapping("/signin")
    @ApiOperation("学生签到")
    public Result signIn(@RequestHeader String Authorization, @RequestBody SignInVO signInVO) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        String userId = claims.getSubject();
        System.out.println(signInVO.getLatitude());
        System.out.println(signInVO.getLatitude());
        if (courseAttendanceService.signIn(userId, signInVO)) {
            return Result.success("签到成功");
        } else {
            return Result.error(2,"签到失败");
        }
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PostMapping("")
    @ApiOperation("录入考勤信息")
    public Result addCourseAttendances(@RequestBody CourseAttendanceAddVO courseAttendanceAddVO, @RequestHeader String Authorization) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        String userId = claims.getSubject();
        courseAttendanceService.addCourseAttendances(courseAttendanceAddVO, userId);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('sys:student:operation')")
    @GetMapping("/courseTableInfo")
    @ApiOperation("学生获取课表/考勤表")
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
    @ApiOperation("滑动获取还未签到的名单")
    public Result listWhoNoCheck(@RequestParam("courseId") Integer courseId, @RequestParam(required = false, defaultValue = "1") Integer returneesNumber,
                                 @RequestParam(required = false, defaultValue = "-1,-1,-1") List<Integer> existingStudentId) {
        //调用service层的添加功能
        List<ResultAttendance> resultAttendanceList = courseAttendanceService.listWhoNoCheck(courseId, returneesNumber, existingStudentId);
        return Result.success(resultAttendanceList);
    }

    //获取这个课程的学生名单及其这节课的考勤情况
    @PreAuthorize("hasAuthority('sys:supervision:operation')")
    @GetMapping("")
    @ApiOperation("督导获取这个课程的学生名单及其这节课的考勤情况")
    public Result listCourseAttendanceBycourseId(@RequestParam("courseId") Integer courseId) {
        //调用service层的添加功能id
        List<ResultAttendance> resultAttendanceList = courseAttendanceService.getresultAttendanceListByCourseId(courseId);
        return Result.success(resultAttendanceList);
    }

    //通过老师的id和学期还有课程名字获取到这个课程的学生
    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/student")
    @ApiOperation("教师通过其id和学期还有课程名字获取到这个课程的学生及其身份")
    public Result listStudentByTeauserIdAndsemesterAndCourseName(@RequestHeader String Authorization, @RequestParam Integer semester, @RequestParam String courseName) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        String teaUserid = claims.getSubject();
        List<CourseStudent> courseStudentList = courseAttendanceService.getStudentByTeauserIdAndsemesterAndCourseName(teaUserid, semester, courseName);
        return Result.success(courseStudentList);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/courseAttendanceList")
    @ApiOperation("教师分页查询课程签到")
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
    @ApiOperation("教师分页查询学生签到")
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
    @ApiOperation("教师导出其所查看的学生签到页面的数据")
    public void exportstudentAttendanceList(HttpServletResponse response, @RequestHeader String Authorization, @RequestParam String courseName, @RequestParam Integer semester,
                                            @RequestParam String studentNo) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        Integer teaUserid = Integer.parseInt(claims.getSubject());
        exportService.exportStudentAttendance(response, teaUserid, courseName
                , semester, studentNo);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/export/courseAttendanceList")
    @ApiOperation("教师导出其所查看的课程签到页面的数据")
    public void exportcourseAttendance(HttpServletResponse response, @RequestHeader String Authorization, @RequestParam String courseName, @RequestParam Integer semester,
                                       @RequestParam(required = false) Integer week, @RequestParam(required = false) Integer weekday,
                                       @RequestParam(required = false) Integer beginSection, @RequestParam(required = false) Integer endSection) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        Integer teaUserid = Integer.parseInt(claims.getSubject());
        exportService.exportCourseAttendance(response, teaUserid, courseName, semester, week, weekday, beginSection, endSection);
    }
}