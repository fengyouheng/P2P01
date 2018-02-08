package cn.wolfcode.p2p;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.wolfcode.p2p.mgrsite.interceptor.LoginInterceptor;

/**
 * Created by seemygo on 2018/1/21.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/*").excludePathPatterns("/managerLogin");
    }
    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }
}
