package com.flyingpig.config;
//import com.flyingpig.filter.JwtAuthenticationTokenFilter;

import com.flyingpig.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不会创建HttpSession并且不通过Session获取SecurityContext对象,因为前后端分离基本session就已经灭有用了
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //对请求认证规则进行相应配置
                .authorizeRequests()
                //1.对于登录,修改密码和注册接口以及swagger相关的静态资源放行(antMatchers)
                // 并且在后面加上anonymous方法允许匿名访问而不允许在已登录状态访问
                //简单说加上anonymous在没有token的情况下可以访问，携带token反而不能访问，一般用于登录注册接口之类的
                //与之相对应的是加上permiAll()，加上后这个接口有无token(有无身份)都可以访问，一般用于静态资源的放行
                .antMatchers("/user/login").anonymous()
                .antMatchers("/user/password").permitAll()
                .antMatchers("/user/email/verificationCode").anonymous()
                .antMatchers("/user/email/register").anonymous()
                .antMatchers("/doc.html").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/swagger-resources").anonymous()
                .antMatchers("/v2/api-docs").anonymous()
                //2.除上面外的所有请求任意的用户认证之后可以访问
                //后面会进行精细化授权
                .anyRequest().authenticated();
        //把token校验过滤器添加到过滤器链中
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //允许跨域
        http.cors();
        //配置异常处理器
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).
                accessDeniedHandler(accessDeniedHandler);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

