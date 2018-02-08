package cn.wolfcode.p2p.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.domain.SystemAccountFlow;
import cn.wolfcode.p2p.business.mapper.SystemAccountFlowMapper;
import cn.wolfcode.p2p.business.service.ISystemAccountFlowService;

@Service@Transactional
public class SystemAccountFlowServiceImpl implements ISystemAccountFlowService{

	@Autowired
	private SystemAccountFlowMapper systemAccountFlowMapper;
	
	public int save(SystemAccountFlow systemAccountFlow) {
		return systemAccountFlowMapper.insert(systemAccountFlow);
	}
	//借款手续费
	public void createGainAccountManagerChargeFlow(SystemAccount systemAccount,BigDecimal calAccountManagementCharge) {
		createFlow(systemAccount,calAccountManagementCharge,BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE,"收取用户的借款管理费:"+calAccountManagementCharge+"元");
	}
	private void createFlow(SystemAccount systemAccount,BigDecimal amount,int actionType,String remark){
		SystemAccountFlow flow = new SystemAccountFlow();
		flow.setActionTime(new Date());
		flow.setActionType(actionType);
		flow.setAmount(amount);
		flow.setFreezedAmount(systemAccount.getFreezedAmount());
		flow.setRemark(remark);
		flow.setUsableAmount(systemAccount.getUsableAmount());
		this.save(flow);
	}
	//利息管理费
	public void createGainInterestManagerChargeFlow(SystemAccount account,BigDecimal amount) {
		createFlow(account,amount,BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE,"收取用户的利息管理费:"+amount+"元");

	}
}
