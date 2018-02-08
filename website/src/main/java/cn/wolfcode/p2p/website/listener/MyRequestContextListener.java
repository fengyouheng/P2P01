package cn.wolfcode.p2p.website.listener;

import cn.wolfcode.p2p.base.util.MyRequestContextHolder;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by seemygo on 2018/1/18.
 */
public class MyRequestContextListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        //HttpServletRequest的监听创建方法
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        System.out.println("Listen中的Request对象:"+request);
        //把Request对象绑定到当前的线程中
        MyRequestContextHolder.setHttpRequest(request);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        //HttpServletRequest的监听销毁方法
    }
}
