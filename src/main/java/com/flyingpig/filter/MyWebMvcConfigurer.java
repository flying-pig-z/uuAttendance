package com.flyingpig.filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** * 在webconfig中配置拦截器 */
@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {


    @Autowired
    private AccessLimtInterceptor accessLimtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(accessLimtInterceptor);
        super.addInterceptors(registry);
    }
}
