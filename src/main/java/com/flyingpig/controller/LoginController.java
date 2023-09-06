package com.flyingpig.controller;
import com.flyingpig.service.*;
import com.flyingpig.entity.*;
import com.flyingpig.pojo.Result;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private LoginService loginService;
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
    @GetMapping("/authenticate")
    public Result getAuthenticateByUserId(@RequestHeader String Authorization){
        Claims claims= JwtUtil.parseJwt(Authorization);
        String userid=claims.getSubject();
        return Result.success(loginService.getAuthenticateByUserId(userid));
    }
}
