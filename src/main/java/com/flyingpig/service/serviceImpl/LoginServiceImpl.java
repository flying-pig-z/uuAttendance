package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.dto.LoginUser;
import com.flyingpig.mapper.UserMapper;
import com.flyingpig.pojo.Result;
import com.flyingpig.service.LoginService;
import com.flyingpig.dataobject.entity.User;
import com.flyingpig.util.JwtUtil;
import com.flyingpig.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Result login(User user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getNo(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        //把完整的用户信息存入redis  userid作为key
        redisCache.setCacheObject("login:"+userid,loginUser);
        Map<String,Object> map = new HashMap<>();
        map.put("token",jwt);
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("no",user.getNo());
        User selectuser=userMapper.selectOne(userQueryWrapper);
        map.put("name",selectuser.getName());
        map.put("id",selectuser.getId());
        map.put("identity",selectuser.getUserType());
        return Result.success(map);
    }

    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
        return new Result(200,"退出成功",null);

    }

    @Override
    public Integer getAuthenticateByUserId(String userId) {
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("id",userId);
        User user=userMapper.selectOne(userQueryWrapper);
        return user.getUserType();
    }

    @Override
    public void updateUserWithPassword(String userId,String newPassword) {
        User user=new User();
        user.setId(Integer.parseInt(userId));
        user.setPassword(newPassword);
        userMapper.updateById(user);
    }

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }
}
