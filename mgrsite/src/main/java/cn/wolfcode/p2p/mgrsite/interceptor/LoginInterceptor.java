package cn.wolfcode.p2p.mgrsite.interceptor;

import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by seemygo on 2018/1/21.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //说明该方法是需要登录才能访问.判断当前用户是否有登录
        if(UserContext.getCurrent()==null){
            response.sendRedirect("/login.html");
            return false;
        }
        return true;
    }
}
