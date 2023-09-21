package com.flyingpig.controller;

import com.flyingpig.dataobject.dto.LoginUser;
import com.flyingpig.dataobject.entity.User;
import com.flyingpig.pojo.Result;
import com.flyingpig.service.LoginService;
import com.flyingpig.util.JwtUtil;
import com.flyingpig.util.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisCache redisCache;
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        try{
            System.out.println(user);
            return loginService.login(user);
        }catch (Exception e) {
            return Result.error("账号或密码错误，请重新登录");
        }
    }
    @PostMapping ("/logout")
    public Result logout(){
        return loginService.logout();
    }
    @PutMapping("/password")
    public Result changePassword(@RequestHeader String Authorization,@RequestParam String oldPassword,@RequestParam String newPassword){
        Claims claims= JwtUtil.parseJwt(Authorization);
        String userId=claims.getSubject();
        // 从缓存中获取当前用户的密码
        LoginUser loginUser=redisCache.getCacheObject("login:"+userId);
        String passwordInDatabase = loginUser.getPassword();
        // 创建BCryptPasswordEncoder对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 判断传回来的旧密码是否与数据库中存储的密码一致
        if (passwordEncoder.matches(oldPassword, passwordInDatabase)) {
            // 对新密码进行加密
            String newEncodedPassword = passwordEncoder.encode(newPassword);
            // 更新数据库中的密码
            loginService.updateUserWithPassword(userId,newEncodedPassword);
            return Result.success("密码更新成功");
        } else {
            // 旧密码不匹配，返回错误提示
            return Result.error("密码更新失败");
        }
    }
    @GetMapping("/authenticate")
    public Result getAuthenticateByUserId(@RequestHeader String Authorization){
        Claims claims= JwtUtil.parseJwt(Authorization);
        String userid=claims.getSubject();
        return Result.success(loginService.getAuthenticateByUserId(userid));
    }
}
