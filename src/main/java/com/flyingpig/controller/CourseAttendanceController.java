package com.flyingpig.controller;

import com.flyingpig.common.PageBean;
import com.flyingpig.dataobject.constant.StatusCode;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.*;
import com.flyingpig.dataobject.vo.CourseAttendanceAddVO;
import com.flyingpig.dataobject.vo.CourseAttendanceQueryVO;
import com.flyingpig.dataobject.vo.SignInVO;
import com.flyingpig.common.Result;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.service.ExportService;
import com.flyingpig.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.File;
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

    @PutMapping("/signin")
    @ApiOperation("学生签到")
    public Result signIn(@RequestHeader String Authorization, @RequestBody SignInVO signInVO) {
        String userId = JwtUtil.parseJwt(Authorization).getSubject();
        System.out.println(signInVO.getLatitude());
        System.out.println(signInVO.getLongitude());

        if (courseAttendanceService.signIn(userId, signInVO)) {
            return Result.success("签到成功");
        } else {
            return Result.error(StatusCode.SERVERERROR, "签到失败");
        }
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PostMapping("")
    @ApiOperation("录入考勤信息")
    public Result addCourseAttendances(@RequestBody CourseAttendanceAddVO courseAttendanceAddVO, @RequestHeader String Authorization) {
        String userId = JwtUtil.parseJwt(Authorization).getSubject();
        courseAttendanceService.addCourseAttendances(courseAttendanceAddVO, userId);
        return Result.success();
    }

    @GetMapping("/courseTableInfo")
    @ApiOperation("学生获取课表/考勤表")
    public Result listCourseDetailWithStatusByWeekAndStudentId(Integer week, Integer semester, @RequestHeader String Authorization) {
        CourseDetail select = new CourseDetail();
        //设置学生id
        String id = JwtUtil.parseJwt(Authorization).getSubject();
        Integer userId = Integer.parseInt(id);
        List<CourseTableInfo> courseDetailWithStatusList = courseAttendanceService.getCourseTableInfoByWeekAndUserId(userId, week, semester);
        return Result.success(courseDetailWithStatusList);
    }

    @PreAuthorize("hasAuthority('sys:supervision:operation')")
    @GetMapping("/whoNoCheck")
    @ApiOperation("滑动获取还未签到的名单")
    public Result listWhoNoCheck(Integer courseId, @RequestParam(required = false, defaultValue = "1") Integer returneesNumber,
                                 @RequestParam(required = false, defaultValue = "-1,-1,-1") List<Integer> existingStudentId) {
        //调用service层的添加功能
        List<ResultAttendance> resultAttendanceList = courseAttendanceService.listWhoNoCheck(courseId, returneesNumber, existingStudentId);
        return Result.success(resultAttendanceList);
    }

    //获取这个课程的学生名单及其这节课的考勤情况
    @PreAuthorize("hasAuthority('sys:supervision:operation')")
    @GetMapping("")
    @ApiOperation("督导获取这个课程的学生名单及其这节课的考勤情况")
    public Result listCourseAttendanceBycourseId(Integer courseId) {
        //调用service层的添加功能id
        List<ResultAttendance> resultAttendanceList = courseAttendanceService.getresultAttendanceListByCourseId(courseId);
        return Result.success(resultAttendanceList);
    }

    //通过老师的id和学期还有课程名字获取到这个课程的学生
    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/student")
    @ApiOperation("教师通过其id和学期还有课程名字获取到这个课程的学生及其身份")
    public Result listStudentByTeauserIdAndsemesterAndCourseName(@RequestHeader String Authorization, Integer semester, String courseName) {
        String teaUserid = JwtUtil.parseJwt(Authorization).getSubject();
        List<CourseStudent> courseStudentList = courseAttendanceService.listStudentByTeauserIdAndsemesterAndCourseName(teaUserid, semester, courseName);
        return Result.success(courseStudentList);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/courseAttendanceList")
    @ApiOperation("教师分页查询课程签到")
    public Result pageCourseAttendance(@RequestHeader String Authorization, String courseName, Integer semester,
                                       @RequestParam(required = false) Integer week, @RequestParam(required = false) Integer weekday,
                                       @RequestParam(required = false) Integer beginSection, @RequestParam(required = false) Integer endSection,
                                       @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        Integer teaUserid = Integer.parseInt(JwtUtil.parseJwt(Authorization).getSubject());
        CourseAttendanceQueryVO courseAttendanceQueryVO = new CourseAttendanceQueryVO(teaUserid, courseName, semester, week, weekday, beginSection, endSection,
                pageNo, pageSize);
        PageBean resultPage = courseAttendanceService.pageCourseAttendance(courseAttendanceQueryVO);
        return Result.success(resultPage);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/studentAttendanceList")
    @ApiOperation("教师分页查询学生签到")
    public Result pageStudentAttendanceByStudentInfo(@RequestHeader String Authorization, String courseName, Integer semester,
                                                     String studentNo,
                                                     @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        Integer teaUserid = Integer.parseInt(JwtUtil.parseJwt(Authorization).getSubject());
        PageBean resultPage = courseAttendanceService.pageStudentAttendanceByteaUserIdAndCourseInfoAndStudentNo(teaUserid, courseName
                , semester, studentNo, pageNo, pageSize);
        return Result.success(resultPage);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/export/studentAttendanceList")
    @ApiOperation("教师导出其所查看的学生签到页面的数据")
    public File exportstudentAttendanceList(@RequestHeader String Authorization, String courseName, Integer semester,
                                            String studentNo) {
        Integer teaUserid = Integer.parseInt(JwtUtil.parseJwt(Authorization).getSubject());
        return(exportService.exportStudentAttendance(teaUserid, courseName
                , semester, studentNo));
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/export/courseAttendanceList")
    @ApiOperation("教师导出其所查看的课程签到页面的数据")
    public File exportcourseAttendance(@RequestHeader String Authorization, String courseName, Integer semester,
                                       @RequestParam(required = false) Integer week, @RequestParam(required = false) Integer weekday,
                                       @RequestParam(required = false) Integer beginSection, @RequestParam(required = false) Integer endSection) {
        Integer teaUserid = Integer.parseInt(JwtUtil.parseJwt(Authorization).getSubject());
        return exportService.exportCourseAttendance(teaUserid, courseName, semester, week, weekday, beginSection, endSection);
    }
}