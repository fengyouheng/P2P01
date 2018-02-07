package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.Logininfo;

public interface ILogininfoService {
	Logininfo register(String username,String password);

	boolean checkUsername(String username);

	Logininfo login(String username, String password);
}
