package com.flyingpig.controller;

import com.flyingpig.dataobject.dto.AttendanceAppealWithCourseName;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.AttendanceAppealDetail;
import com.flyingpig.common.PageBean;
import com.flyingpig.common.Result;
import com.flyingpig.service.AttendanceAppealService;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.service.CourseDetailService;
import com.flyingpig.service.StudentService;
import com.flyingpig.util.AliOSSUtils;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/attendanceAppeals")
@Api("与考勤申诉表相关的api")
public class AttendanceAppealController {
    @Autowired
    CourseDetailService courseDetailService;
    @Autowired
    AttendanceAppealService attendanceAppealService;
    @Autowired
    CourseAttendanceService courseAttendanceService;
    @Autowired
    StudentService studentService;
    @Autowired
    AliOSSUtils aliOSSUtils;

    @PostMapping("")
    @ApiOperation("学生考勤申诉")
    public Result addAttendanceAppeal(@RequestHeader String Authorization, Integer courseId,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime appealBeginTime,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime appealEndTime,
                                      String reason, String status, @RequestParam MultipartFile attendanceAppealImage) throws IOException {
        //设置userid
        Long userid = Long.parseLong(JwtUtil.parseJwt(Authorization).getSubject());
        Map<String, Object> studentInfo = studentService.getStudentInfoByUserId(userid);
        AttendanceAppeal attendanceAppeal = new AttendanceAppeal(null, null, courseId, appealBeginTime, appealEndTime, status, reason, null);
        attendanceAppeal.setStatus("0");
        attendanceAppeal.setStudentId((Integer) studentInfo.get("id"));
        //调用阿里云OSS工具类，将上传上来的文件存入阿里云
        attendanceAppeal.setImage(aliOSSUtils.upload(attendanceAppealImage));
        attendanceAppealService.addAttendanceAppeal(attendanceAppeal);
        return Result.success();
    }

    @GetMapping("/student")
    @ApiOperation("学生查看考勤申诉列表")
    public Result listAttendanceAppealByStuUserId(@RequestHeader String Authorization) {
        //设置学生id
        Integer userId = Integer.parseInt(JwtUtil.parseJwt(Authorization).getSubject());
        List<AttendanceAppealWithCourseName> result = attendanceAppealService.listAttendanceAppealByStuUserId(userId);
        return Result.success(result);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/teaAttendanceAppealSummary")
    @ApiOperation("教师分页查询所属学生的考勤申诉")
    public Result pageAttendanceAppealByTeacherId(Integer pageNo, Integer pageSize, @RequestHeader String Authorization) {
        //设置教师id
        Integer userid = Integer.parseInt(JwtUtil.parseJwt(Authorization).getSubject());
        PageBean result = attendanceAppealService.pageAttendanceAppealSummaryByTeaUserId(pageNo, pageSize, userid);
        return Result.success(result);
    }

    //查询对应申诉的详细信息
    @GetMapping("/{attendanceAppealId}/attendanceAppealDetail")
    @ApiOperation("学生和老师查询想要查询的考勤申诉的详细信息")
    public Result getAttendanceAppealDetail(@RequestHeader String Authorization, @PathVariable Long attendanceAppealId) {
        AttendanceAppealDetail resultAttendanceAppealDetail = attendanceAppealService.getAttendanceAppealDetail(attendanceAppealId);
        return Result.success(resultAttendanceAppealDetail);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PutMapping("/{attendanceAppealId}")
    @ApiOperation("老师决定考勤申诉通不通过")
    public Result judgeLeave(@PathVariable Integer attendanceAppealId, String status) {
        attendanceAppealService.updateByAttendanceAppealIdAndStatus(attendanceAppealId, status);
        return Result.success();
    }

}
