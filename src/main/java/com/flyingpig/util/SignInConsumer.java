//package com.flyingpig.util;
//
//import com.flyingpig.dataobject.vo.SignInMessage;
//import com.flyingpig.dataobject.vo.SignInVO;
//import com.flyingpig.service.CourseAttendanceService;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//@Component
//public class SignInConsumer {
//    @Resource
//    CourseAttendanceService courseAttendanceService;
//
//    @RabbitListener(queues = "sign_in_queue")
//    public void processSignInRequest(SignInMessage message) {
//        String userId = message.getUserId();
//        SignInVO signInVO = message.getSignInVO();
//
//        // 调用签到服务进行签到处理
//        if (courseAttendanceService.signIn(userId, signInVO)) {
//            System.out.println("签到成功");
//        } else {
//            System.out.println("签到失败");
//        }
//    }
//}
