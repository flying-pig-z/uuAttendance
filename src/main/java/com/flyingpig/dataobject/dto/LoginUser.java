package com.flyingpig.dataobject.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.flyingpig.dataobject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails{
    private User user;
    //存储权限信息
    private List<String> permissions;
    //存储SpringSecurity所需要的权限信息的集合
    //为了避免下面获取权限的方法调用都需要进行封装，我们将这个变量作为成员变量
    //redis默认在存储的时候不会将SimpleGrantedAuthority序列化，加上这个注解使得成员变量不会存储到redis
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
    public LoginUser(User user,List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
    //获取用户权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //把permissions中String类型的权限信息封装成SimpleGrantedAuthority权限类对象
        if(authorities!=null){
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        // 这里采用stream流编写
        authorities = permissions.stream().
                map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }
    //判断用户名和密码是否没过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //返回用户名
    @Override
    public String getUsername(){
        return user.getNo();
    }
    //返回密码
    @Override
    public String getPassword(){
        return user.getPassword();
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
