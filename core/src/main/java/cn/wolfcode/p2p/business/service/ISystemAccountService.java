package cn.wolfcode.p2p.business.service;

import java.math.BigDecimal;

import cn.wolfcode.p2p.business.domain.SystemAccount;

public interface ISystemAccountService {
	int save(SystemAccount systemAccount);
	int update(SystemAccount systemAccount);
	
	SystemAccount getCurrent();
	void initSystemAccount();
}
