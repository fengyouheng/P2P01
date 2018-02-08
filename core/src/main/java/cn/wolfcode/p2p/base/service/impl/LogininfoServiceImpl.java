package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.LogininfoMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.MD5;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by seemygo on 2018/1/17.
 */
@Service@Transactional
public class LogininfoServiceImpl implements ILogininfoService {
    @Autowired
    private LogininfoMapper logininfoMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IIpLogService ipLogService;
    public Logininfo register(String username, String password) {
        //1.根据用户判断该用户名在数据库中是否已经存在
        int count = logininfoMapper.selectCountByUsername(username);
        //如果存在，抛出异常
        if(count>0){
            throw new RuntimeException("账户名已存在.");
        }
        //如果不存在,保存入库
        Logininfo logininfo = new Logininfo();
        logininfo.setUsername(username);
        logininfo.setPassword(MD5.encode(password));
        logininfo.setState(Logininfo.STATE_NORMAL);
        logininfo.setUserType(Logininfo.USERTYPE_USER);
        logininfoMapper.insert(logininfo);

        Account account = new Account();
        account.setId(logininfo.getId());
        accountService.save(account);

        Userinfo userinfo = new Userinfo();
        userinfo.setId(logininfo.getId());
        userinfoService.save(userinfo);
        return logininfo;
    }

    public boolean checkUsername(String username) {
        //根据传入的用户名去数据库汇总查找。
        /*int count = logininfoMapper.selectCountByUsername(username);
        //如果没有,返回true
        if(count<=0){
            return true;
        }else{
            return false;
        }*/
        //如果有,返回false
        return logininfoMapper.selectCountByUsername(username)<=0;
    }

    public Logininfo login(String username, String password,int userType) {
        //1.先根据用户名和密码在数据库中查找logininfo对象
        Logininfo logininfo = logininfoMapper.login(username,MD5.encode(password),userType);
        //如果能找到,把查询到的logininfo对象放入到session中.
        //记录登录日志
        IpLog ipLog = new IpLog();
        ipLog.setUsername(username);
        ipLog.setLoginTime(new Date());
        ipLog.setIp(UserContext.getIp());
        ipLog.setUserType(userType);
        if(logininfo!=null){
            ipLog.setState(IpLog.LOGIN_SUCCESS);
            //把对象放入到session中
            UserContext.setCurrent(logininfo);
            ipLogService.save(ipLog);
        }else{
            ipLog.setState(IpLog.LOGIN_FAILED);
            ipLogService.save(ipLog);
            //如果找不到,抛出异常
        }
        return logininfo;
    }

    public void initAdmin() {
        //1.根据类型查找数据库中是否有管理员.
        int count = logininfoMapper.queryCountByUserType(Logininfo.USERTYPE_MANAGER);
        //如果没有，创建第一个管理员
        if(count<=0){
            Logininfo logininfo = new Logininfo();
            logininfo.setState(Logininfo.STATE_NORMAL);
            logininfo.setUserType(Logininfo.USERTYPE_MANAGER);
            logininfo.setUsername(BidConst.MANAGER_ACCOUNT);
            logininfo.setPassword(MD5.encode(BidConst.MANAGER_PASSWORD));
           this.logininfoMapper.insert(logininfo);
        }
    }

	public List<Map<String, Object>> autocomplate(String keyword) {

		return logininfoMapper.autocomplate(keyword,Logininfo.USERTYPE_USER);
	}
}
