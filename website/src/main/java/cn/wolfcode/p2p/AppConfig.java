package cn.wolfcode.p2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by seemygo on 2018/1/17.
 */
@SpringBootApplication
public class AppConfig {
    /*@Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean listenerRegistrationBean = new ServletListenerRegistrationBean();
        listenerRegistrationBean.setListener(requestContextListener());
        return listenerRegistrationBean;
    }
    @Bean
    public MyRequestContextListener requestContextListener(){
        return new MyRequestContextListener();
    }*/
    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class,args);
    }
}
