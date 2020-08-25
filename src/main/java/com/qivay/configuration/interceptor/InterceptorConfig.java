package com.qivay.configuration.interceptor;

//import com.qivay.dao.UserTicketDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

/**
 * Created by Administrator on 2017/10/18.
 */

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir = registry.addInterceptor(authInterceptor());
        // 配置拦截的路径
        ir.addPathPatterns("/**");
    }
}
