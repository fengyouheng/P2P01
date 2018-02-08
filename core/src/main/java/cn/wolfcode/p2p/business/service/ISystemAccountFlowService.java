package cn.wolfcode.p2p.business.service;

import java.math.BigDecimal;

import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.domain.SystemAccountFlow;

public interface ISystemAccountFlowService {
	int save(SystemAccountFlow systemAccountFlow);

	void createGainAccountManagerChargeFlow(SystemAccount systemAccount,BigDecimal calAccountManagementCharge);

	void createGainInterestManagerChargeFlow(SystemAccount account,BigDecimal amount);
}
