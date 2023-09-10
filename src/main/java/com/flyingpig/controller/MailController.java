package com.flyingpig.controller;

import com.flyingpig.pojo.Result;
import com.flyingpig.util.EmailUtil;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/mails")
public class MailController {
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${spring.mail.username}")
    private String emailUserName;
    @GetMapping("")
    public Result sendEmailVerificationCode(@RequestHeader String Authorization ,@RequestParam  String email) {
        //检查email是否符合格式

        //获取用户身份
        Claims claims= JwtUtil.parseJwt(Authorization);
        String userId=claims.getSubject();
        //检查redis中有无验证码，如果有，则返回已存在
        ValueOperations<String, String> operation = redisTemplate.opsForValue();
        String verificationCode=operation.get(userId);
        if(verificationCode!=null){
            return Result.error("验证码已存在，请误重复发送");
        }else{
            //如果没有，生成验证码存入缓存并发送
            verificationCode= EmailUtil.createVerificationCode();
            //存入缓存
            redisTemplate.opsForValue().set(userId, verificationCode, 120, TimeUnit.SECONDS);
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
}
