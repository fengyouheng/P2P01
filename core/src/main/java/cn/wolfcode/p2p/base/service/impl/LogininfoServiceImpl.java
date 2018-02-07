package cn.wolfcode.p2p.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.mapper.LogininfoMapper;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.util.AjaxResult;
import cn.wolfcode.p2p.base.util.MD5;

@Service@Transactional
public class LogininfoServiceImpl implements ILogininfoService{

	@Autowired
	private LogininfoMapper logininfoMapper;
	
	@Override
	public Logininfo register(String username, String password) {
		//1:判断登录名是否存在
		int count = logininfoMapper.selectCountByUsername(username);
		//如果存在
		if(count>0){
			throw new RuntimeException("用户名已存在");
		}
		//如果不存在
		Logininfo logininfo = new Logininfo();
		logininfo.setUsername(username);
		logininfo.setPassword(MD5.encode(password));
		logininfo.setState(Logininfo.STATE_NORMAL);
		logininfoMapper.insert(logininfo);
		return null;
	}

	@Override
	public boolean checkUsername(String username) {
		// 根据传入的用户名去数据库汇总查找
		return logininfoMapper.selectCountByUsername(username)<=0;
	}

	@Override
	public Logininfo login(String username, String password) {
		Logininfo logininfo = logininfoMapper.login(username,MD5.encode(password));
		if(logininfo != null){
			//将logininfo放入session中
		}else{
			//找不到,抛出异常
			throw new RuntimeException("用户名密码有误");
		}
		return logininfo;
	}
	
	
}
