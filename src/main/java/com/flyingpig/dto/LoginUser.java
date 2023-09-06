package com.flyingpig.dto;

import com.flyingpig.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails{
    private User user;
    //获取用户权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
