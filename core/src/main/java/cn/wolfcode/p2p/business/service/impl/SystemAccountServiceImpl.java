package cn.wolfcode.p2p.business.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.mapper.SystemAccountMapper;
import cn.wolfcode.p2p.business.service.ISystemAccountService;

@Service@Transactional
public class SystemAccountServiceImpl implements ISystemAccountService{

	@Autowired
	private SystemAccountMapper systemAccountMapper;
	
	public int save(SystemAccount systemAccount) {
		return systemAccountMapper.insert(systemAccount);
	}

	public int update(SystemAccount systemAccount) {
		int count = systemAccountMapper.updateByPrimaryKey(systemAccount);
		if(count <= 0){
			throw new RuntimeException("乐观锁异常,systemAccountId:"+systemAccount.getId()+"元");
		}
		return count;
	}

	public SystemAccount getCurrent() {
		return systemAccountMapper.selectCurrent();
	}

	public void initSystemAccount() {
		SystemAccount systemAccount = this.getCurrent();
		if(systemAccount==null){
			systemAccount = new SystemAccount();
			this.save(systemAccount);
		}
	}
}
