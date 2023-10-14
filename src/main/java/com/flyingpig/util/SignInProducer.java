//package com.flyingpig.util;
//
//import com.flyingpig.dataobject.vo.SignInMessage;
//import com.flyingpig.dataobject.vo.SignInVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SignInProducer {
//
//    private final AmqpTemplate rabbitTemplate;
//
//    @Autowired
//    public SignInProducer(AmqpTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void sendSignInRequest(String userId, SignInVO signInVO) {
//        rabbitTemplate.convertAndSend("sign_in_queue", new SignInMessage(userId, signInVO));
//    }
//}
