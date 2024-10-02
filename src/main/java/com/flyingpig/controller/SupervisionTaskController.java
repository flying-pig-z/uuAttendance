package com.flyingpig.controller;

import com.flyingpig.common.PageBean;
import com.flyingpig.common.Result;
import com.flyingpig.dataobject.vo.SupervisionTaskAddVO;
import com.flyingpig.service.SupervisionTaskService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/supervisiontasks")
@Api("与督导任务表相关的api")
public class SupervisionTaskController {
    @Autowired
    private SupervisionTaskService supervisionTaskService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('sys:supervision:operation')")
    @ApiOperation("督导分页查询督导任务")
    public Result pageSupervisonTaskBySupervisonId(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "5") Integer pageSize, @RequestHeader String Authorization) {
        //记录日志
        log.info("分页查询，参数：{}，{}", pageNo, pageSize);
        //调用业务层分页查询功能
        Integer userId = Integer.parseInt(JwtUtil.parseJwt(Authorization).getSubject());
        PageBean pageBean = supervisionTaskService.pageSupervisonTaskBySupervisonId(pageNo, pageSize, userId);
        //响应
        return Result.success(pageBean);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PostMapping("")
    @ApiOperation("教师指定督导某个课程考勤")
    public Result addSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(@RequestHeader String Authorization, @RequestBody SupervisionTaskAddVO supervisionTaskAddVO) {
        String teaUserid = JwtUtil.parseJwt(Authorization).getSubject();
        supervisionTaskService.addSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(teaUserid, supervisionTaskAddVO);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @DeleteMapping("")
    @ApiOperation("教师删除督导某个课程考勤")
    public Result deleteSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(@RequestHeader String Authorization, @RequestBody SupervisionTaskAddVO supervisionTaskAddVO) {
        String teaUserid = JwtUtil.parseJwt(Authorization).getSubject();
        supervisionTaskService.deleteSupervisonTaskByTeaUserIdAndSupervisionTaskAddVO(teaUserid, supervisionTaskAddVO);
        return Result.success();
    }
}
