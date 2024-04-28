package com.flyingpig.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.constant.RabbitMQConstants;
import com.flyingpig.dataobject.entity.CourseAttendance;
import com.flyingpig.dataobject.message.SignInMessage;
import com.flyingpig.mapper.CourseAttendanceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class SignInListener {
    @Autowired
    CourseAttendanceMapper courseAttendanceMapper;

    @RabbitListener(queues = RabbitMQConstants.SIGNIN_QUEUE_NAME1)
    public void handleSignInRequest1(SignInMessage signInMessage) {
        try {
            CourseAttendance courseAttendance = new CourseAttendance();
            courseAttendance.setStatus(1);
            QueryWrapper<CourseAttendance> courseAttendanceQueryWrapper = new QueryWrapper<>();
            courseAttendanceQueryWrapper.eq("course_id", signInMessage.getCourseId());
            courseAttendanceQueryWrapper.eq("student_id", signInMessage.getStudentId());
            courseAttendanceMapper.update(courseAttendance, courseAttendanceQueryWrapper);
        } catch (Exception e) {
            // 异常处理
            log.error("签到失败");
            throw new AmqpRejectAndDontRequeueException("处理签到失败，将消息丢弃");
        }

    }

    @RabbitListener(queues = RabbitMQConstants.SIGNIN_QUEUE_NAME2)
    public void handleSignInRequest2(SignInMessage signInMessage) {
        try {
            CourseAttendance courseAttendance = new CourseAttendance();
            courseAttendance.setStatus(1);
            QueryWrapper<CourseAttendance> courseAttendanceQueryWrapper = new QueryWrapper<>();
            courseAttendanceQueryWrapper.eq("course_id", signInMessage.getCourseId());
            courseAttendanceQueryWrapper.eq("student_id", signInMessage.getStudentId());
            courseAttendanceMapper.update(courseAttendance, courseAttendanceQueryWrapper);
        } catch (Exception e) {
            // 异常处理
            log.error("签到失败");
            throw new AmqpRejectAndDontRequeueException("处理签到失败，将消息丢弃");
        }

    }
}
