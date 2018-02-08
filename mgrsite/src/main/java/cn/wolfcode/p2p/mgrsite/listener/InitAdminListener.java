package cn.wolfcode.p2p.mgrsite.listener;

import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.business.service.ISystemAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by seemygo on 2018/1/20.
 */
@Component
public class InitAdminListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ILogininfoService logininfoService;
    @Autowired
    private ISystemAccountService systemAccountService;
    
    public void onApplicationEvent(ContextRefreshedEvent event) {
       logininfoService.initAdmin();
       systemAccountService.initSystemAccount(); //初始化账号信息
    }
}
