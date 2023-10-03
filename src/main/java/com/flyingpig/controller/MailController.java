package com.flyingpig.controller;

import com.flyingpig.dataobject.entity.User;
import com.flyingpig.dataobject.vo.EmailRegisterVO;
import com.flyingpig.common.Result;
import com.flyingpig.service.LoginService;
import com.flyingpig.util.EmailUtil;
import com.flyingpig.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user/email")
public class MailController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private JavaMailSenderImpl mailSender;
    //这里要使用工具类，不然各个方法之间的redis数据无法共用
    @Autowired
    private RedisCache redisCache;
    @Value("${spring.mail.username}")
    private String emailUserName;

    @GetMapping("/verificationCode")
    public Result sendEmailVerificationCode(@RequestParam  String email) {
        //检查email是否符合格式
        if(EmailUtil.judgeEmailFormat(email)){
            return Result.error("邮箱不符合格式");
        }
        //检查redis中有无验证码，如果有，则返回已存在
        String verificationCode=redisCache.getCacheObject(email);
        if(verificationCode!=null){
            return Result.error("验证码已存在，请误重复发送");
        }else {
            //如果没有，生成验证码存入缓存并发送
            verificationCode = EmailUtil.createVerificationCode();
            //存入缓存
            redisCache.setCacheObject(email, verificationCode, 120, TimeUnit.SECONDS);
            //发送
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailUserName);//设置发件qq邮箱
            message.setTo(email);      // 设置收件人邮箱地址
            message.setSubject("验证码");    // 设置邮件主题
            message.setText(verificationCode);   // 设置邮件正文
            mailSender.send(message);
            return Result.success("验证码已发送");
        }
    }

    @PostMapping("/register")
    public Result emailRegister(@RequestBody EmailRegisterVO emailRegisterVO) {
        System.out.println(emailRegisterVO.getEmail());
        String verificationCode=redisCache.getCacheObject(emailRegisterVO.getEmail());
        System.out.println(verificationCode);
        if(verificationCode!=null&&verificationCode.equals(emailRegisterVO.getVerificationCode())){
            //添加用户
            User user=new User();
            user.setNo(emailRegisterVO.getNo());
            user.setPassword(emailRegisterVO.getPassword());
            user.setUserType(0);
            loginService.addUser(user);
            return Result.success("添加成功,请联系管理员审核");
        }else {
            return Result.error("验证码验证错误");
        }

    }
}
