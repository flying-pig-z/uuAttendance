package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flyingpig.dataobject.dto.LoginUser;
import com.flyingpig.dataobject.entity.UserRoleRelation;
import com.flyingpig.mapper.UserMapper;
import com.flyingpig.common.Result;
import com.flyingpig.mapper.UserRoleRelationMapper;
import com.flyingpig.service.LoginService;
import com.flyingpig.dataobject.entity.User;
import com.flyingpig.util.cache.CacheUtil;
import com.flyingpig.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.flyingpig.dataobject.constant.RedisConstants.*;

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    UserRoleRelationMapper userRoleRelationMapper;

    @Override
    public Result login(User user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getNo(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        System.out.println(loginUser);
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);

        //把完整的用户信息存入redis  userid作为key
        cacheUtil.set(LOGIN_USER_KEY + userid, loginUser, USER_INFO_TTL, TimeUnit.DAYS);
        cacheUtil.set(USER_INFO_KEY + userid, loginUser.getUser(), USER_INFO_TTL, TimeUnit.DAYS);

        Map<String, Object> map = new HashMap<>();
        map.put("token", jwt);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("no", user.getNo());
        User selectuser = userMapper.selectOne(userQueryWrapper);
        map.put("name", selectuser.getName());
        map.put("id", selectuser.getId());
        map.put("identity", selectuser.getUserType());
        return Result.success(map);
    }

    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer userid = loginUser.getUser().getId();
        cacheUtil.delete(USER_INFO_KEY + userid);
        return new Result(200, "退出成功", null);
    }

    @Override
    public Integer getAuthenticateByUserId(String userId) {
        User user = cacheUtil.safeGetWithLock(USER_INFO_KEY + userId, User.class, () -> {
            return userMapper.selectById(userId);
        }, USER_INFO_TTL, TimeUnit.DAYS);
        return user.getUserType();
    }

    @Override
    public void updateUserWithPassword(String no, String newPassword) {
        User user = new User();
        user.setNo(no);
        user.setPassword(newPassword);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("no", no);
        userMapper.update(user, userQueryWrapper);
    }

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
        UserRoleRelation userRoleRelation = new UserRoleRelation(user.getId(), 3);
        userRoleRelationMapper.insert(userRoleRelation);
    }

    @Override
    public String getPasswordByNo(String no) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("no", no);
        return userMapper.selectOne(userQueryWrapper).getPassword();
    }
}
